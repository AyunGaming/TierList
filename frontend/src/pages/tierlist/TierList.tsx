import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {
  draggable,
  dropTargetForElements,
  monitorForElements,
} from "@atlaskit/pragmatic-drag-and-drop/element/adapter";

interface Company {
  id: string;
  name: string;
  logoUrl: string;
}

const TIERS = [
  { id: "S", color: "bg-[#ff7f7f]" },
  { id: "A", color: "bg-[#ffbf7f]" },
  { id: "B", color: "bg-[#ffff7f]" },
  { id: "C", color: "bg-[#7fff7f]" },
  { id: "D", color: "bg-[#7fbfff]" },
];

const CompanyLogo = ({ company }: { company: Company }) => (
  <div
    className="w-16 h-16 bg-[#333] border border-gray-600 rounded flex items-center justify-center cursor-grab active:cursor-grabbing overflow-hidden shadow-md"
    title={company.name}
  >
    <img
      src={`${company.logoUrl}?token=pk_GRX1YgVoTMCmJ2KyAQW9nA`}
      alt={company.name}
      className="w-full h-full object-contain pointer-events-none"
      onError={(e) => (e.currentTarget.style.display = "none")}
    />
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
      getInitialData: () => ({ id: company.id, type: "logo" }),
      onDragStart: () => setIsDragging(true),
      onDrop: () => setIsDragging(false),
    });
  }, [company]);

  return (
    <div ref={ref} className={isDragging ? "opacity-30" : "opacity-100"}>
      <CompanyLogo company={company} />
    </div>
  );
};

const DropZone = ({
  id,
  children,
  className,
}: {
  id: string;
  children: React.ReactNode;
  className: string;
}) => {
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
    <div ref={ref} className={`${className} ${isOver ? "bg-white/10" : ""}`}>
      {children}
    </div>
  );
};

