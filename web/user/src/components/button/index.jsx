import ClipLoader from "react-spinners/ClipLoader";
import "./style.css";

// Função principal que renderiza o componente Button
export default function Button({ text, type, loading, className, onClick }) {
  return (
    <button
      className={`${loading ? "loading" : ""} button ${className}`}
      type={type}
      onClick={onClick}
      disabled={loading}
    >
      {!loading ? text : <ClipLoader color="#000" />}
    </button>
  );
}
