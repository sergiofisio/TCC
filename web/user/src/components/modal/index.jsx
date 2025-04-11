import "./style.css"
import Form from "../form"


export default function Modal({ setModal }) {
    return (
        <div className="modal">
            <div className="modal-content">
                <span className="close" onClick={() => setModal(false)}>&times;</span>
                <h3>Somente acessível pelos administradores</h3>
                <Form />
            </div>
        </div>
    )
}