package org.smither.opwatch.server.signEvents;

import lombok.*;
import org.hibernate.annotations.Type;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.utils.SignChangeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NotNull
@NonNull
public final class SignEvent {

  @Id
  @Type(type = "uuid-char")
  private UUID id;

  private SignChangeType type;

  private String line1;
  private String line2;
  private String line3;
  private String line4;

  private LocalDateTime time;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Sign sign;

  private String actioner;

  private String reason;
}
