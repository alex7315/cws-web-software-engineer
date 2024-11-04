import React, { Component } from "react";

import Auth from "./components/auth.component";
import AuthService from "./services/auth.service";

class App extends Component {
  constructor(props) {
    super(props);

    this.logOut = this.logOut.bind(this);
  }

  logOut() {
    AuthService.logout();
  }

  render() {
    return (
      <div>
        <Auth />
      </div>
    );
  }
}

export default App;