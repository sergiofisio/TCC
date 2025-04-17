import "./header.css";
import logo from "../../assets/iconePrincipal.svg";
import { useNavigate } from "react-router-dom";
import Button from "../button";

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
