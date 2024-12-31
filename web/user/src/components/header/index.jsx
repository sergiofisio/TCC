import "./header.css";
import logo from "../../assets/iconePrincipal.svg";

export default function Header() {
  return (
    <header className="header">
      <div>
        <img className="logo" src={logo} alt="Logo Emotion Harmony" />
        <h2 className="title">EMOTION HARMONY</h2>
      </div>
    </header>
  );
}
