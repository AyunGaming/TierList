import { Link } from 'react-router-dom';

const ResetPassword = () => {
  return (
    <div className="min-h-screen bg-[#121212] flex flex-col items-center justify-center font-sans p-4">
      <h1 className="text-white text-3xl mb-12 font-medium tracking-tight text-center">
        Réinitialiser le mot de passe
      </h1>

      <div className="relative bg-white rounded-xl w-full max-w-[440px] shadow-2xl p-10 pt-12">
        <p className="text-gray-600 text-sm mb-6">
          Saisissez votre adresse email pour recevoir un lien de réinitialisation.
        </p>

        <form className="flex flex-col gap-6" onSubmit={(e) => e.preventDefault()}>
          <div className="flex flex-col gap-2">
            <label className="text-xs font-bold text-gray-700 uppercase">Email</label>
            <input 
              type="email" 
              placeholder="votre@email.com"
              className="w-full p-3 border border-gray-300 rounded-md bg-[#fdfdfd] text-black focus:ring-2 focus:ring-[#58a65c] outline-none transition-all"
            />
          </div>

          <button className="w-full bg-gray-800 hover:bg-black text-white py-3 rounded-lg font-bold text-sm transition-colors">
            Envoyer le lien
          </button>
        </form>

        <div className="mt-8 text-center text-[11px] font-bold uppercase">
          <Link to="/login" className="underline">Retour à la connexion</Link>
        </div>
      </div>
    </div>
  );
};

export default ResetPassword;