import "./style.css";
import { useNavigate } from "react-router-dom";

// Função para criar o componente de barra lateral
export default function Sidebar({ setView, setIsAdmin }) {
  // Hook para navegação
  const navigate = useNavigate();

  // Função para lidar com o logout
  const handleLogout = () => {
    sessionStorage.clear();
    setIsAdmin(false);
    navigate("/");
  };
  return (
    <aside className="sidebar">
      <div>
        <button onClick={() => setView("users")}>Usuários</button>
        <button onClick={() => setView("charts")}>Gráficos</button>
      </div>
      <button onClick={handleLogout} className="btn-logout">
        Sair
      </button>
    </aside>
  );
}
