import { Navigate } from "react-router-dom";

// ProtectedRoute component to restrict access based on authentication and user roles
export default function ProtectedRoute({ children, allowedRoles }) {
    const token = localStorage.getItem("token");
    if (!token) return <Navigate to="/login" />;
    const role = localStorage.getItem("role");

    if (!allowedRoles.includes(role)) return <Navigate to="/login" />;

    return children;
}

