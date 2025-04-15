export default function StatusSwitch({ user, formData, setFormData }) {
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
