package vn.com.truemoney.user.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import vn.com.truemoney.user.impl.commands.UserCommand;
import vn.com.truemoney.user.impl.commands.UserCommand.CreateUser;
import vn.com.truemoney.user.impl.commands.UserCommand.UpdateUser;
import vn.com.truemoney.user.impl.commands.UserCommand.DeleteUser;
import vn.com.truemoney.user.impl.commands.UserCommand.UserCurrentState;
import vn.com.truemoney.user.impl.events.UserEvent;
import vn.com.truemoney.user.impl.events.UserEvent.UserCreated;
import vn.com.truemoney.user.impl.events.UserEvent.UserUpdated;
import vn.com.truemoney.user.impl.events.UserEvent.UserDeleted;
import vn.com.truemoney.user.impl.states.UserState;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserEntity extends PersistentEntity<UserCommand, UserEvent, UserState> {

  @Override
  public Behavior initialBehavior(Optional<UserState> snapshotState) {

    // Behavior consist of a State and defined event handlers and command handlers.
    BehaviorBuilder behaviorBuilder = newBehaviorBuilder(UserState.EMPTY);

    behaviorBuilder.setCommandHandler(
        CreateUser.class,
        (cmd, ctx) -> {
          final UserCreated evt = UserCreated.builder().userView(cmd.getUserView()).entityId(entityId()).build();
          return ctx.thenPersist(
              evt,
              persistedEvt -> ctx.reply(Done.getInstance())
          );
        }
    );

    behaviorBuilder.setEventHandler(
        UserCreated.class,
        evt -> UserState.builder()
            .userView(Optional.of(evt.getUserView()))
            .timestamp(LocalDateTime.now().toString())
            .build());

    behaviorBuilder.setCommandHandler(
        UpdateUser.class,
        (cmd, ctx) -> {
          final UserUpdated evt = UserUpdated.builder().userView(cmd.getUserView()).entityId(entityId()).build();
          return ctx.thenPersist(
              evt,
              persistedEvt -> ctx.reply(Done.getInstance())
          );
        }
    );

    behaviorBuilder.setEventHandler(
        UserUpdated.class,
        evt -> UserState.builder().userView(Optional.of(evt.getUserView()))
            .timestamp(LocalDateTime.now().toString()).build()
    );

    behaviorBuilder.setCommandHandler(
        DeleteUser.class,
        (cmd, ctx) -> {
          final UserDeleted evt = UserDeleted.builder().userView(cmd.getUserView()).entityId(entityId()).build();
          return ctx.thenPersist(
              evt,
              persistedEvt -> ctx.reply(cmd.getUserView()));
        }
    );

    behaviorBuilder.setEventHandler(
        UserDeleted.class,
        evt -> UserState.builder().userView(Optional.empty())
            .timestamp(LocalDateTime.now().toString()).build()
    );

    behaviorBuilder.setReadOnlyCommandHandler(
        UserCurrentState.class,
        (cmd, ctx) -> ctx.reply(state().getUserView())
    );

    return behaviorBuilder.build();
  }

}
