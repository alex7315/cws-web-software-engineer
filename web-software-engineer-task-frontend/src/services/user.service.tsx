import axios from 'axios'

const API_URL = process.env.REACT_APP_API

const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

axiosInstance.interceptors.request.use(
  (config) => {
    const itemString = localStorage.getItem('user')
    const tokenValue = itemString ? JSON.parse(itemString).token : ''
    config.headers['Authorization'] = 'Bearer ' + tokenValue
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

axiosInstance.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalConfig = error.config
    if (
      error.response &&
      error.response.status === 401 &&
      !originalConfig._retry
    ) {
      originalConfig._retry = true
      const itemString = localStorage.getItem('user')
      const refreshTokenValue = itemString
        ? JSON.parse(itemString).refreshToken
        : ''
      await axiosInstance
        .post('/auth/refreshtoken', {
          refreshToken: refreshTokenValue,
        })
        .then((rs) => {
          if (rs.data.token) {
            let user = itemString ? JSON.parse(itemString) : null
            user.token = rs.data.token
            localStorage.setItem('user', JSON.stringify(user))
          }
          return axiosInstance(originalConfig)
        })
        .catch((error2) => {
          console.log(
            'refreshtoken response error. Response status: ' +
              error2.response.status
          )
          return Promise.reject(error2)
        })
    }

    return Promise.reject(error)
  }
)

export default axiosInstance
