package org.smither.opwatch.server.websock;

import com.corundumstudio.socketio.SocketIOClient;
import org.smither.opwatch.server.auth.AuthService;
import org.smither.opwatch.server.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SocketUserAuthenticator {
    private Map<SocketIOClient, Authentication> socketId = new HashMap<>();
    private Map<User, SocketIOClient> userSockets = new HashMap<>();

    private AuthService authService;

    @Autowired
    public SocketUserAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    public void login(SocketIOClient client, String token) {
        Authentication auth = authService.getPrinciple(token);
        socketId.put(client, auth);
        userSockets.put((User) auth.getPrincipal(), client);
        client.set("user", auth.getPrincipal());
    }
}
