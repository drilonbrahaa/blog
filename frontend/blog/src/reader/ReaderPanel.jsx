// ReaderPanel.jsx
import React from 'react';
import {Routes, Route, Link, useNavigate} from 'react-router-dom';
import Feed from "../Feed";
import ManageComments from "./ReaderManageComments";
import "../Dashboard.css";

const ReaderPanel = () => {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="dashboard">
            {/* Sidebar */}
            <nav className="sidenav">
                <h3>Reader Menu</h3>
                <ul style={{listStyle: 'none', padding: 0}}>
                    <li><Link to="/reader/feed">Feed</Link></li>
                    <li><Link to="/reader/comments">Manage Comments</Link></li>
                </ul>

                <button className="logout" onClick={logout}>Logout</button>
            </nav>

            {/* Content */}
            <div className="display">
                <Routes>
                    <Route path="comments" element={<ManageComments/>}/>
                    <Route path="feed" element={<Feed/>}></Route>
                </Routes>
            </div>
        </div>
    );
};

export default ReaderPanel;
