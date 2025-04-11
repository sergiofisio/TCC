import { formatPhone } from "../../functions/format";
import "./style.css";

export default function Table({ description, users, action }) {
  return (
    <div className="table-container">
      <h3>{description}</h3>
      <table className="table">
        <thead>
          <tr>
            <th className="id">ID</th>
            <th className="nome">Nome</th>
            <th className="email">Email</th>
            <th className="telefone">Telefone pessoal</th>
            <th className="telefone">Telefone emergência</th>
            <th className="acoes"></th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, key) => {
            const personalPhone = user.phones_user.find(
              (p) => p.type_phone === "celular"
            );
            const emergencyPhone = user.phones_user.find(
              (p) => p.type_phone === "emergencia"
            );

            return (
              <tr key={key}>
                <td onClick={() => action("Verificar", user)} className="id">
                  {user.id_user}
                </td>
                <td onClick={() => action("Verificar", user)} className="nome">
                  {user.name_user}
                </td>
                <td onClick={() => action("Verificar", user)} className="email">
                  {user.email_user}
                </td>
                <td
                  onClick={() => action("Verificar", user)}
                  className="telefone"
                >
                  {formatPhone(personalPhone)}
                </td>
                <td
                  onClick={() => action("Verificar", user)}
                  className="telefone"
                >
                  {formatPhone(emergencyPhone)}
                </td>
                <td className="acoes">
                  <button
                    onClick={() => action("Editar", user)}
                    className="btn-edit"
                  >
                    Editar
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
