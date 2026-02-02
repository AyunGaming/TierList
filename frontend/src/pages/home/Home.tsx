import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-[#121212] flex flex-col items-center justify-center font-sans p-4">
  
      <div className="text-center mb-16">
        <h1 className="text-white text-4xl font-medium mb-4 tracking-tight">
          Crée ta tier list de logos
        </h1>
        <p className="text-gray-400 italic text-sm">
          Classe et compare les logos d'entreprises de S à D.
        </p>
      </div>

      <div className="relative bg-[#252525] rounded-xl w-full max-w-[600px] p-16 shadow-2xl flex flex-col sm:flex-row gap-12 justify-center items-center">
        <button 
          onClick={() => navigate('/login')}
          className="w-full sm:w-48 bg-[#58a65c] hover:bg-[#4a8d4e] text-white py-4 rounded-lg font-bold text-sm transition-all shadow-lg"
        >
          Se connecter
        </button>

        <button 
          onClick={() => navigate('/register')}
          className="w-full sm:w-48 bg-[#e2b355] hover:bg-[#d1a24a] text-white py-4 rounded-lg font-bold text-sm transition-all shadow-lg"
        >
          S'inscrire
        </button>
      </div>
    </div>
  );
};

export default Home;