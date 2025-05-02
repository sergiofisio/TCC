import logo from "../../assets/iconePrincipal.svg";
import "./footer.css";

// Função principal que renderiza o componente Footer
export default function Footer({ setModal }) {
  return (
    <footer>
      <div>
        <img className="logo-footer" src={logo} alt="Logo Emotion Harmony" />
        <h2 className="title">EMOTION HARMONY</h2>
      </div>
      <div>
        <button onClick={() => setModal(true)}>Contato</button>
      </div>
      <p className="copy">© 2022 - Todos os direitos reservados</p>
    </footer>
  );
}
