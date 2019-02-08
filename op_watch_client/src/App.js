import React, { Component } from "react";
import SocketHandler from "../socketHandler";
import SockJsClient from "react-stomp";
import "./App.css";

class App extends Component {
  componentDidMount() {
    this.socketHandler = new SocketHandler(this);
    this.socketHandler.connect();
  }
  render() {
    return (
      <div className="App">
        <header className="App-header" />
      </div>
    );
  }
}

export default App;
