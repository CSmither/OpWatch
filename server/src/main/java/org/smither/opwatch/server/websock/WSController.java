package org.smither.opwatch.server.websock;

import com.corundumstudio.socketio.SocketIOServer;
import org.smither.opwatch.server.auth.AuthService;
import org.smither.opwatch.server.signEvents.SignEventService;
import org.smither.opwatch.server.signs.SignService;
import org.smither.opwatch.utils.SocketDTO.SignChangeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class WSController {

    @Autowired
    private SocketIOServer socketIoServer;

    private ConnectionListener connectionListener;

    @Autowired
    public WSController(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    @PostConstruct
    public void start() {
        socketIoServer.start();
        socketIoServer.addConnectListener(connectionListener);
    }

    @PreDestroy
    public void stop() {
        socketIoServer.stop();
    }

    public void sendSignChangeRequest(SignChangeRequestDTO dto) {

    }

}