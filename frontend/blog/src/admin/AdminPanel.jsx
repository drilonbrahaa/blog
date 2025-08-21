// AdminPanel.jsx
import React from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import ManageUsers from "./ManageUsers";
import ManagePosts from "./ManagePosts";
import ManageComments from "./ManageComments";
import Feed from "../Feed";
import "../styles/Dashboard.css";

const AdminPanel = () => {
    const navigate = useNavigate();
    const logout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="dashboard">
            {/* Sidebar */}
            <nav className="sidenav">
                <h3>Admin Menu</h3>
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    <li><Link to="/admin/users">Manage Users</Link></li>
                    <li><Link to="/admin/posts">Manage Posts</Link></li>
                    <li><Link to="/admin/comments">Manage Comments</Link></li>
                    <li><Link to="/admin/feed">Feed</Link></li>
                </ul>

                <button className="logout" onClick={logout}>Logout</button>
            </nav>

            {/* Content */}
            <div className="display">
                <Routes>
                    <Route path="users" element={<ManageUsers />} />
                    <Route path="posts" element={<ManagePosts />} />
                    <Route path="comments" element={<ManageComments />} />
                    <Route path="feed" element={<Feed />}></Route>
                </Routes>
            </div>
        </div>
    );
};

export default AdminPanel;
