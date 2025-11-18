import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import Register from "./pages/Register";
import Login from "./pages/Login";
import OtpVerify from "./pages/OtpVerify";
import UserList from "./pages/UserList";

function App() {
  return (
    <div className="app-wrapper">
      <Navbar />
      <div className="container app-content">
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/verify-otp" element={<OtpVerify />} />
          <Route path="/users" element={<UserList />} />
        </Routes>
      </div>
      <footer className="text-center py-3 text-muted small">
        User Management App &copy; 2025
      </footer>
    </div>
  );
}

export default App;
