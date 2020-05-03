package vn.com.truemoney.user.impl.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

  @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @Column(columnDefinition = "serial")
  private String id;

  @NotNull
  private String name;

  @NotNull
  private Integer age;
}
