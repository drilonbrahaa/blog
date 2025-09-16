import React, {useEffect, useState} from "react";
import api from "../api";
import "../styles/Crud.css";

// Component for authors to manage their own comments (view, edit, delete)
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
            <table border="1" className="crud-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Content</th>
                    <th>Post</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {comments.length > 0 ? (
                    comments.map((comment) => (
                        <tr key={comment.id}>
                            <td>{comment.id}</td>
                            <td>{editingComments[comment.id] !== undefined ? (
                                <input
                                    type="text"
                                    value={editingComments[comment.id]}
                                    onChange={(e) =>
                                        setEditingComments({
                                            ...editingComments,
                                            [comment.id]: e.target.value,
                                        })
                                    }
                                />) : comment.content}</td>
                            <td>{comment.postId || 'Unknown'}</td>
                            <td>
                                {editingComments[comment.id] === undefined ? (
                                    <div className="comment-actions">
                                        <button
                                            className="green-button"
                                            onClick={() => handleEdit(comment.id, comment.content)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="red-button"
                                            onClick={() => handleDelete(comment.id)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                ) : (<>
                                    <button
                                        className="green-button"
                                        onClick={() => handleUpdate(comment.id)}
                                    >
                                        Update
                                    </button>
                                    <button className="red-button" type="button" onClick={() => setEditingComments({})}>Cancel</button>
                                </>)}
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
}
