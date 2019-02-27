package org.smither.opwatch.utils.SocketDTO;

import lombok.*;
import org.smither.opwatch.utils.SignChangeType;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignChangeRequestDTO {
  private UUID id;
  private int x;
  private int y;
  private int z;
  private UUID server;
  private String world;
  private String actioner;
  private SignChangeType eventType;
  private String reason;
  private String newLine1;
  private String newLine2;
  private String newLine3;
  private String newLine4;
}
