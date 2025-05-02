//função do componente de criação do switch de status do usuario
export default function StatusSwitch({ user, formData, setFormData }) {
  //função para mudança do switch
  const toggleStatus = () => {
    setFormData({ ...formData, active_user: !formData.active_user });
  };

  return (
    <div className="status-switch">
      <label>
        <input
          type="checkbox"
          checked={formData.active_user}
          onChange={toggleStatus}
        />
        Ativo
      </label>
    </div>
  );
}
