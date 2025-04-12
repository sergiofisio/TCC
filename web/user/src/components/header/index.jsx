import "./header.css";
import logo from "../../assets/iconePrincipal.svg";
import { useNavigate } from "react-router-dom";

export default function Header({ setModal, isAdmin }) {
  const Navigate = useNavigate();

  function handleLogin() {
    if (isAdmin) {
      return Navigate("/admin");
    }
    setModal(true);
  }

  return (
    <header className="header">
      <div>
        <img className="logo" src={logo} alt="Logo Emotion Harmony" />
        <h2 className="title">EMOTION HARMONY</h2>
      </div>
      <button type="button" className="btn-login" onClick={handleLogin}>
        {!isAdmin
          ? "Entrar Painel Admin"
          : "Logado - clique para entrar no Painel"}
      </button>
    </header>
  );
}
