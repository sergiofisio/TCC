import logo from "../../assets/iconePrincipal.svg";
import "./footer.css";

export default function Footer() {
  return (
    <footer>
      <div>
        <img className="logo" src={logo} alt="Logo Emotion Harmony" />
        <h2 className="title">EMOTION HARMONY</h2>
      </div>
      <p className="copy">© 2022 - Todos os direitos reservados</p>
    </footer>
  );
}
