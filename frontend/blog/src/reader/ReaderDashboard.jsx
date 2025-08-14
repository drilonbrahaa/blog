// ReaderDashboard.jsx
import React from 'react';
import {Routes, Route, Link, useNavigate} from 'react-router-dom';
import Feed from "../Feed";
import ManageComments from "./ReaderManageComments";

const ReaderDashboard = () => {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div style={{display: 'flex'}}>
            {/* Sidebar */}
            <nav style={{width: 200, padding: 20, background: '#f0f0f0'}}>
                <h3>Reader Menu</h3>
                <ul style={{listStyle: 'none', padding: 0}}>
                    <li><Link to="/reader/feed">Feed</Link></li>
                    <li><Link to="/reader/comments">Manage Comments</Link></li>
                </ul>

                <button onClick={logout}>Logout</button>
            </nav>

            {/* Content */}
            <div style={{flex: 1, padding: 20}}>
                <Routes>
                    <Route path="comments" element={<ManageComments/>}/>
                    <Route path="feed" element={<Feed/>}></Route>
                </Routes>
            </div>
        </div>
    );
};

export default ReaderDashboard;
