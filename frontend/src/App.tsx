import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import Home from "./pages/home/Home";
import ResetPassword from "./pages/login/ResetPassword";
import TierList from "./pages/tierlist/TierList";

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-[#121212]">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />

          <Route path="/register" element={<Register />} />

          <Route path="/reset-password" element={<ResetPassword />} />

          <Route path="/tierlist" element={<TierList />} />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
