import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import api from "../api";

export default function AuthorManagePosts() {
    const [posts, setPosts] = useState([]);
    const [form, setForm] = useState({title: "", content: "", categoryName: "", tagNames: []});
    const [editingId, setEditingId] = useState(null);
    const [categories, setCategories] = useState([]);
    const [tagsList, setTagsList] = useState([]);
    const [error, setError] = useState("");
    const [comments, setComments] = useState({});
    const [showCommentsFor, setShowCommentsFor] = useState(null);

    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchPosts();
        fetchCategories();
        fetchTags();
    }, []);

    const fetchPosts = () => {
        api.get("/posts/author", {headers: {Authorization: `Bearer ${token}`}})
            .then((res) => setPosts(res.data))
            .catch((err) => console.error(err));
    };

    const fetchCategories = async () => {
        try {
            const res = await api.get("/categories");
            setCategories(res.data);
        } catch {
            setError("Failed to load categories.");
        }
    };

    const fetchTags = async () => {
        try {
            const res = await api.get("/tags");
            setTagsList(res.data);
        } catch {
            setError("Failed to load tags.");
        }
    };

    const fetchComments = async (postId) => {
        try {
            const res = await api.get(`/comments/post/${postId}`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setComments((prev) => ({...prev, [postId]: res.data}));
        } catch {
            setError("Failed to load comments.");
        }
    };

    const deleteComment = async (commentId, postId) => {
        if (!window.confirm("Delete this comment?")) return;
        try {
            await api.delete(`/comments/${commentId}`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            fetchComments(postId);
        } catch {
            setError("Failed to delete comment.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (!form.title.trim() || !form.content.trim() || !form.categoryName) {
            setError("Title, content, and category are required.");
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
                await api.put(`/posts/${editingId}`, payload, {headers: {Authorization: `Bearer ${token}`}});
            } else {
                await api.post(`/posts/author`, payload, {headers: {Authorization: `Bearer ${token}`}});
            }

            setForm({title: "", content: "", categoryName: "", tagNames: []});
            setEditingId(null);
            fetchPosts();
        } catch {
            setError("Failed to save post.");
        }
    };

    const handleEdit = (post) => {
        setForm({
            title: post.title,
            content: post.content,
            categoryName: post.categoryName || "",
            tagNames: post.tagNames || [],
        });
        setEditingId(post.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Delete this post?")) return;
        try {
            await api.delete(`/posts/${id}`, {headers: {Authorization: `Bearer ${token}`}});
            fetchPosts();
        } catch {
            setError("Failed to delete post.");
        }
    };

    const handleTagToggle = (tag) => {
        if (form.tagNames.includes(tag)) {
            setForm({...form, tagNames: form.tagNames.filter((t) => t !== tag)});
        } else {
            setForm({...form, tagNames: [...form.tagNames, tag]});
        }
    };

    return (
        <div style={{display: "flex", height: "100vh"}}>

            {/* Main Content */}
            <div style={{flex: 1, padding: 20}}>
                <h2>Manage Your Posts</h2>
                {error && <p style={{color: "red"}}>{error}</p>}

                {/* Post Form */}
                <form onSubmit={handleSubmit} style={{marginBottom: 20}}>
                    <div>
                        <label>Title</label><br/>
                        <input
                            type="text"
                            value={form.title}
                            onChange={(e) => setForm({...form, title: e.target.value})}
                            required
                        />
                    </div>

                    <div style={{marginTop: 10}}>
                        <label>Content</label><br/>
                        <textarea
                            rows={5}
                            value={form.content}
                            onChange={(e) => setForm({...form, content: e.target.value})}
                            required
                        />
                    </div>

                    <div style={{marginTop: 10}}>
                        <label>Category</label><br/>
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

                    <div style={{marginTop: 10}}>
                        <label>Tags</label><br/>
                        {tagsList.map((tag) => (
                            <label key={tag.id || tag} style={{marginRight: 10}}>
                                <input
                                    type="checkbox"
                                    checked={form.tagNames.includes(tag.name || tag)}
                                    onChange={() => handleTagToggle(tag.name || tag)}
                                />{" "}
                                {tag.name || tag}
                            </label>
                        ))}
                    </div>

                    <button type="submit" style={{marginTop: 10}}>
                        {editingId ? "Update Post" : "Create Post"}
                    </button>
                    {editingId && (
                        <button
                            type="button"
                            onClick={() => {
                                setForm({title: "", content: "", categoryName: "", tagNames: []});
                                setEditingId(null);
                                setError("");
                            }}
                            style={{marginLeft: 10}}
                        >
                            Cancel
                        </button>
                    )}
                </form>

                {/* Posts Table */}
                <table border="1" cellPadding="8" style={{width: "100%"}}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Category</th>
                        <th>Tags</th>
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
                                    <td>{post.categoryName || "-"}</td>
                                    <td>{(post.tagNames || []).join(", ")}</td>
                                    <td>
                                        <button onClick={() => handleEdit(post)}>Edit</button>
                                        <button onClick={() => handleDelete(post.id)} style={{marginLeft: 8}}>
                                            Delete
                                        </button>
                                        <button
                                            onClick={() => {
                                                setShowCommentsFor(showCommentsFor === post.id ? null : post.id);
                                                if (showCommentsFor !== post.id) {
                                                    fetchComments(post.id);
                                                }
                                            }}
                                            style={{marginLeft: 8}}
                                        >
                                            {showCommentsFor === post.id ? "Hide Comments" : "View Comments"}
                                        </button>
                                    </td>
                                </tr>
                                {showCommentsFor === post.id && (
                                    <tr>
                                        <td colSpan="5">
                                            {comments[post.id]?.length > 0 ? (
                                                <ul>
                                                    {comments[post.id].map((c) => (
                                                        <li key={c.id}>
                                                            <strong>{c.authorUsername}</strong> {c.content}{" "}
                                                            <button
                                                                onClick={() => deleteComment(c.id, post.id)}
                                                                style={{marginLeft: 10}}
                                                            >
                                                                Delete
                                                            </button>
                                                        </li>
                                                    ))}
                                                </ul>
                                            ) : (
                                                <p>No comments for this post.</p>
                                            )}
                                        </td>
                                    </tr>
                                )}
                            </React.Fragment>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5">No posts found.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
