package vn.com.truemoney.user.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;
import vn.com.truemoney.user.api.model.UserView;

/**
 * Modelling Events
 * {@link https://www.lagomframework.com/documentation/1.6.x/java/UsingAkkaPersistenceTyped.html#Modelling-Events}
 */
public interface UserEvent extends Jsonable, AggregateEvent<UserEvent> {

  @Override
  default AggregateEventTagger<UserEvent> aggregateTag() {
    return UserEventTag.USER_EVENT_TAG;
  }

  @Value
  @Builder
  @JsonDeserialize
  final class UserCreated implements UserEvent, CompressedJsonable {
    UserView userView;
    String entityId;
  }

  @Value
  @Builder
  @JsonDeserialize
  final class UserUpdated implements UserEvent, CompressedJsonable {
    UserView userView;
    String entityId;
  }

  @Value
  @Builder
  @JsonDeserialize
  final class UserDeleted implements UserEvent, CompressedJsonable {
    UserView userView;
    String entityId;
  }

}
