package vn.com.truemoney.user.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import jdk.nashorn.internal.runtime.options.Option;
import vn.com.truemoney.user.api.model.UserView;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

/**
 * The user service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and consume the UserService.
 */
public interface UserService extends Service {

  ServiceCall<NotUsed, UserView> getUser(String id);

  ServiceCall<UserView, Done> createUser();

  ServiceCall<UserView, Done> updateUser();

  ServiceCall<NotUsed, UserView> deleteUser(String id);

  ServiceCall<NotUsed, Optional<UserView>> currentState(String id);

  @Override
  default Descriptor descriptor() {
    return named("user")
        .withCalls(
            restCall(GET, "/api/user/:id", this::getUser),
            restCall(POST, "/api/user", this::createUser),
            restCall(PUT, "/api/user", this::updateUser),
            restCall(DELETE, "/api/user/:id", this::deleteUser),
            restCall(GET, "/api/user/current-state/:id", this::currentState)
        )
        .withAutoAcl(true);
  }
}
