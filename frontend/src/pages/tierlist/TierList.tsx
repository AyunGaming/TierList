import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const TIERS = [
  { id: 'S', label: "S: Les chefs-d'oeuvre du branding", color: 'bg-[#ff7f7f]' },
  { id: 'A', label: 'A: Très bons logos', color: 'bg-[#ffbf7f]' },
  { id: 'B', label: 'B: Ça passe', color: 'bg-[#ffff7f]' },
  { id: 'C', label: 'C: Médiocres', color: 'bg-[#7fff7f]' },
  { id: 'D', label: 'D: Les flops visuels', color: 'bg-[#7fbfff]' },
];

const TierList = () => {
  const navigate = useNavigate();
  const [username] = useState("Test"); 

  const handleLogout = () => {
    navigate('/');
  };

  const handleExportPDF = () => {
    console.log("Exportation en cours vers S3...");
  };

  return (
    <div className="min-h-screen bg-[#121212] text-white flex flex-col items-center p-6">
      
      <header className="w-full max-w-5xl flex justify-between items-center mb-12">
        <div className="flex items-center gap-4">
          <h1 className="text-2xl font-semibold italic">Bienvenue {username} !</h1>
        </div>
        <button 
          onClick={handleLogout}
          className="bg-[#e25555] hover:bg-red-700 px-4 py-1 rounded text-sm font-bold transition-colors"
        >
          Déconnexion
        </button>
      </header>

      <div className="w-full max-w-5xl bg-[#1a1a1a] border-2 border-black rounded-sm shadow-2xl mb-12 relative">
        <button 
          onClick={handleExportPDF}
          className="absolute -top-8 right-0 bg-[#4a90e2] hover:bg-blue-600 px-3 py-1 rounded text-xs font-bold transition-all"
        >
          Exporter PDF
        </button>

        {TIERS.map((tier) => (
          <div key={tier.id} className="flex border-b-2 last:border-b-0 border-black min-h-[100px]">
            <div className={`${tier.color} w-24 sm:w-32 flex items-center justify-center text-black font-extrabold text-3xl border-r-2 border-black`}>
              {tier.id}
            </div>
            <div className="flex-1 p-2 flex flex-wrap gap-2 content-start">
              {/* Les logos viendront ici via le drag & drop */}
            </div>
          </div>
        ))}
      </div>

      <div className="w-full max-w-5xl">
        <div className="bg-[#252525] p-2 inline-block rounded-t-md text-sm font-bold border-x border-t border-black">
          Liste des logos disponibles
        </div>
        <div className="bg-[#1a1a1a] border-2 border-black p-6 rounded-b-md min-h-[200px]">
          <div className="grid grid-cols-4 sm:grid-cols-6 md:grid-cols-8 gap-4 justify-items-center">
            {[...Array(6)].map((_, i) => (
              <div key={i} className="w-16 h-16 bg-[#333] border border-gray-600 rounded flex items-center justify-center cursor-pointer hover:border-[#e2b355] transition-all">
                <span className="text-xs text-gray-500">Logo</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TierList;