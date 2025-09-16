import React, {useState, useEffect} from 'react';
import api from '../api';
import "../styles/Crud.css";

// Component for managing posts (create, edit, delete) and viewing/deleting comments
const ManagePosts = () => {
    const [posts, setPosts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [tagsList, setTagsList] = useState([]);
    const [comments, setComments] = useState({});
    const [showCommentsFor, setShowCommentsFor] = useState(null);

    const [form, setForm] = useState({
        title: '',
        content: '',
        categoryName: '',
        tagNames: [],
    });
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState('');

    const fetchPosts = async () => {
        try {
            const res = await api.get('/posts');
            setPosts(res.data);
        } catch {
            setError('Failed to load posts.');
        }
    };

    const fetchCategories = async () => {
        try {
            const res = await api.get('/categories');
            setCategories(res.data);
        } catch {
            setError('Failed to load categories.');
        }
    };

    const fetchTags = async () => {
        try {
            const res = await api.get('/tags');
            setTagsList(res.data);
        } catch {
            setError('Failed to load tags.');
        }
    };

    const fetchCommentsForPost = async (postId) => {
        try {
            const res = await api.get(`/comments/post/${postId}`);
            setComments(prev => ({...prev, [postId]: res.data}));
        } catch {
            setError('Failed to load comments.');
        }
    };

    const handleDeleteComment = async (postId, commentId) => {
        if (!window.confirm('Delete this comment?')) return;
        try {
            await api.delete(`/comments/${commentId}`);
            setComments(prev => ({
                ...prev,
                [postId]: prev[postId].filter(c => c.id !== commentId),
            }));
        } catch {
            setError('Failed to delete comment.');
        }
    };

    useEffect(() => {
        fetchPosts();
        fetchCategories();
        fetchTags();
    }, []);

    const handleTagToggle = (tag) => {
        if (form.tagNames.includes(tag)) {
            setForm({
                ...form,
                tagNames: form.tagNames.filter((t) => t !== tag),
            });
        } else {
            setForm({
                ...form,
                tagNames: [...form.tagNames, tag],
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!form.title.trim() || !form.content.trim() || !form.categoryName) {
            setError('Title, content and category are required.');
            return;
        }

        try {
            const payload = {
                title: form.title,
                content: form.content,
                categoryName: form.categoryName,
                tagNames: form.tagNames,
            };
            if (editingId) {
                await api.put(`/posts/${editingId}`, payload);
            } else {
                await api.post(`/posts/author`, payload);
            }
            setForm({title: '', content: '', categoryName: '', tagNames: []});
            setEditingId(null);
            fetchPosts();
        } catch {
            setError('Failed to save post.');
        }
    };

    const handleEdit = (post) => {
        setForm({
            title: post.title,
            content: post.content,
            categoryName: post.categoryName || '',
            tagNames: post.tagNames || [],
        });
        setEditingId(post.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this post?')) return;
        try {
            await api.delete(`/posts/${id}`);
            fetchPosts();
        } catch {
            setError('Failed to delete post.');
        }
    };

    return (
        <div style={{padding: 20}}>
            <h2>Manage Posts</h2>
            {error && <p style={{color: 'red'}}>{error}</p>}

            <form className="crud-form" onSubmit={handleSubmit}>
                <div>
                    <input
                        type="text"
                        placeholder="Title"
                        value={form.title}
                        onChange={(e) => setForm({...form, title: e.target.value})}
                        required
                    />
                </div>

                <div style={{marginTop: 10}}>
                    <textarea
                        placeholder="Content"
                        rows={5}
                        value={form.content}
                        onChange={(e) => setForm({...form, content: e.target.value})}
                        required
                    />
                </div>

                <div style={{marginTop: 10}}>
                    <select
                        value={form.categoryName}
                        onChange={(e) => setForm({...form, categoryName: e.target.value})}
                        required
                    >
                        <option value="">-- Select category --</option>
                        {categories.map((cat) => (
                            <option key={cat.id || cat} value={cat.name || cat}>
                                {cat.name || cat}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="tags">
                    {tagsList.map((tag) => (
                        <label key={tag.id || tag} style={{marginRight: 10}}>
                            <input
                                type="checkbox"
                                checked={form.tagNames.includes(tag.name || tag)}
                                onChange={() => handleTagToggle(tag.name || tag)}
                            />{' '}
                            {tag.name || tag}
                        </label>
                    ))}
                </div>

                <button className="green-button" type="submit">
                    {editingId ? 'Update Post' : 'Create Post'}
                </button>
                {editingId && (
                    <button
                        className="red-button"
                        type="button"
                        onClick={() => {
                            setForm({title: '', content: '', categoryName: '', tagNames: []});
                            setEditingId(null);
                            setError('');
                        }}
                    >
                        Cancel
                    </button>
                )}
            </form>

            <table border="1" className="crud-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Tags</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {posts.length > 0 ? (
                    posts.map((post) => (
                        <React.Fragment key={post.id}>
                            <tr>
                                <td>{post.id}</td>
                                <td>{post.title}</td>
                                <td>{post.categoryName || '-'}</td>
                                <td>{(post.tagNames || []).join(', ')}</td>
                                <td>{post.authorUsername || 'Unknown'}</td>
                                <td>
                                    <button className="green-button" onClick={() => handleEdit(post)}>Edit</button>
                                    <button className="red-button" onClick={() => handleDelete(post.id)}>
                                        Delete
                                    </button>
                                    <button className="blue-button" onClick={() => {
                                        setShowCommentsFor(showCommentsFor === post.id ? null : post.id);
                                        if (showCommentsFor !== post.id) {
                                            fetchCommentsForPost(post.id)
                                        }

                                    }}>
                                        {showCommentsFor === post.id ? "Hide Comments" : "View Comments"}
                                    </button>
                                </td>
                            </tr>

                            {showCommentsFor === post.id && comments[post.id] && (
                                <tr>
                                    <td colSpan="6">
                                        <table border="1" cellPadding="6" style={{width: '100%', marginTop: 5}}>
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Author</th>
                                                <th>Content</th>
                                                <th>Actions</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {comments[post.id].map(comment => (
                                                <tr key={comment.id}>
                                                    <td>{comment.id}</td>
                                                    <td>{comment.authorUsername}</td>
                                                    <td>{comment.content}</td>
                                                    <td>
                                                        <button className="red-button"
                                                                onClick={() => handleDeleteComment(post.id, comment.id)}
                                                                style={{marginLeft: 5}}>
                                                            Delete
                                                        </button>
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))
                ) : (
                    <tr>
                        <td colSpan="6">No posts found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default ManagePosts;



