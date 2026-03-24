import axios from 'axios';

const AUTH_API_URL = import.meta.env.VITE_API_URL_AUTH || 'http://localhost:8080/api/auth/';

class AuthService {
    async login(username: string, password: string){
        try {
        const response = await axios.post(AUTH_API_URL + 'login', {
            username,
            password
        });
        if (response.data) {
            localStorage.setItem('user_token', JSON.stringify(response.data));
            localStorage.setItem('username', username);
        }
        } catch(error){
            throw error;
        }
    
    
    }
    async register(username: string, password: string){
        try {
            const response = await axios.post(AUTH_API_URL + 'register', {
                username,
                password
            });
            return response.data;
        } catch(error){
            throw error;
        }
    }
    logout() {
        localStorage.removeItem('user_token');
        localStorage.removeItem('username');
    }

    getToken() {
        return localStorage.getItem('user_token');
    }
}

export default new AuthService();