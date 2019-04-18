package org.smither.opwatch.spigot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.smither.opwatch.utils.SignChangeType;
import org.smither.opwatch.utils.sharedDTO.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;

public class RestController {
    public static void sendSignToServer(SignChangeEvent sce) {
        SignPostDTO dto =
                new SignPostDTO(
                        sce.getLine(0),
                        sce.getLine(1),
                        sce.getLine(2),
                        sce.getLine(3),
                        LocalDateTime.now(),
                        sce.getPlayer().getUniqueId(),
                        UUID.randomUUID(),
                        UUID.fromString(Plugin.getInstance().getConfig().getString("userID")),
                        sce.getBlock().getX(),
                        sce.getBlock().getY(),
                        sce.getBlock().getZ(),
                        sce.getBlock().getWorld().getName());
        sendDto(dto);
    }

    public static void deleteSignFromServer(Block block, Player player, SignChangeType type, String reason) {
        SignEventPostDTO dto =
                new SignEventPostDTO(
                        UUID.randomUUID(),
                        UUID.fromString(Plugin.getInstance().getConfig().getString("userID")),
                        block.getX(),
                        block.getY(),
                        block.getZ(),
                        block.getWorld().getName(),
                        LocalDateTime.now(),
                        type,
                        player.getUniqueId().toString(),
                        reason,
                        null,
                        null,
                        null,
                        null);
        sendDto(dto);
    }

    public static void sendDto(SignPostDTO dto) {
        try {
            byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
            postRequest(new URI(Plugin.getInstance().getConfig().getString("serverURL") + "/sign"), body, AuthManager.get().getToken());
        } catch (JsonProcessingException | URISyntaxException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting SignPostDTO", e);
        }
    }

    public static void sendDto(ErrorDTO dto) {
        try {
            byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
            postRequest(new URI(Plugin.getInstance().getConfig().getString("serverURL") + "/problem"), body, AuthManager.get().getToken());
        } catch (JsonProcessingException | URISyntaxException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting ErrorDTO", e);
        }
    }

    public static void sendDto(SignEventPostDTO dto) {
        try {
            byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
            postRequest(new URI(Plugin.getInstance().getConfig().getString("serverUrl") + "/signEvent"), body, AuthManager.get().getToken());
        } catch (JsonProcessingException | URISyntaxException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting SignEventDTO", e);
        }
    }

    public static TokenReturnDTO sendDto(LoginDTO dto) {
        try {
            byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
            byte[] returned = postRequest(new URI(Plugin.getInstance().getConfig().getString("serverUrl") + "/token"), body, "");
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(returned, TokenReturnDTO.class);
            } catch (IOException e) {
                Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting LoginDTO", e);
            }
        } catch (URISyntaxException | JsonProcessingException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting LoginDTO", e);
        }
        return null;
    }

    private static byte[] postRequest(URI url, byte[] body, String token) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting a DTO", e);
        }
        try {
            return IOUtils.toByteArray(response.getEntity().getContent());
        } catch (IOException e) {
            Plugin.getInstance().getLogger().log(Level.WARNING, "Error whilst posting LoginDTO", e);
        }
        return new byte[0];
    }
}
