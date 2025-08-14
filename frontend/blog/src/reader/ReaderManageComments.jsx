import React, {useEffect, useState} from "react";
import api from "../api";

export default function ManageComments() {
    const [comments, setComments] = useState([]);
    const [editingComments, setEditingComments] = useState({});
    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchMyComments();
    }, []);

    const fetchMyComments = () => {
        api
            .get(`/comments/author`, {
                headers: {Authorization: `Bearer ${token}`},
            })
            .then((res) => setComments(res.data))
            .catch((err) => console.error(err));
    };

    const handleEdit = (id, content) => {
        setEditingComments({...editingComments, [id]: content});
    };

    const handleUpdate = (id) => {
        const content = editingComments[id];
        if (!content?.trim()) return;

        api
            .put(
                `/comments/${id}`,
                {content},
                {headers: {Authorization: `Bearer ${token}`}}
            )
            .then(() => {
                const updated = {...editingComments};
                delete updated[id];
                setEditingComments(updated);
                fetchMyComments();
            })
            .catch((err) => console.error(err));
    };

    const handleDelete = (id) => {
        if (!window.confirm("Delete this comment?")) return;

        api
            .delete(`/comments/${id}`, {
                headers: {Authorization: `Bearer ${token}`},
            })
            .then(fetchMyComments)
            .catch((err) => console.error(err));
    };

    return (
        <div className="manage-comments">
            <h2>My Comments</h2>
            {comments.length === 0 ? (
                <p>No comments found.</p>
            ) : (
                comments.map((c) => (
                    <div key={c.id} className="comment-card">
                        <div className="comment-post-title">
                            On post: <strong>{c.postId}</strong>
                        </div>
                        {editingComments[c.id] !== undefined ? (
                            <div className="edit-comment">
                                <input
                                    type="text"
                                    value={editingComments[c.id]}
                                    onChange={(e) =>
                                        setEditingComments({
                                            ...editingComments,
                                            [c.id]: e.target.value,
                                        })
                                    }
                                />
                                <button
                                    className="btn-update"
                                    onClick={() => handleUpdate(c.id)}
                                >
                                    Update
                                </button>
                                <button type="button" onClick={() => setEditingComments({})}>Cancel</button>
                            </div>
                        ) : (
                            <p>{c.content}</p>
                        )}
                        {editingComments[c.id] === undefined && (
                            <div className="comment-actions">
                                <button
                                    className="btn-edit"
                                    onClick={() => handleEdit(c.id, c.content)}
                                >
                                    Edit
                                </button>
                                <button
                                    className="btn-delete"
                                    onClick={() => handleDelete(c.id)}
                                >
                                    Delete
                                </button>
                            </div>
                        )}
                    </div>
                ))
            )}
        </div>
    );
}
