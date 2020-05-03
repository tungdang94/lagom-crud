package vn.com.truemoney.user.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;
import vn.com.truemoney.user.api.model.UserView;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * Modelling Commands and Replies
 * {@link https://www.lagomframework.com/documentation/1.6.x/java/UsingAkkaPersistenceTyped.html#Modelling-Commands-and-Replies}
 */
public interface UserCommand extends Jsonable {

  @Value
  @Builder
  @JsonDeserialize
  final class CreateUser implements UserCommand, PersistentEntity.ReplyType<Done> {
    UserView userView;
  }

  @Value
  @Builder
  @JsonDeserialize
  final class UpdateUser implements UserCommand, PersistentEntity.ReplyType<Done> {
    UserView userView;
  }

  @Value
  @Builder
  @JsonDeserialize
  final class DeleteUser implements UserCommand, PersistentEntity.ReplyType<UserView> {
    UserView userView;
  }

  @Immutable
  @JsonDeserialize
  final class UserCurrentState implements UserCommand, PersistentEntity.ReplyType<Optional<UserView>> {
  }

}
