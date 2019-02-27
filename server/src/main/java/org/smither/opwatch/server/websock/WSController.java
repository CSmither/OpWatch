package org.smither.opwatch.server.websock;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.smither.opwatch.server.signEvents.SignEvent;
import org.smither.opwatch.server.signEvents.SignEventService;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.server.signs.SignService;
import org.smither.opwatch.server.users.User;
import org.smither.opwatch.utils.SocketDTO.EventAck;
import org.smither.opwatch.utils.SocketDTO.SignChangeRequestDTO;
import org.smither.opwatch.utils.Success;
import org.smither.opwatch.utils.sharedDTO.LoginDTO;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;
import org.smither.opwatch.utils.sharedDTO.SignPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityExistsException;
import java.util.UUID;

@Service
public class WSController {

    private SocketIOServer socketIoServer;

    private SocketUserAuthenticator socketUserAuthenticator;

    private SignEventService signEventService;

    private SignService signService;

    private SocketUserAuthenticator userAuth;

    @Autowired
    public WSController(@Value("${ws.port}") int port, SocketUserAuthenticator socketUserAuthenticator, SignEventService signEventService, SignService signService, SocketUserAuthenticator userAuth) {
        this.socketUserAuthenticator = socketUserAuthenticator;
        this.signEventService = signEventService;
        this.signService = signService;
        this.userAuth = userAuth;
        Configuration conf=new Configuration();
        conf.setPort(port);
        socketIoServer=new SocketIOServer(conf);
    }

    @PostConstruct
    public void start() {
        socketIoServer.start();
        socketIoServer.addEventListener("auth", LoginDTO.class, (client, data, ackRequest) -> {
            Success success = Success.UNKNOWN;
            try {
                String token = client.getHandshakeData().getHttpHeaders().get("Authorisation");
                User user = socketUserAuthenticator.login(client, token);
                client.getSessionId();
                client.sendEvent("authAck", new EventAck(Success.OK));
            } catch (Throwable t) {
                success = Success.FAILED;
            } finally {
                ackRequest.sendAckData(new EventAck(success));
            }
        });
        socketIoServer.addEventListener("signEvent", SignEventPostDTO.class, (client, data, ackRequest) -> {
            Success success = Success.UNKNOWN;
            try {
                SignEvent signEvent = signEventService.createSignEvent(data);
                success = Success.OK;
            } catch (EntityExistsException ex) {
                success = Success.ID_CLASH;
            } catch (Throwable t) {
                success = Success.FAILED;
            } finally {
                ackRequest.sendAckData(new EventAck(data.getId(), success));
            }
        });
        socketIoServer.addEventListener("sign", SignPostDTO.class, (client, data, ackRequest) -> {
            Success success = Success.UNKNOWN;
            try {
                Sign sign = signService.createSign(data);
                success = Success.OK;
            } catch (EntityExistsException ex) {
                success = Success.ID_CLASH;
            } catch (Throwable t) {
                success = Success.FAILED;
            } finally {
                ackRequest.sendAckData(new EventAck(data.getId(), success));
            }
        });

    }

    @PreDestroy
    public void stop() {
        socketIoServer.stop();
    }

    public void sendSignChangeRequest(SignChangeRequestDTO dto) {
        SocketIOClient client = userAuth.getClientForId(dto.getServer());
        client.sendEvent("signRequest");
    }

    private void reportSuccess(UUID id, Success success) {

    }

}