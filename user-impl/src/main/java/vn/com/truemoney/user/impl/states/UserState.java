package vn.com.truemoney.user.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Builder;
import lombok.Value;
import vn.com.truemoney.user.api.model.UserView;

import java.time.LocalDateTime;
import java.util.Optional;

@Value
@Builder
@JsonDeserialize
public class UserState implements CompressedJsonable {

  public static final UserState EMPTY = UserState.builder()
      .userView(Optional.empty())
      .timestamp(LocalDateTime.now().toString())
      .build();

  Optional<UserView> userView;
  String timestamp;
}
