package vn.com.truemoney.user.impl.events;

import akka.Done;
import com.google.common.collect.ImmutableMap;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaReadSide;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.truemoney.user.impl.events.UserEvent.UserCreated;
import vn.com.truemoney.user.impl.events.UserEvent.UserUpdated;
import vn.com.truemoney.user.impl.events.UserEvent.UserDeleted;
import vn.com.truemoney.user.impl.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.concurrent.CompletionStage;

/**
 * A read-side processor is responsible not just for handling the events produced by the persistent entity,
 * it’s also responsible for tracking which events it has handled.
 * It will consume events produced by persistent entities and update the database table.
 * <p>
 * {@link https://www.lagomframework.com/documentation/1.6.x/java/ReadSide.html#Read-side-design}
 */
public class UserEventProcessor extends ReadSideProcessor<UserEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserEventProcessor.class);

  private final JpaSession jpaSession;
  private final JpaReadSide jpaReadSide;

  @Inject
  public UserEventProcessor(JpaSession jpaSession, JpaReadSide jpaReadSide) {
    this.jpaSession = jpaSession;
    this.jpaReadSide = jpaReadSide;
  }

  @Override
  public ReadSideHandler<UserEvent> buildHandler() {
    LOGGER.info(" buildHandler method ... ");
    return jpaReadSide.<UserEvent>builder("users_offset")
        .setGlobalPrepare(this::createSchema)
        .setEventHandler(UserCreated.class, this::createUser)
        .setEventHandler(UserUpdated.class, this::updateUser)
        .setEventHandler(UserDeleted.class, this::deleteUser)
        .build();
  }

  // Execute only once while application is start
  private void createSchema(@SuppressWarnings("unused") EntityManager ignored) {
    Persistence.generateSchema("default", ImmutableMap.of("hibernate.hbm2ddl.auto", "update"));
  }

  private void createUser(EntityManager entityManager, UserCreated evt) {
    LOGGER.debug("Received UserCreated event: " + evt);
    User user = User.builder()
        .id(evt.getUserView().getId())
        .name(evt.getUserView().getName())
        .age(evt.getUserView().getAge())
        .build();
    entityManager.persist(user);
  }

  private void updateUser(EntityManager entityManager, UserUpdated evt) {
    LOGGER.debug("Received UserUpdated event: " + evt);
    User saved = findUser(entityManager, evt.getUserView().getId());
    if (saved == null) {
      throw new RuntimeException("Didn't find user with id: " + evt.getUserView().getId());
    }

    saved.setName(evt.getUserView().getName());
    saved.setAge(evt.getUserView().getAge());
    entityManager.persist(saved);
  }

  private void deleteUser(EntityManager entityManager, UserDeleted evt) {
    LOGGER.debug("Received UserDeleted event: " + evt);
    User saved = findUser(entityManager, evt.getUserView().getId());
    if (saved == null) {
      throw new RuntimeException("Didn't find user with id: " + evt.getUserView().getId());
    }
    entityManager.remove(saved);
  }

  private User findUser(EntityManager entityManager, String userId) {
    return entityManager.find(User.class, userId);
  }

  @Override
  public PSequence<AggregateEventTag<UserEvent>> aggregateTags() {
    LOGGER.info(" aggregateTags method ... ");
    return TreePVector.singleton(UserEventTag.USER_EVENT_TAG);
  }

}
