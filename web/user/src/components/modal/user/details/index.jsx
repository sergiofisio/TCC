import { formatPhone } from "./../../../../functions/format";

//função para criação do componente de detalhes do usuario
export default function UserDetails({ user }) {
  const personalPhone = user?.phones_user?.find(
    (p) => p.type_phone === "celular"
  );
  const emergencyPhone = user?.phones_user?.find(
    (p) => p.type_phone === "emergencia"
  );
  return (
    <div className="user-details">
      <p>
        <strong>ID:</strong> {user.id_user}
      </p>
      <p>
        <strong>Nome:</strong> {user.name_user}
      </p>
      <p>
        <strong>Email:</strong> {user.email_user}
      </p>
      <p>
        <strong>CPF:</strong>{" "}
        {user.cpf_user
          .replace(/(\d{3})(\d)/, "$1.$2")
          .replace(/(\d{3})(\d)/, "$1.$2")
          .replace(/(\d{3})(\d{1,2})$/, "$1-$2")}
      </p>
      <p>
        <strong>Telefone Pessoal:</strong> {formatPhone(personalPhone)}
      </p>
      <p>
        <strong>Telefone Emergência:</strong> {formatPhone(emergencyPhone)}
      </p>
      <p>
        <strong>Status:</strong> {user.active_user ? "Ativo" : "Inativo"}
      </p>
    </div>
  );
}
