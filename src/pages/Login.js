import React, { useState } from "react";
import api from "../api";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [form, setForm] = useState({ email: "", password: "" });
  const [message, setMessage] = useState("");
  const [otpValue, setOtpValue] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setOtpValue("");
    setLoading(true);
    try {
      const res = await api.post("/auth/login", form, { responseType: "text" });
      const text = res.data;
      setMessage(text);
      const parts = text.split(":");
      if (parts.length === 2) {
        const otp = parts[1].trim();
        setOtpValue(otp);
      }
    } catch (err) {
      console.error(err);
      setMessage("Login failed. Check email/password.");
    } finally {
      setLoading(false);
    }
  };

  const goToVerify = () => {
    navigate("/verify-otp", { state: { email: form.email, otpFromLogin: otpValue } });
  };

  return (
    <div className="row justify-content-center">
      <div className="col-md-6 col-lg-5">
        <div className="card shadow-sm">
          <div className="card-body">
            <h3 className="card-title mb-3 text-center">Login</h3>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className="form-control"
                  name="email"
                  value={form.email}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  className="form-control"
                  name="password"
                  value={form.password}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="d-grid">
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? "Logging in..." : "Login & Get OTP"}
                </button>
              </div>
            </form>
            {message && (
              <div className="alert alert-info mt-3" role="alert">
                {message}
              </div>
            )}
            {otpValue && (
              <div className="mt-3">
                <div className="alert alert-warning" role="alert">
                  <strong>Demo OTP:</strong> {otpValue}
                </div>
                <button className="btn btn-outline-secondary" onClick={goToVerify}>
                  Go to OTP Verification
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
