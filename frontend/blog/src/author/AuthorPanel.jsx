// AuthorPanel.jsx
import React from 'react';
import {Routes, Route, Link, useNavigate} from 'react-router-dom';
import Feed from "../Feed";
import AuthorManagePosts from "./AuthorManagePosts";
import "../Dashboard.css";


const AuthorPanel = () => {
    const navigate = useNavigate();
    const logout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="dashboard">
            {/* Sidebar */}
            <nav className="sidenav">
                <h3>Author Menu</h3>
                <ul style={{listStyle: 'none', padding: 0}}>
                    <li><Link to="/author/posts">Manage Posts</Link></li>
                    <li><Link to="/author/feed">Feed</Link></li>
                </ul>

                <button className="logout" onClick={logout}>Logout</button>
            </nav>

            {/* Content */}
            <div className="display">
                <Routes>
                    <Route path="posts" element={<AuthorManagePosts/>}/>
                    <Route path="feed" element={<Feed/>}></Route>
                </Routes>
            </div>
        </div>
    );
};

export default AuthorPanel;
