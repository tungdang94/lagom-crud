package vn.com.truemoney.user.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize
public class UserView implements Jsonable {
  String id;
  String name;
  int age;
}
