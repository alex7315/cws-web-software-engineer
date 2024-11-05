import React, { Component } from "react";
import { Box, TextField, Button, Typography } from "@mui/material";
import LoginIcon from '@mui/icons-material/Login';
import HowToRegIcon from '@mui/icons-material/HowToReg';

import AuthService from "../services/auth.service";

class Auth extends Component {
    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.resetState = this.resetState.bind(this);

        this.state = {
            name: "",
            email: "",
            password: "",
            loading: false,
            message: "",
            isSignup: false
        };
    }

    

    handleChange(e) {
        this.setState({[e.target.name] : e.target.value});
    }

    handleLogin(e) {
        console.log(this.state);
        AuthService.login(this.state.name, this.state.password).then(
            () => {
              console.log(this.state);
            //   window.location.reload();
            },
            error => {
              const resMessage =
                (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
                error.message ||
                error.toString();
    
              this.setState({
                loading: false,
                message: resMessage
              });
            }
          );
    }

    handleSubmit(e) {
        e.preventDefault();
        console.log(this.state);
        this.state.isSignup ? this.handleLogin() : this.handleLogin();
    }

    resetState() {
        console.log(this.state);
        this.setState({name:"", password:"", email:"", isSignup:!this.state.isSignup});
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
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
                                {this.state.isSignup ? "Signup" : "Login"}
                        </Typography>

                        <TextField 
                            onChange={this.handleChange}
                            name="name"
                            value={this.state.name}
                            margin="normal" 
                            type={"text"} 
                            variant="outlined" 
                            placeholder="Username">
                        </TextField>
                        {this.state.isSignup && (
                            <TextField 
                                onChange={this.handleChange}
                                name="email"
                                value={this.state.email}
                                margin="normal" 
                                type={"email"} 
                                variant="outlined" 
                                placeholder="Email"
                            />
                        )}
                        <TextField 
                            onChange={this.handleChange}
                            name="password"
                            value={this.state.password}
                            margin="normal" 
                            type={"password"} 
                            variant="outlined" 
                            placeholder="Password">
                        </TextField>
                        <Button endIcon={this.state.isSignup ? <HowToRegIcon /> : <LoginIcon />}
                            type="submit"
                            sx={{marginTop:3, borderRadius: 3}} 
                            variant="contained" >
                            {this.state.isSignup ? "Signup" : "Login"}
                        </Button>

                        <Button 
                            endIcon= {this.state.isSignup ? <LoginIcon /> : <HowToRegIcon />}
                            onClick={this.resetState}
                            sx={{marginTop:3, borderRadius: 3}} >
                                Change {this.state.isSignup ? "Login" : "Signup"}
                        </Button>
                    </Box>
                </form>
            </div>
        );
    }
}

export default Auth;