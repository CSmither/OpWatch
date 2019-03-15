package org.smither.opwatch.spigot;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import org.smither.opwatch.utils.SocketDTO.EventAck;
import org.smither.opwatch.utils.SocketDTO.EventTypes;
import org.smither.opwatch.utils.SocketDTO.SignChangeRequestDTO;
import org.smither.opwatch.utils.Success;

import java.net.URISyntaxException;

public class WsController {
    private Socket socket;
    private boolean authed;

    public WsController() throws URISyntaxException {
        connect();
    }

    private void connect() throws URISyntaxException {
        authed = false;
        IO.Options options = new IO.Options();
        socket = IO.socket(Plugin.getInstance().getConfig().getString("serverURL"), options);
        socket.on(Socket.EVENT_CONNECT, args -> {
            JSONObject auth = new JSONObject();
            try {
                auth.put("username", Plugin.getInstance().getConfig().getString("userID"));
                auth.put("password", Plugin.getInstance().getConfig().getString("userSecret"));
                socket.emit("auth", auth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        socket.on(EventTypes.AUTH_ACK, args -> authed = ((EventAck) args[0]).getSuccess() == Success.OK);
        socket.on(EventTypes.SIGN_REQUEST, args -> {
            SignChangeRequestDTO dto = (SignChangeRequestDTO) args[0];
            SignManager.changeSign(dto);
        });
        socket.connect();
    }
}
