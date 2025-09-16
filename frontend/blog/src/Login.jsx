import React, {useState} from 'react';
import api from './api';
import {useNavigate} from 'react-router-dom';
import './styles/Auth.css';

// Login component for user authentication and role-based redirection
const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const response = await api.post('/auth/login', {username, password});
            const {token, role} = response.data;

            localStorage.setItem('token', token);
            localStorage.setItem('role', role);

            switch (role) {
                case 'ADMIN':
                    navigate('/admin/feed');
                    break;
                case 'AUTHOR':
                    navigate('/author/feed');
                    break;
                case 'READER':
                    navigate('/reader/feed');
                    break;
                default:
                    navigate('/login');
            }
        } catch {
            setError('Invalid credentials.');
        }
    };

    return (
        <div className="auth-container">
            <h2>Login</h2>
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Username</label>
                    <input value={username} onChange={e => setUsername(e.target.value)} required autoFocus/>
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} required/>
                </div>
                <button type="submit" className="btn">Login</button>
            </form>
            <p style={{marginTop: 10}}>
                Don't have an account? <a href="/register">Register here</a>
            </p>
        </div>
    );
};

export default Login;
