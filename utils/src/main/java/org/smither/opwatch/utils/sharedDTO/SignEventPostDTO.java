package org.smither.opwatch.utils.sharedDTO;

import lombok.*;
import org.smither.opwatch.utils.SignChangeReason;
import org.smither.opwatch.utils.SignChangeType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignEventPostDTO {

  private UUID server;

  private int x;

  private int y;

  private int z;

  private String world;

  private LocalDateTime dateTime;

  private SignChangeType eventType;

  private String editor;

  private SignChangeReason reason;

  private String newLine1;
  private String newLine2;
  private String newLine3;
  private String newLine4;

}
