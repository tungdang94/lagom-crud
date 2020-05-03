package vn.com.truemoney.user.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import vn.com.truemoney.user.api.UserService;
import vn.com.truemoney.user.impl.repository.UserRepository;

/**
 * The module that binds the {@link UserService} so that it can be served.
 */
public class UserModule extends AbstractModule implements ServiceGuiceSupport {

  @Override
  protected void configure() {
    bindService(UserService.class, UserServiceImpl.class);
    bind(UserRepository.class);
  }

}
