import axios from 'axios'

const API_URL = process.env.REACT_APP_API_AUTH_URL

const login = (username: string, password: string) => {
  return axios
    .post(API_URL + '/signin', {
      username,
      password,
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data))
      }

      return response.data
    })
}

const logout = () => {
  console.log('calls logout')

  localStorage.removeItem('user')
}

const getCurrentUser = () => {
  const itemString = localStorage.getItem('user')
  return itemString ? JSON.parse(itemString) : null
}

const AuthService = {
  login,
  logout,
  getCurrentUser,
}

export default AuthService
