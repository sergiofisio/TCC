import "./style.css";
import Form from "../form/login";
import Contato from "../form/contato";

//renderiza o modal de login ou contato
export default function Modal({ setModal, contato }) {
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
