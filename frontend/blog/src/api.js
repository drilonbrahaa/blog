import axios from 'axios';

// Create an Axios instance with base URL and interceptors for auth token
const api = axios.create({
    baseURL: 'http://localhost:8080/api', // adjust
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;

