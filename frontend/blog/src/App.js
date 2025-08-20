import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login";
import Register from "./Register";
import Feed from "./Feed";
import AdminPanel from "./admin/AdminPanel";
import ReaderPanel from "./reader/ReaderPanel";
import ProtectedRoute from "./ProtectedRoute";
import { Navigate } from "react-router-dom";
import AuthorPanel from "./author/AuthorPanel";

function App() {
    return (
        <Router>
            <Routes>
                {/* Public routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Admin routes */}
                <Route
                    path="/admin/*"
                    element={
                        <ProtectedRoute allowedRoles={["ADMIN"]}>
                            <AdminPanel />
                        </ProtectedRoute>
                    }
                />

                {/* Author routes */}
                <Route
                    path="/author/*"
                    element={
                        <ProtectedRoute allowedRoles={["AUTHOR"]}>
                            <AuthorPanel />
                        </ProtectedRoute>
                    }
                />

                {/* Reader routes */}
                <Route
                    path="/reader/*"
                    element={
                        <ProtectedRoute allowedRoles={["READER"]}>
                            <ReaderPanel />
                        </ProtectedRoute>
                    }
                />

                {/* Default route after login: Feed */}
                <Route
                    path="/feed"
                    element={
                        <ProtectedRoute allowedRoles={["ADMIN", "AUTHOR", "READER"]}>
                            <Feed />
                        </ProtectedRoute>
                    }
                />

                {/* Catch all */}
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

export default App;


