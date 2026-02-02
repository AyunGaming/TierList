import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const Login = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setSuccess(false);
    setIsLoading(true);

    try {
      const response = await axios.post(
        "http://localhost:8180/api/user/login",
        {
          email: email,
          password: password,
        },
      );

      if (response.status === 200) {
        setSuccess(true);

        localStorage.setItem("user_connected", "true");
        localStorage.setItem("user_info", JSON.stringify(response.data));

        setTimeout(() => {
          navigate("/tierlist");
        }, 1500);
      }
    } catch (err: any) {
      console.error("Erreur de connexion", err);
      setError("Email ou mot de passe incorrect.");
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#121212] flex flex-col items-center justify-center font-sans px-4 py-10">
      <h1 className="text-white text-[40px] leading-none font-medium tracking-tight mb-10 text-center">
        Connexion
      </h1>

      {success && (
        <div className="mb-4 p-4 bg-green-500 text-white rounded-lg font-bold shadow-lg animate-bounce">
          Connexion réussie ! Préparation de votre espace...
        </div>
      )}

      {error && (
        <div className="mb-4 p-4 bg-red-500 text-white rounded-lg font-bold shadow-lg animate-fade-in">
          {error}
        </div>
      )}
      
      <div className="relative w-full max-w-[440px] bg-white rounded-xl shadow-2xl overflow-visible">
        <form className="p-10 pt-12 flex flex-col gap-6" onSubmit={handleLogin}>
          <div className="flex flex-col gap-2">
            <label className="text-[11px] font-bold text-gray-700 uppercase tracking-wide">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="votre@email.com"
              className="w-full h-11 px-3 border border-gray-300 rounded-md bg-[#fdfdfd] text-black
                         focus:outline-none focus:ring-2 focus:ring-[#58a65c] transition"
              required
            />
          </div>

          <div className="flex flex-col gap-2">
            <label className="text-[11px] font-bold text-gray-700 uppercase tracking-wide">
              Mot de passe
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              className="w-full h-11 px-3 border border-gray-300 rounded-md bg-[#fdfdfd] text-black
                         focus:outline-none focus:ring-2 focus:ring-[#58a65c] transition"
              required
            />

            <Link
              to="/reset-password"
              className="text-[9px] text-gray-500 text-right font-bold underline uppercase mt-1 hover:text-black transition-colors"
            >
              Mot de passe oublié ?
            </Link>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className={`w-full h-12 rounded-lg font-bold text-sm transition-all mt-4 shadow-md text-white
                       ${isLoading ? "bg-gray-400 cursor-not-allowed" : "bg-[#58a65c] hover:bg-[#4a8d4e]"}`}
          >
            {isLoading ? "Connexion..." : "Se connecter"}
          </button>

          <div className="text-center mt-2">
            <Link
              to="/register"
              className="text-gray-600 text-[14px] font-bold underline decoration-gray-400 hover:text-[#58a65c] transition-colors"
            >
              Pas encore de compte ?
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
