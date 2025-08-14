import React, { useEffect, useState } from 'react';
import api from "../api";

const ManageUsers = () => {
    const [users, setUsers] = useState([]);
    const [form, setForm] = useState({ username: '', password: '', roleName: '' });
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState('');

    const fetchUsers = async () => {
        try {
            const res = await api.get('/admin/users');
            setUsers(res.data);
        } catch (err) {
            setError('Failed to load users.');
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await api.put(`/admin/users/${editingId}`, form);
            } else {
                await api.post('/admin/users', form);
            }
            setForm({ username: '', password: '', roleName: '' });
            setEditingId(null);
            await fetchUsers();
        } catch (err) {
            setError('Failed to save user.');
        }
    };

    const handleEdit = (user) => {
        setForm({ username: user.username, password: '', roleName: user.roleName });
        setEditingId(user.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this user?')) return;
        try {
            await api.delete(`/admin/users/${id}`);
            await fetchUsers();
        } catch (err) {
            setError('Failed to delete user.');
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>Manage Users</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {/* User Form */}
            <form onSubmit={handleSubmit} style={{ marginBottom: 20 }}>
                <input
                    type="text"
                    placeholder="Username"
                    value={form.username}
                    onChange={(e) => setForm({ ...form, username: e.target.value })}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={(e) => setForm({ ...form, password: e.target.value })}
                    required={!editingId} // password optional when editing
                />
                <select
                    value={form.roleName}
                    onChange={(e) => setForm({ ...form, roleName: e.target.value })}
                    required
                >
                    <option value="">Select Role</option>
                    <option value="ADMIN">Admin</option>
                    <option value="AUTHOR">Author</option>
                    <option value="READER">Reader</option>
                </select>
                <button type="submit">
                    {editingId ? 'Update' : 'Create'}
                </button>
                {editingId && (
                    <button
                        type="button"
                        onClick={() => {
                            setForm({ username: '', password: '', roleName: '' });
                            setEditingId(null);
                        }}
                    >
                        Cancel
                    </button>
                )}
            </form>

            {/* Users Table */}
            <table border="1" cellPadding="8" style={{ width: '100%' }}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.length > 0 ? (
                    users.map((u) => (
                        <tr key={u.id}>
                            <td>{u.id}</td>
                            <td>{u.username}</td>
                            <td>{u.roleName}</td>
                            <td>
                                <button onClick={() => handleEdit(u)}>Edit</button>
                                <button onClick={() => handleDelete(u.id)}>Delete</button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr><td colSpan="4">No users found.</td></tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default ManageUsers;
