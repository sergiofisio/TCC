//função de criação do componente de confirmação de exclusão do usuario
export default function ConfirmDelete({ user, onConfirm }) {
  return (
    <div className="confirm-delete">
      <p>
        Tem certeza que deseja deletar o usuário{" "}
        <strong>{user.name_user}</strong>?
      </p>
      <button onClick={onConfirm} className="btn-delete">
        Confirmar
      </button>
    </div>
  );
}
