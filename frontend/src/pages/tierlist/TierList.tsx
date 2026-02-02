import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const TIERS = [
  { id: 'S', label: "S: Les chefs-d'oeuvre du branding", color: 'bg-[#ff7f7f]' },
  { id: 'A', label: 'A: Très bons logos', color: 'bg-[#ffbf7f]' },
  { id: 'B', label: 'B: Ça passe', color: 'bg-[#ffff7f]' },
  { id: 'C', label: 'C: Médiocres', color: 'bg-[#7fff7f]' },
  { id: 'D', label: 'D: Les flops visuels', color: 'bg-[#7fbfff]' },
];

interface Company {
  id: string;
  name: string;
  logoUrl: string;
}

const getLogoImageUrl = (logoUrl: string): string => {
  try {    
    // Construire l'URL de l'image via l'API logo.dev
    return `${logoUrl}?token=pk_GRX1YgVoTMCmJ2KyAQW9nA`;
  } catch (error) {
    console.error('Erreur lors de la construction de l\'URL du logo:', error);
    return logoUrl; // Retourner l'URL originale en cas d'erreur
  }
};

const CompanyLogo = ({ company }: { company: Company }) => {
  const [imageError, setImageError] = useState(false);
  const imageUrl = company.logoUrl ? getLogoImageUrl(company.logoUrl) : null;

  return (
    <div
      className="w-16 h-16 bg-[#333] border border-gray-600 rounded flex items-center justify-center cursor-pointer hover:border-[#e2b355] transition-all overflow-hidden"
      title={company.name}
    >
      {imageUrl && !imageError ? (
        <img
          src={imageUrl}
          alt={company.name}
          className="w-full h-full object-contain"
          onError={() => setImageError(true)}
        />
      ) : (
        <span className="text-xs text-gray-500">{company.name}</span>
      )}
    </div>
  );
};

const TierList = () => {
  const navigate = useNavigate();
  const [username] = useState("Test");
  const [companies, setCompanies] = useState<Company[]>([]);
  const [isLoading, setIsLoading] = useState(true); 

  const handleLogout = () => {
    navigate('/');
  };

  const handleExportPDF = () => {
    console.log("Exportation en cours vers S3...");
  };

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const response = await axios.get<Company[]>('http://localhost:8180/companies');
        setCompanies(response.data);
      } catch (error) {
        console.error('Erreur lors de la récupération des companies:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchCompanies();
  }, []);

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
            {isLoading ? (
              <div className="col-span-full text-gray-500 text-sm">Chargement...</div>
            ) : companies.length === 0 ? (
              <div className="col-span-full text-gray-500 text-sm">Aucune company disponible</div>
            ) : (
              companies.map((company) => (
                <CompanyLogo key={company.id} company={company} />
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TierList;