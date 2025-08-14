import React, {useState} from 'react';
import api from './api';
import {useNavigate} from 'react-router-dom';
import './Auth.css';

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        try {
            await api.post('/auth/register', {username, password});
            setSuccess('Registration successful. Redirecting to login...');
            setTimeout(() => navigate('/login'), 2000);
        } catch {
            setError('Registration failed. Username may already exist.');
        }
    };

    return (
        <div className="auth-container">
            <h2>Register</h2>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Username</label>
                    <input value={username} onChange={e => setUsername(e.target.value)} required autoFocus/>
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} required/>
                </div>
                <button type="submit" className="btn">Register</button>
            </form>
            <p style={{marginTop: 10}}>
                Already have an account? <a href="/login">Login here</a>
            </p>
        </div>
    );
};

export default Register;
