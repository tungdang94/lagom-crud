package vn.com.truemoney.user.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import vn.com.truemoney.user.api.UserService;
import vn.com.truemoney.user.api.model.UserView;
import vn.com.truemoney.user.impl.commands.UserCommand;
import vn.com.truemoney.user.impl.commands.UserCommand.DeleteUser;
import vn.com.truemoney.user.impl.commands.UserCommand.UpdateUser;
import vn.com.truemoney.user.impl.commands.UserCommand.UserCurrentState;
import vn.com.truemoney.user.impl.model.User;
import vn.com.truemoney.user.impl.repository.UserRepository;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Implementation of the {@link UserService}.
 */
public class UserServiceImpl implements UserService {

  private final PersistentEntityRegistry persistentEntityRegistry;
  private final UserRepository userRepository;

  @Inject
  public UserServiceImpl(PersistentEntityRegistry persistentEntityRegistry,
                         UserRepository userRepository) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    this.userRepository = userRepository;

    this.persistentEntityRegistry.register(UserEntity.class);
  }

  private PersistentEntityRef<UserCommand> userEntityRef(String userId) {
    return persistentEntityRegistry.refFor(UserEntity.class, userId);
  }

  @Override
  public ServiceCall<NotUsed, UserView> getUser(String id) {
    return request -> userRepository.findById(id).thenApply(user -> {
      if (user != null)
        return toUserView(user);
      else
        throw new NotFound("Couldn't find a user for '" + id + "'");
    });
  }

  @Override
  public ServiceCall<UserView, Done> createUser() {
    return user -> {
      PersistentEntityRef<UserCommand> ref = userEntityRef(user.getId());
      return ref.ask(UserCommand.CreateUser.builder().userView(user).build());
    };
  }

  @Override
  public ServiceCall<UserView, Done> updateUser() {
    return user -> {
      PersistentEntityRef<UserCommand> ref = userEntityRef(user.getId());
      return ref.ask(UpdateUser.builder().userView(user).build());
    };
  }

  @Override
  public ServiceCall<NotUsed, UserView> deleteUser(String id) {
    return request -> {
      PersistentEntityRef<UserCommand> ref = userEntityRef(id);
      UserView userView = UserView.builder().id(id).build();
      return ref.ask(DeleteUser.builder().userView(userView).build());
    };
  }

  @Override
  public ServiceCall<NotUsed, Optional<UserView>> currentState(String id) {
    return request -> {
      PersistentEntityRef<UserCommand> ref = userEntityRef(id);
      return ref.ask(new UserCurrentState());
    };
  }

  private UserView toUserView(User entity) {
    return UserView.builder()
        .id(entity.getId())
        .name(entity.getName())
        .age(entity.getAge())
        .build();
  }

}
