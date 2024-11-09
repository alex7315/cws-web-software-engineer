import axios from "axios";

const API_URL = process.env.REACT_APP_API_AUTH_URL;

const login = (username, password) => {
  return axios
  .post(API_URL + "/signin", {
    username,
    password
  })
  .then(response => {
    if (response.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data));
    }

    return response.data;
  });
}

const logout = () => {
  localStorage.removeItem("/user");
}

const register = (username, email, password)  => {
  return axios.post(API_URL + "/signup", {
    username,
    email,
    password
  });
}

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
}

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default AuthService;