// AdminDashboard.jsx
import React from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import ManageUsers from "./ManageUsers";
import ManagePosts from "./ManagePosts";
import ManageComments from "./ManageComments";
import Feed from "../Feed";

const AdminDashboard = () => {
    const navigate = useNavigate();
    const logout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div style={{ display: 'flex' }}>
            {/* Sidebar */}
            <nav style={{ width: 200, padding: 20, background: '#f0f0f0' }}>
                <h3>Admin Menu</h3>
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    <li><Link to="/admin/users">Manage Users</Link></li>
                    <li><Link to="/admin/posts">Manage Posts</Link></li>
                    <li><Link to="/admin/comments">Manage Comments</Link></li>
                    <li><Link to="/admin/feed">Feed</Link></li>
                </ul>

                <button onClick={logout}>Logout</button>
            </nav>

            {/* Content */}
            <div style={{ flex: 1, padding: 20 }}>
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

export default AdminDashboard;
