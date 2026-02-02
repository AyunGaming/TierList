import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const Register = () => {
  const [formData, setFormData] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
  });
  const [success, setSuccess] = useState(false); // État pour le message de succès
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8180/api/user/register", formData);
      setSuccess(true);

      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (err) {
      console.error(err);
      alert("Erreur lors de l'inscription");
    }
  };

  return (
    <div className="min-h-screen bg-[#121212] flex flex-col items-center justify-center font-sans p-4">
      <h1 className="text-white text-4xl mb-10 font-medium tracking-tight">
        Inscription
      </h1>

      {success && (
        <div className="mb-4 p-4 bg-green-500 text-white rounded-lg font-bold shadow-lg animate-bounce">
          Compte créé avec succès ! Redirection...
        </div>
      )}

      <div className="relative bg-white rounded-xl w-full max-w-[440px] shadow-2xl overflow-visible">
        <form
          className="p-10 pt-12 flex flex-col gap-5"
          onSubmit={handleSubmit}
        >
          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold text-gray-700 uppercase">
              Nom
            </label>
            <input
              type="text"
              placeholder="Nom"
              className="w-full p-2.5 border border-gray-300 rounded bg-[#fdfdfd] text-black focus:ring-2 focus:ring-[#e2b355] outline-none transition-all"
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              required
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold text-gray-700 uppercase">
              Pseudo
            </label>
            <input
              type="text"
              placeholder="Prénom"
              className="w-full p-2.5 border border-gray-300 rounded bg-[#fdfdfd] text-black focus:ring-2 focus:ring-[#e2b355] outline-none transition-all"
              onChange={(e) =>
                setFormData({ ...formData, username: e.target.value })
              }
              required
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold text-gray-700 uppercase">
              Email
            </label>
            <input
              type="email"
              placeholder="Email"
              className="w-full p-2.5 border border-gray-300 rounded bg-[#fdfdfd] text-black focus:ring-2 focus:ring-[#e2b355] outline-none transition-all"
              onChange={(e) =>
                setFormData({ ...formData, email: e.target.value })
              }
              required
            />
          </div>

          <div className="flex flex-col gap-1.5 pb-2">
            <label className="text-[11px] font-bold text-gray-700 uppercase">
              Mot de passe
            </label>
            <input
              type="password"
              placeholder="Mot de passe"
              className="w-full p-2.5 border border-gray-300 rounded bg-[#fdfdfd] text-black focus:ring-2 focus:ring-[#e2b355] outline-none transition-all"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-[#e2b355] hover:bg-[#d1a24a] text-white py-3.5 rounded-lg font-bold text-sm transition-colors mt-2 shadow-sm"
          >
            S'inscrire
          </button>
          <div className="text-center mt-2">
            <Link
              to="/login"
              className="text-gray-600 text-[14px] font-bold underline hover:text-[#e2b355]"
            >
              Déjà un compte ? Se connecter
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;
