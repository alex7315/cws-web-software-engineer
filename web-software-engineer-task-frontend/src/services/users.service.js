import axios from "axios";
import authHeader from './auth-header';

const API_URL = process.env.REACT_APP_API_USERS_URL;

const users = (page, size, sort) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.accessToken) {
        axios.get(API_URL, {
            params: {
                page: page, 
                size: size, 
                sort: sort
            },
            headers: authHeader()
        })
        .then(response => {
            if(response.data) {
                console.log(response.data);
            }

            return response.data;
        });
    }
}