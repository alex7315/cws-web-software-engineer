import axios from 'axios';
import authHeader from './auth-header';

const USERS_API_BASE_URL = "http://localhost:8080"
class UsersService {
    getUserBoard() {
        return axios.get(USERS_API_BASE_URL + '/api/users', { headers: authHeader() });
    }
}

export default new UsersService();