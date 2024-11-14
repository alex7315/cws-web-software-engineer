import React, { useState } from "react";
import { Box, TextField, Button, Typography } from "@mui/material";
import LoginIcon from '@mui/icons-material/Login';

import { useNavigate } from "react-router-dom";

import AuthService from "../services/auth.service";

const Auth = () => {

    interface IAuthInput {
        username: string; 
        email: string; 
        password: string; 
        loading: boolean; 
        message: string;
    }

    const[inputs, setInputs] = useState<IAuthInput>({
        username: "",
        email: "",
        password: "",
        loading: false,
        message: ""
    });

    const handleChange = (e: React.SyntheticEvent) => {
        let target = e.target as HTMLInputElement;
        setInputs((prevState) => (
            {
                ...prevState, 
                [target.name] : target.value
            }
        )
        )
    };

    const handleSubmit = (e: React.SyntheticEvent) => {
        e.preventDefault();
        handleLogin();
    };

    const handleLogin = () => {
        
        AuthService.login(inputs.username, inputs.password).then(
            (data) => {
                if(data.roles.includes("ROLE_USER")) {
                    navigate("/users", { replace: true });
                    window.location.reload();
                }
            },
            (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                    error.response.data.message) ||
                    error.message ||
                    error.toString();
    
                setInputs({
                    username: "",
                    email: "",
                    password: "",
                    loading: false,
                    message: resMessage
                });
                window.location.reload();
            }
          );
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
                            Login
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

                    <TextField 
                        onChange={handleChange}
                        name="password"
                        value={inputs.password}
                        margin="normal" 
                        type={"password"} 
                        variant="outlined" 
                        placeholder="Password">
                    </TextField>

                    <Button endIcon={<LoginIcon />}
                        type="submit"
                        sx={{marginTop:3, borderRadius: 3}} 
                        variant="contained" 
                        style={{ backgroundColor: 'black', color: 'white' }}>
                        Login
                    </Button>
                </Box>
            </form>
        </div>
    );
};

export default Auth;