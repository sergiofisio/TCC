import "./style.css";
import { useNavigate } from "react-router-dom";
export default function Sidebar({ setView, setIsAdmin }) {
  const navigate = useNavigate();
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
