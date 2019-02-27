package org.smither.opwatch.utils.SocketDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.smither.opwatch.utils.Success;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventAck {

    private UUID id;

    @NonNull
    private Success success;
}
