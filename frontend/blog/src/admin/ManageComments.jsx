import React, {useState, useEffect} from 'react';
import api from '../api';

const ManageComments = () => {
    const [comments, setComments] = useState([]);
    const [form, setForm] = useState({content: ''});
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState('');

    const fetchComments = async () => {
        try {
            const res = await api.get('/comments');
            setComments(res.data);
        } catch {
            setError('Failed to load comments.');
        }
    };

    useEffect(() => {
        fetchComments();
    }, []);

    const handleEdit = (comment) => {
        setForm({content: comment.content});
        setEditingId(comment.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this comment?')) return;
        try {
            await api.delete(`/comments/${id}`);
            fetchComments();
        } catch {
            setError('Failed to delete comment.');
        }
    };

    return (
        <div style={{padding: 20}}>
            <h2>Manage Comments</h2>
            {error && <p style={{color: 'red'}}>{error}</p>}

            <table border="1" cellPadding="8" style={{width: '100%'}}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Content</th>
                    <th>Post</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {comments.length > 0 ? (
                    comments.map((comment) => (
                        <tr key={comment.id}>
                            <td>{comment.id}</td>
                            <td>{comment.content}</td>
                            <td>{comment.postId || 'Unknown'}</td>
                            <td>{comment.authorUsername || 'Unknown'}</td>
                            <td>
                                <button onClick={() => handleDelete(comment.id)} style={{marginLeft: 8}}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="5">No comments found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default ManageComments;
