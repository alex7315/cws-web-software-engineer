import React, { Component } from "react";
import { Routes, Route } from "react-router-dom";

import AuthService from "./services/auth.service";
import Auth from "./components/auth.component";
import Users from "./components/users.component";

type Props = {};
class App extends Component<Props> {
  constructor(props: Props) {
    super(props);

    this.logOut = this.logOut.bind(this);
  }

  logOut() {
    AuthService.logout();
  }

  render() {
    return (
        <div className="container mt-3">
          <Routes>
            <Route path="/" element={<Auth />} />
            <Route path="/users" element={<Users />} />
          </Routes>
        </div>
    );
  }
}

export default App;
