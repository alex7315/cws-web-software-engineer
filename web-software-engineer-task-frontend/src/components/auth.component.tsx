import React, { useState } from 'react'
import {
  Box,
  TextField,
  Button,
  Typography,
  Snackbar,
  Container,
  Paper,
} from '@mui/material'
import LoginIcon from '@mui/icons-material/Login'

import { useNavigate } from 'react-router-dom'

import AuthService from '../services/auth.service'

const Auth = () => {
  interface IAuthInput {
    username: string
    email: string
    password: string
    authError: boolean
    message: string
  }

  const [inputs, setInputs] = useState<IAuthInput>({
    username: '',
    email: '',
    password: '',
    authError: false,
    message: '',
  })

  const [snackState, setSnackState] = useState(false)

  const handleSnackClose = () => {
    setSnackState(false)
  }

  const handleChange = (e: React.SyntheticEvent) => {
    let target = e.target as HTMLInputElement
    setInputs((prevState) => ({
      ...prevState,
      [target.name]: target.value,
    }))
  }

  const handleSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault()
    handleLogin()
  }

  const handleLogin = () => {
    AuthService.login(inputs.username, inputs.password).then(
      (data) => {
        if (data.roles.includes('ROLE_USER')) {
          navigate('/users', { replace: true })
        }
      },
      (error) => {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString()

        setInputs({
          username: '',
          email: '',
          password: '',
          authError: true,
          message: resMessage,
        })
        setSnackState(true)
        console.log(
          'login error. Message:' +
            inputs.message +
            ' auth error: ' +
            inputs.authError
        )

        // window.location.reload()
      }
    )
  }

  const navigate = useNavigate()

  return (
    <Container maxWidth="xs">
      <Paper elevation={10} sx={{ marginTop: 8, padding: 2 }}>
        <Typography variant="h2" padding={3} textAlign={'center'}>
          Login
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <TextField
            onChange={handleChange}
            name="username"
            value={inputs.username}
            margin="normal"
            type={'text'}
            variant="outlined"
            fullWidth
            required
            autoFocus
            placeholder="Username"
          ></TextField>
          <TextField
            onChange={handleChange}
            name="password"
            value={inputs.password}
            margin="normal"
            type={'password'}
            variant="outlined"
            fullWidth
            required
            autoFocus
            placeholder="Password"
          ></TextField>
          <Button
            endIcon={<LoginIcon />}
            type="submit"
            variant="contained"
            fullWidth
            sx={{ marginTop: 3, borderRadius: 3 }}
            style={{ backgroundColor: 'black', color: 'white' }}
          >
            Login
          </Button>
        </Box>
      </Paper>
      <Snackbar
        open={snackState}
        onClose={handleSnackClose}
        message="Sign In error"
        key={Math.random()}
        autoHideDuration={3000}
      />
    </Container>
  )
}

export default Auth
