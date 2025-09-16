import React, {useState, useEffect} from "react";
import api from "./api";
import "./styles/Feed.css";

// Feed component for displaying posts with filtering and comment management
export default function Feed() {
    const [posts, setPosts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [tags, setTags] = useState([]);
    const [filters, setFilters] = useState({
        search: "", category: "", tags: []
    });

    const [commentInputs, setCommentInputs] = useState({});
    const [editingCommentId, setEditingCommentId] = useState(null);

    const token = localStorage.getItem("token");
    const username = JSON.parse(atob(token.split(".")[1])).sub;

    useEffect(() => {
        api.get("/posts", {headers: {Authorization: `Bearer ${token}`}})
            .then(res => setPosts(res.data))
            .catch(err => console.error(err));

        api.get("/categories", {headers: {Authorization: `Bearer ${token}`}})
            .then(res => setCategories(res.data))
            .catch(err => console.error(err));

        api.get("/tags", {headers: {Authorization: `Bearer ${token}`}})
            .then(res => setTags(res.data))
            .catch(err => console.error(err));
    }, [token]);

    const handleAddComment = (postId) => {
        const content = commentInputs[postId]?.trim();
        if (!content) return;

        api.post(`/comments/post/${postId}`, {content}, {headers: {Authorization: `Bearer ${token}`}})
            .then(res => {
                setPosts(posts.map(p => p.id === postId ? {...p, comments: [...p.comments, res.data]} : p));
                setCommentInputs({...commentInputs, [postId]: ""});
            })
            .catch(err => console.error(err));
    };

    const handleEditComment = (commentId, postId) => {
        const content = commentInputs[postId]?.trim();
        if (!content) return;

        api.put(`/comments/${commentId}`, {content}, {headers: {Authorization: `Bearer ${token}`}})
            .then(res => {
                setPosts(posts.map(p => p.id === postId ? {
                    ...p, comments: p.comments.map(c => c.id === commentId ? res.data : c)
                } : p));
                setEditingCommentId(null);
                setCommentInputs({...commentInputs, [postId]: ""});
            })
            .catch(err => console.error(err));
    };

    const handleDeleteComment = (commentId, postId) => {
        api.delete(`/comments/${commentId}`, {headers: {Authorization: `Bearer ${token}`}})
            .then(() => {
                setPosts(posts.map(p => p.id === postId ? {
                    ...p,
                    comments: p.comments.filter(c => c.id !== commentId)
                } : p));
            })
            .catch(err => console.error(err));
    };

    const toggleTagFilter = (tagName) => {
        setFilters(prev => {
            const tags = prev.tags.includes(tagName) ? prev.tags.filter(t => t !== tagName) : [...prev.tags, tagName];
            return {...prev, tags};
        });
    };

    const filteredPosts = posts.filter((post) => {
        const matchesSearch = post.title.toLowerCase().includes(filters.search.toLowerCase());
        const matchesCategory = filters.category === "" || filters.category === post.categoryName;
        const matchesTags = filters.tags.length === 0 || filters.tags.every(tag => post.tagNames.includes(tag));

        return matchesSearch && matchesCategory && matchesTags;
    });

    return (<div className="feed-container">
        <div className="feed-navbar">
            <h2>FEED</h2>
            <div className="filter">
                <input
                    className="filter-search"
                    type="text"
                    placeholder="Search posts..."
                    value={filters.search}
                    onChange={(e) => setFilters({...filters, search: e.target.value})}
                    style={{marginRight: "10px"}}
                />

                {/* Categories */}

                <select
                    value={filters.category}
                    onChange={(e) => {
                        const selected = e.target.value;
                        setFilters({...filters, category: selected});
                    }}

                >
                    <option value="">None</option>
                    {categories.map(cat => (<option key={cat.id} value={cat.name}>{cat.name}</option>))}
                </select>

                {/* Tags as Checkboxes */}
                <div className="tags">
                    {tags.map(tag => (<label key={tag.id}>
                        <input
                            type="checkbox"
                            checked={filters.tags.includes(tag.name)}
                            onChange={() => toggleTagFilter(tag.name)}
                        />
                        {tag.name}
                    </label>))}
                </div>
            </div>
        </div>

        {/* Posts */}
        <div className="posts">
            {filteredPosts.map((post) => (<div key={post.id} className="post-card"
            >
                <div className={"title"}>
                    <h3>{post.title}</h3>
                    <small>By {post.authorUsername}</small>
                </div>
                <p className="content">{post.content}</p>


                {/* Comments */}
                <div className="comments">
                    {post.comments.map((c) => (<div key={c.id} className="comment">
                        {editingCommentId === c.id ? (<div className="edit-comment">
                            <input
                                type="text"
                                value={commentInputs[post.id] || ""}
                                onChange={(e) => setCommentInputs({
                                    ...commentInputs, [post.id]: e.target.value
                                })}
                            />
                            <ul>
                                <button className="green-button" onClick={() => handleEditComment(c.id, post.id)}>Save
                                </button>
                                <button className="red-button" onClick={() => setEditingCommentId(null)}>Cancel</button>
                            </ul>
                        </div>) : (<div>
                            <p>
                                <strong>{c.authorUsername}: </strong>
                                {c.content}
                            </p>
                            {c.authorUsername === username && (<ul>
                                <button className="green-button" onClick={() => {
                                    setEditingCommentId(c.id);
                                    setCommentInputs({...commentInputs, [post.id]: c.content});
                                }}>Edit
                                </button>
                                <button className="red-button"
                                        onClick={() => handleDeleteComment(c.id, post.id)}>Delete
                                </button>
                            </ul>)}
                        </div>)}
                    </div>))}
                    <div className="add-comment">
                        <div>
                            <input
                                type="text"
                                placeholder="Add a comment..."
                                onChange={(e) => setCommentInputs({...commentInputs, [post.id]: e.target.value})}
                            />
                            <button className="green-button" onClick={(e) => {
                                handleAddComment(post.id);
                                e.target.previousElementSibling.value = "";
                            }}>Post
                            </button>
                        </div>
                    </div>

                </div>

                {/* Add Comment */}

            </div>))}
        </div>
    </div>);
}
