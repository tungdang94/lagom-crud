package vn.com.truemoney.user.impl.repository;

import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import vn.com.truemoney.user.impl.events.UserEventProcessor;
import vn.com.truemoney.user.impl.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Singleton
public class UserRepository {

  private final JpaSession jpaSession;

  @Inject
  public UserRepository(JpaSession jpaSession, ReadSide readSide) {
    this.jpaSession = jpaSession;
    readSide.register(UserEventProcessor.class);
  }

  public CompletionStage<User> findById(String userId) {
    return jpaSession.withTransaction(em -> em.find(User.class, userId));
  }

}
