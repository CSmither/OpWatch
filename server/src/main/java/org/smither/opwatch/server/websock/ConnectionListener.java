package org.smither.opwatch.server.websock;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.smither.opwatch.server.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectionListener implements ConnectListener {

    private AuthService authService;

    private SocketUserAuthenticator socketUserAuthenticator;

    @Autowired
    public ConnectionListener(AuthService authService, SocketUserAuthenticator socketUserAuthenticator) {
        this.authService = authService;
        this.socketUserAuthenticator = socketUserAuthenticator;
    }

    @Override
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getHttpHeaders().get("Authorisation");
        socketUserAuthenticator.login(client, token);
    }
}
