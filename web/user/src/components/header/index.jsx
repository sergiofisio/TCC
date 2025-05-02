import "./header.css";
import logo from "../../assets/iconePrincipal.svg";
import { useNavigate } from "react-router-dom";
import Button from "../button";

// Função principal que renderiza o componente Header
export default function Header({ setModal, isAdmin }) {
  // Hook do React Router para navegação
  const Navigate = useNavigate();

  // Função que lida com o clique no botão de login
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
      <Button
        className="btn-login"
        text={
          !isAdmin
            ? "Entrar Painel Admin"
            : "Logado - clique para entrar no Painel"
        }
        onClick={handleLogin}
      />
    </header>
  );
}
