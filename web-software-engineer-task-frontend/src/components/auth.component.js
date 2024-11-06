import React, { useState } from "react";
import { Box, TextField, Button, Typography } from "@mui/material";
import LoginIcon from '@mui/icons-material/Login';
import HowToRegIcon from '@mui/icons-material/HowToReg';

import { useNavigate } from "react-router-dom";

import AuthService from "../services/auth.service";

const Auth = () => {
    const[isSignup, setIsSignup] = useState(false);
    console.log(isSignup);

    const[inputs, setInputs] = useState({
        username: "",
        email: "",
        password: "",
        loading: false,
        message: ""
    });

    const handleChange = (e) => {
        setInputs((prevState) => (
            {
                ...prevState, 
                [e.target.name] : e.target.value
            }
        )
        )
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(inputs);
        isSignup ? handleLogin() : handleLogin();
    };

    const handleLogin = () => {
        
        AuthService.login(inputs.username, inputs.password).then(
            () => {
                console.log(inputs);
                navigate("/users", { replace: true });
                // window.location.reload();
            },
            (error) => {
                console.log(inputs);
                const resMessage =
                    (error.response &&
                        error.response.data &&
                    error.response.data.message) ||
                    error.message ||
                    error.toString();
    
                setInputs({
                    loading: false,
                    message: resMessage
                });
            }
          );
    }

    const resetState = () => {
        setIsSignup(!isSignup);
        setInputs({username:"", password:"", email:""});
    }

    const navigate = useNavigate();

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <Box display={"flex"} 
                    flexDirection={"column"} 
                    maxWidth={400} 
                    alignItems={"center"} 
                    justifyContent={"center"}
                    margin={"auth"}
                    marginTop={5}
                    padding={3}
                    borderRadius={5}
                    boxShadow={"5px 5px 10px #ccc"}
                    sx={ 
                        {":hover": {
                            boxShadow:'10px 10px 20px #ccc'
                            }
                        }
                    }>
                    <Typography 
                        variant="h2" 
                        padding={3} 
                        textAlign={"center"}>
                            {isSignup ? "Signup" : "Login"}
                    </Typography>

                    <TextField 
                        onChange={handleChange}
                        name="username"
                        value={inputs.username}
                        margin="normal" 
                        type={"text"} 
                        variant="outlined" 
                        placeholder="Username">
                    </TextField>
                    {isSignup && (
                        <TextField 
                            onChange={handleChange}
                            name="email"
                            value={inputs.email}
                            margin="normal" 
                            type={"email"} 
                            variant="outlined" 
                            placeholder="Email"
                        />
                    )}
                    <TextField 
                        onChange={handleChange}
                        name="password"
                        value={inputs.password}
                        margin="normal" 
                        type={"password"} 
                        variant="outlined" 
                        placeholder="Password">
                    </TextField>
                    <Button endIcon={isSignup ? <HowToRegIcon /> : <LoginIcon />}
                        type="submit"
                        sx={{marginTop:3, borderRadius: 3}} 
                        variant="contained" 
                        color="warning">
                        {isSignup ? "Signup" : "Login"}
                    </Button>

                    <Button 
                        endIcon={isSignup ? <LoginIcon /> : <HowToRegIcon />}
                        onClick={resetState}
                        sx={{marginTop:3, borderRadius: 3}} >
                            Change {isSignup ? "Login" : "Signup"}
                    </Button>
                </Box>
            </form>
        </div>
    );
};

export default Auth;