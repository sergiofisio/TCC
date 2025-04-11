import "./style.css";

export default function Modal({ isOpen, onClose, title, children }) {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>
          {title === "Verificar"
            ? "Informações do Usuário"
            : title === "Editar"
            ? "Editar informações do Usuário"
            : "Deletar Usuário"}
        </h2>
        <div className="modal-body">{children}</div>
        <button onClick={onClose} className="modal-close">
          X
        </button>
      </div>
    </div>
  );
}
