import "./style.css";
import { useNavigate } from "react-router-dom";
export default function Sidebar({ setView }) {
  const navigate = useNavigate();
  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
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
