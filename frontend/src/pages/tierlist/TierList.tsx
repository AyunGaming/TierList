import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { draggable, dropTargetForElements, monitorForElements } from '@atlaskit/pragmatic-drag-and-drop/element/adapter';

interface Company {
  id: string;
  name: string;
  logoUrl: string;
}

const TIERS = [
  { id: 'S', color: 'bg-[#ff7f7f]' },
  { id: 'A', color: 'bg-[#ffbf7f]' },
  { id: 'B', color: 'bg-[#ffff7f]' },
  { id: 'C', color: 'bg-[#7fff7f]' },
  { id: 'D', color: 'bg-[#7fbfff]' },
];

const CompanyLogo = ({ company }: { company: Company }) => (
  <div className="w-16 h-16 bg-[#333] border border-gray-600 rounded flex items-center justify-center cursor-grab active:cursor-grabbing overflow-hidden shadow-md" title={company.name}>
    <img src={`${company.logoUrl}?token=pk_GRX1YgVoTMCmJ2KyAQW9nA`} alt={company.name} className="w-full h-full object-contain pointer-events-none" onError={(e) => (e.currentTarget.style.display = 'none')} />
  </div>
);

const DraggableLogo = ({ company }: { company: Company }) => {
  const ref = useRef<HTMLDivElement>(null);
  const [isDragging, setIsDragging] = useState(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    return draggable({
      element: el,
      getInitialData: () => ({ id: company.id, type: 'logo' }),
      onDragStart: () => setIsDragging(true),
      onDrop: () => setIsDragging(false),
    });
  }, [company]);

  return (
    <div ref={ref} className={isDragging ? 'opacity-30' : 'opacity-100'}>
      <CompanyLogo company={company} />
    </div>
  );
};

const DropZone = ({ id, children, className }: { id: string; children: React.ReactNode; className: string }) => {
  const ref = useRef<HTMLDivElement>(null);
  const [isOver, setIsOver] = useState(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    return dropTargetForElements({
      element: el,
      getData: () => ({ id }),
      onDragEnter: () => setIsOver(true),
      onDragLeave: () => setIsOver(false),
      onDrop: () => setIsOver(false),
    });
  }, [id]);

  return (
    <div ref={ref} className={`${className} ${isOver ? 'bg-white/10' : ''}`}>
      {children}
    </div>
  );
};

const TierList = () => {
  const navigate = useNavigate();
  const [data, setData] = useState<{ [key: string]: Company[] }>({
    available: [], S: [], A: [], B: [], C: [], D: []
  });

  useEffect(() => {
    axios.get<Company[]>('http://localhost:8180/companies')
      .then(res => setData(prev => ({ ...prev, available: res.data })))
      .catch(err => console.error(err));
  }, []);

  useEffect(() => {
    return monitorForElements({
      onDrop({ source, location }) {
        const destination = location.current.dropTargets[0];
        if (!destination) return;

        const itemId = source.data.id as string;
        const targetContainerId = destination.data.id as string;

        setData(prev => {
          const sourceContainerId = Object.keys(prev).find(key => prev[key].some(c => c.id === itemId));
          if (!sourceContainerId || sourceContainerId === targetContainerId) return prev;

          const logo = prev[sourceContainerId].find(c => c.id === itemId)!;
          
          return {
            ...prev,
            [sourceContainerId]: prev[sourceContainerId].filter(c => c.id !== itemId),
            [targetContainerId]: [...prev[targetContainerId], logo],
          };
        });
      },
    });
  }, []);

  return (
    <div className="min-h-screen bg-[#121212] text-white flex flex-col items-center p-6">
      <header className="w-full max-w-5xl flex justify-between mb-8">
        <h1 className="text-2xl font-bold italic">Tier List Maker</h1>
        <button onClick={() => navigate('/')} className="bg-red-500 px-4 py-1 rounded text-sm">Déconnexion</button>
      </header>

      <div className="w-full max-w-5xl bg-[#1a1a1a] border-2 border-black mb-12 shadow-2xl">
        {TIERS.map(tier => (
          <div key={tier.id} className="flex border-b-2 last:border-b-0 border-black min-h-[100px]">
            <div className={`${tier.color} w-24 sm:w-32 flex items-center justify-center text-black font-extrabold text-3xl border-r-2 border-black`}>
              {tier.id}
            </div>
            <DropZone id={tier.id} className="flex-1 p-2 flex flex-wrap gap-2 content-start">
              {data[tier.id].map(c => <DraggableLogo key={c.id} company={c} />)}
            </DropZone>
          </div>
        ))}
      </div>

      <div className="w-full max-w-5xl">
        <div className="bg-[#252525] p-2 inline-block rounded-t-md text-sm font-bold border-x border-t border-black">Logos disponibles</div>
        <DropZone id="available" className="bg-[#1a1a1a] border-2 border-black p-6 rounded-b-md min-h-[150px] flex flex-wrap gap-4">
          {data.available.map(c => <DraggableLogo key={c.id} company={c} />)}
        </DropZone>
      </div>
    </div>
  );
};

export default TierList;