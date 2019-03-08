package org.smither.opwatch.spigot;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.smither.opwatch.utils.sharedDTO.LoginDTO;
import org.smither.opwatch.utils.sharedDTO.TokenReturnDTO;

import java.util.Date;

public class AuthManager {

    private static AuthManager instance;

    public static AuthManager get() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    private String username;
    private String password;
    private String token = "";
    private String refreshToken = "";

    public AuthManager() {
        loadAuthDetails();
    }

    private void loadAuthDetails() {
        username = Plugin.getInstance().getConfig().getString("userID");
        password = Plugin.getInstance().getConfig().getString("userSecret");
    }

    public String getToken() {
        if (!tokenIsValid(token)) {
            refreshToken();
        }
        return token;
    }

    private boolean tokenIsValid(String token) {
        boolean valid = true;
        try {
            DecodedJWT decodedToken = JWT.decode(token);
            valid = valid && decodedToken.getExpiresAt().after(new Date());
        } catch (JWTDecodeException decodeEx) {
            valid = false;
        }
        return valid;
    }

    private void refreshToken() {
        if (tokenIsValid(refreshToken)) {
            TokenReturnDTO tokens = RestController.sendDto(LoginDTO.builder().refresh(refreshToken).build());
            refreshToken = tokens.getRefresh();
            token = tokens.getAccess();
        }
    }
}
