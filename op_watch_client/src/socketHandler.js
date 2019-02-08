import * as io from "socket.io-client";

export class SocketHandler {
  connect() {
    const socket = io.connect("https://localhost:8080");
    socket.on("connect", () => {});
    socket.on("signs", data => {
      console.log(data);
    });
  }
}
