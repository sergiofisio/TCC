import "./style.css";
import Form from "../form/login";
import Contato from "../form/contato";

export default function Modal({ setModal, contato, children, text }) {
  return (
    <div className="modal">
      <div className="modal-content">
        <span className="close" onClick={() => setModal(false)}>
          &times;
        </span>
        <h3>
          {!contato
            ? "Somente acessível pelos administradores"
            : "Entre em contato conosco"}
        </h3>
        {!contato ? <Form /> : <Contato />}
      </div>
    </div>
  );
}