const TierList = () => {
  const navigate = useNavigate();
  const [username] = useState("Test");
  const [tierListId, setTierListId] = useState<string | null>(null);
  const [data, setData] = useState<{ [key: string]: Company[] }>({
    available: [],
    S: [],
    A: [],
    B: [],
    C: [],
    D: [],
  });

  useEffect(() => {
    const userInfoRaw = localStorage.getItem("user_info");
    if (!userInfoRaw) {
      navigate("/login");
      return;
    }

    const initializeTierList = async () => {
      try {
        const user = JSON.parse(userInfoRaw) as { id: string };
        const [companiesRes, tierListRes] = await Promise.all([
          axios.get<Company[]>("http://localhost:8180/companies"),
          axios.post("http://localhost:8180/tier-lists", {
            userId: user.id,
          }),
        ]);

        const companies = companiesRes.data;
        const tierList = tierListRes.data as {
          id: string;
          tiers: {
            name: string;
            listCompany: { name: string; logoUrl: string }[];
          }[];
        };

        const newData: { [key: string]: Company[] } = {
          available: [],
          S: [],
          A: [],
          B: [],
          C: [],
          D: [],
        };

        const assignedNames = new Set<string>();

        tierList.tiers.forEach((tier) => {
          const tierKey = tier.name.toUpperCase();
          if (!["S", "A", "B", "C", "D"].includes(tierKey)) return;

          newData[tierKey] = tier.listCompany.map((c) => {
            assignedNames.add(c.name);
            const existing = companies.find((comp) => comp.name === c.name);

            return {
              id: existing?.id ?? `tier-${tierKey}-${c.name}`,
              name: c.name,
              logoUrl: c.logoUrl,
            };
          });
        });

        newData.available = companies
          .filter((c) => !assignedNames.has(c.name))
          .map((c) => ({ id: c.id, name: c.name, logoUrl: c.logoUrl }));

        setTierListId(tierList.id);
        setData(newData);
      } catch (e) {
        console.error("Erreur lors de l'initialisation de la tierlist", e);
      }
    };

    initializeTierList();
  }, [navigate]);

  useEffect(() => {
    return monitorForElements({
      onDrop({ source, location }) {
        const destination = location.current.dropTargets[0];
        if (!destination) return;

        const itemId = source.data.id as string;
        const targetContainerId = destination.data.id as string;

        setData((prev) => {
          const sourceContainerId = Object.keys(prev).find((key) =>
            prev[key].some((c) => c.id === itemId),
          );
          if (!sourceContainerId || sourceContainerId === targetContainerId)
            return prev;

          const logo = prev[sourceContainerId].find((c) => c.id === itemId)!;

          if (tierListId && logo && logo.name) {
            if (targetContainerId === "available" && sourceContainerId !== "available") {
              // Retirer la company de tous les tiers
              axios
                .post("http://localhost:8180/tiers/remove", {
                  tierListId: tierListId,
                  companyName: logo.name,
                })
                .catch((err) => {
                  console.error(
                    "Erreur lors de la suppression de la company des tiers",
                    err,
                  );
                });
            } else if (targetContainerId !== "available") {
              // Assigner la company au tier cible
              axios
                .post("http://localhost:8180/tiers/assign", {
                  tierListId: tierListId,
                  companyName: logo.name,
                  tierName: targetContainerId,
                })
                .catch((err) => {
                  console.error(
                    "Erreur lors de l'assignation de la company au tier",
                    err,
                  );
                });
            }
          }

          return {
            ...prev,
            [sourceContainerId]: prev[sourceContainerId].filter(
              (c) => c.id !== itemId,
            ),
            [targetContainerId]: [...prev[targetContainerId], logo],
          };
        });
      },
    });
  }, [tierListId]);

  const handleExportPDF = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8180/exportSyntheseTierListsPdf",
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "synthese-tier-lists.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Erreur lors de l'export PDF", err);
      alert("Erreur lors de l'export du PDF");
    }
  };

  return (
    <div className="min-h-screen bg-[#121212] text-white flex flex-col items-center p-6">
      <header className="w-full max-w-5xl flex justify-between mb-12">
        <h1 className="text-2xl font-bold italic">Bienvenue {username} !</h1>
        <button
          onClick={() => navigate("/")}
          className="bg-red-500 px-4 py-1 rounded text-sm"
        >
          Déconnexion
        </button>
      </header>

      <div className="w-full max-w-5xl bg-[#1a1a1a] border-2 border-black mb-12 shadow-2xl relative">
        <button
          onClick={handleExportPDF}
          className="absolute -top-8 right-0 bg-[#4a90e2] hover:bg-blue-600 px-3 py-1 rounded text-xs font-bold transition-all"
        >
          Synthèse des tier lists
        </button>
        {TIERS.map((tier) => (
          <div
            key={tier.id}
            className="flex border-b-2 last:border-b-0 border-black min-h-[100px]"
          >
            <div
              className={`${tier.color} w-24 sm:w-32 flex items-center justify-center text-black font-extrabold text-3xl border-r-2 border-black`}
            >
              {tier.id}
            </div>
            <DropZone
              id={tier.id}
              className="flex-1 p-2 flex flex-wrap gap-2 content-start"
            >
              {data[tier.id].map((c) => (
                <DraggableLogo key={c.id} company={c} />
              ))}
            </DropZone>
          </div>
        ))}
      </div>

      <div className="w-full max-w-5xl">
        <div className="bg-[#252525] p-2 inline-block rounded-t-md text-sm font-bold border-x border-t border-black">
          Logos disponibles
        </div>
        <DropZone
          id="available"
          className="bg-[#1a1a1a] border-2 border-black p-6 rounded-b-md min-h-[150px] flex flex-wrap gap-4"
        >
          {data.available.map((c) => (
            <DraggableLogo key={c.id} company={c} />
          ))}
        </DropZone>
      </div>
    </div>
  );
};

export default TierList;
