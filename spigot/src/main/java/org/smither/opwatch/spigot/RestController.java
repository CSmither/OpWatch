package org.smither.opwatch.spigot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.smither.opwatch.utils.SignChangeType;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;
import org.smither.opwatch.utils.sharedDTO.SignPostDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.UUID;

public class RestController {
  public void sendSignToServer(SignChangeEvent sce) {
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
            sce.getBlock().getWorld().getName(),
            false);
      try {
          byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
          postRequest(new URL(Plugin.getInstance().getConfig().getString("serverUrl") + "/sign"), body);
      } catch (JsonProcessingException | MalformedURLException e1) {
          e1.printStackTrace();
      }
  }

  public void deleteSignFromServer(Block block, Player player) {
    SignEventPostDTO dto =
        new SignEventPostDTO(
            UUID.fromString(Plugin.getInstance().getConfig().getString("userID")),
            block.getX(),
            block.getY(),
            block.getZ(),
            block.getWorld().getName(),
            LocalDateTime.now(),
            SignChangeType.delete,
            player.getUniqueId().toString(),
            "Sign Destroyed",
            null,
            null,
            null,
            null);
      try {
          byte[] body = new ObjectMapper().writeValueAsString(dto).getBytes();
          postRequest(new URL(Plugin.getInstance().getConfig().getString("serverUrl") + "/signEvent"), body);
      } catch (JsonProcessingException | MalformedURLException e1) {
          e1.printStackTrace();
      }
  }

  private int postRequest(URL url, byte[] body) {
    try {
      URLConnection con = url.openConnection();
      HttpURLConnection http = (HttpURLConnection) con;
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      http.setFixedLengthStreamingMode(body.length);
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.connect();
      try (OutputStream os = http.getOutputStream()) {
        os.write(body);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      return http.getResponseCode();
    } catch (IOException e1) {
      e1.printStackTrace();
      return -1;
    }
  }
}
