package org.smither.opwatch.utils.sharedDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenReturnDTO {

  private String access;

  private String refresh;
}
