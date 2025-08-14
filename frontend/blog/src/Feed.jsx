import React, {useState, useEffect} from "react";
import api from "./api";
import "./Feed.css";

export default function Feed() {
    const [posts, setPosts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [tags, setTags] = useState([]);
    const [filters, setFilters] = useState({
        search: "",
        categories: [],
        tags: []
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
                setPosts(posts.map(p =>
                    p.id === postId ? {...p, comments: [...p.comments, res.data]} : p
                ));
                setCommentInputs({...commentInputs, [postId]: ""});
            })
            .catch(err => console.error(err));
    };

    const handleEditComment = (commentId, postId) => {
        const content = commentInputs[postId]?.trim();
        if (!content) return;

        api.put(`/comments/${commentId}`, {content}, {headers: {Authorization: `Bearer ${token}`}})
            .then(res => {
                setPosts(posts.map(p =>
                    p.id === postId
                        ? {
                            ...p,
                            comments: p.comments.map(c => c.id === commentId ? res.data : c)
                        }
                        : p
                ));
                setEditingCommentId(null);
                setCommentInputs({...commentInputs, [postId]: ""});
            })
            .catch(err => console.error(err));
    };

    const handleDeleteComment = (commentId, postId) => {
        api.delete(`/comments/${commentId}`, {headers: {Authorization: `Bearer ${token}`}})
            .then(() => {
                setPosts(posts.map(p =>
                    p.id === postId
                        ? {...p, comments: p.comments.filter(c => c.id !== commentId)}
                        : p
                ));
            })
            .catch(err => console.error(err));
    };

    const toggleTagFilter = (tagName) => {
        setFilters(prev => {
            const tags = prev.tags.includes(tagName)
                ? prev.tags.filter(t => t !== tagName)
                : [...prev.tags, tagName];
            return {...prev, tags};
        });
    };

    const filteredPosts = posts.filter((post) => {
        const matchesSearch = post.title.toLowerCase().includes(filters.search.toLowerCase());
        const matchesCategories =
            filters.categories.length === 0 ||
            filters.categories.some(cat => post.categoryName === cat);
        const matchesTags =
            filters.tags.length === 0 ||
            post.tagNames.some(tag => filters.tags.includes(tag));

        return matchesSearch && matchesCategories && matchesTags;
    });

    return (
        <div className="feed-container" style={{padding: "20px"}}>
            <h2>Feed</h2>

            {/* Search */}
            <input
                type="text"
                placeholder="Search posts..."
                value={filters.search}
                onChange={(e) => setFilters({...filters, search: e.target.value})}
                style={{marginRight: "10px"}}
            />

            {/* Categories */}
            <select
                multiple
                value={filters.categories}
                onChange={(e) => {
                    const selected = Array.from(e.target.selectedOptions, opt => opt.value);
                    setFilters({...filters, categories: selected});
                }}
                style={{marginRight: "10px"}}
            >
                {categories.map(cat => (
                    <option key={cat.id} value={cat.name}>{cat.name}</option>
                ))}
            </select>

            {/* Tags as Checkboxes */}
            <div style={{display: "inline-block"}}>
                {tags.map(tag => (
                    <label key={tag.id} style={{marginRight: "10px"}}>
                        <input
                            type="checkbox"
                            checked={filters.tags.includes(tag.name)}
                            onChange={() => toggleTagFilter(tag.name)}
                        />
                        {tag.name}
                    </label>
                ))}
            </div>

            {/* Posts */}
            <div className="feed" style={{marginTop: "20px"}}>
                {filteredPosts.map((post) => (
                    <div key={post.id} className="post-card"
                         style={{border: "1px solid #ccc", padding: "15px", marginBottom: "15px", borderRadius: "8px"}}>
                        <h3>{post.title}</h3>
                        <p>{post.content}</p>
                        <small>By {post.authorUsername}</small>

                        {/* Comments */}
                        <div className="comments" style={{marginTop: "10px", paddingLeft: "15px"}}>
                            {post.comments.map((c) => (
                                <div key={c.id} className="comment" style={{marginBottom: "5px"}}>
                                    <strong>{c.authorUsername}: </strong>
                                    {editingCommentId === c.id ? (
                                        <>
                                            <input
                                                type="text"
                                                value={commentInputs[post.id] || ""}
                                                onChange={(e) => setCommentInputs({
                                                    ...commentInputs,
                                                    [post.id]: e.target.value
                                                })}
                                            />
                                            <button onClick={() => handleEditComment(c.id, post.id)}>Save</button>
                                            <button onClick={() => setEditingCommentId(null)}>Cancel</button>
                                        </>
                                    ) : (
                                        <>
                                            {c.content}
                                            {c.authorUsername === username && (
                                                <>
                                                    <button onClick={() => {
                                                        setEditingCommentId(c.id);
                                                        setCommentInputs({...commentInputs, [post.id]: c.content});
                                                    }}>Edit
                                                    </button>
                                                    <button onClick={() => handleDeleteComment(c.id, post.id)}>Delete
                                                    </button>
                                                </>
                                            )}
                                        </>
                                    )}
                                </div>
                            ))}
                        </div>

                        {/* Add Comment */}
                        <div style={{marginTop: "10px"}}>
                            <input
                                type="text"
                                placeholder="Add a comment..."
                                onChange={(e) => setCommentInputs({...commentInputs, [post.id]: e.target.value})}
                            />
                            <button onClick={(e) => {handleAddComment(post.id);e.target.previousElementSibling.value="";}}>Post</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
