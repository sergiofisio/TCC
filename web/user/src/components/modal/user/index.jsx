import { useEffect, useState } from "react";
import Modal from "../dashboard";
import { formatPhone } from "../../../functions/format";
import {
  format,
  getDate,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  addMonths,
  subMonths,
} from "date-fns";
import { ptBR } from "date-fns/locale";
import axios from "./../../../service/api";
import { toastFail, toastSuccess } from "../../../functions/toast";

export default function UserModal({ user, isOpen, onClose, onSave, action }) {
  const isEdit = action === "Editar";
  const isDelete = action === "Deletar";
  const isVerificar = action === "Verificar";

  const [formData, setFormData] = useState("");
  const [tab, setTab] = useState("usuario");
  const [currentMonth, setCurrentMonth] = useState(new Date());

  useEffect(() => {
    if (user) {
      setFormData(user);
    }
  }, [user]);

  const handleSubmit = async () => {
    try {
      await axios.delete(
        `/auth/user/${user.id_user}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
        { active_user: formData.active_user }
      );

      onSave(user.id_user, { active_user: formData.active_user });
      toastSuccess("Usuário atualizado com sucesso!", 3000, "top-center");
      onClose();
    } catch (error) {
      console.error(error);
      toastFail(
        error?.response?.status === 401
          ? error.response.data.error
          : error.message,
        3000,
        "top-center"
      );
    }
  };

  const personalPhone = user?.phones_user?.find(
    (p) => p.type_phone === "celular"
  );
  const emergencyPhone = user?.phones_user?.find(
    (p) => p.type_phone === "emergencia"
  );

  const getMonthData = () => {
    const startMonth = startOfMonth(currentMonth);
    const endMonth = endOfMonth(currentMonth);
    const startDate = startOfWeek(startMonth);
    const endDate = endOfWeek(endMonth);

    const days = [];
    let date = new Date(startDate);

    while (date <= endDate) {
      days.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }

    return days;
  };

  const daysInMonth = getMonthData();
  const emotionsByDate = (user?.todays_user || []).reduce((acc, item) => {
    const dateKey = format(new Date(item.created_at), "yyyy-MM-dd");
    if (!acc[dateKey]) acc[dateKey] = [];
    acc[dateKey].push(item);
    return acc;
  }, {});

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={action}>
      {isEdit ? (
        <>
          <h3>Nome do usuário: {user.name_user}</h3>
          <div className="switch-container">
            <span className="switch-label">Status:</span>
            <label className="switch">
              <input
                type="checkbox"
                checked={
                  formData.active_user !== undefined
                    ? formData.active_user
                    : user.active_user
                }
                onChange={() =>
                  setFormData((prev) => ({
                    ...prev,
                    active_user: !(formData.active_user ?? user.active_user),
                  }))
                }
              />
              <span className="slider"></span>
            </label>
            <span>
              {formData.active_user ?? user.active_user ? "Ativo" : "Inativo"}
            </span>
          </div>
          <button onClick={handleSubmit} className="btn-edit">
            Salvar
          </button>
        </>
      ) : isDelete ? (
        <>
          <p>
            Tem certeza que deseja deletar <strong>{user?.name_user}</strong>?
          </p>
          <button onClick={handleSubmit} className="btn-delete">
            Confirmar
          </button>
        </>
      ) : isVerificar && user ? (
        <>
          <div className="tab-buttons">
            <button onClick={() => setTab("usuario")}>Dados</button>
            <button onClick={() => setTab("exercicios")}>Exercícios</button>
            <button onClick={() => setTab("emocao")}>Diário</button>
          </div>

          {tab === "usuario" && (
            <>
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
                <strong>Telefone Emergência:</strong>{" "}
                {formatPhone(emergencyPhone)}
              </p>
              <p>
                <strong>Status:</strong>{" "}
                {user.active_user ? "Ativo" : "Inativo"}
              </p>
            </>
          )}

          {tab === "exercicios" && (
            <>
              <h4>Meditações: {user.meditations_user?.length || 0}</h4>
              <h4>Respirações: {user.breaths_user?.length || 0}</h4>
            </>
          )}

          {tab === "emocao" && (
            <>
              <div className="calendar-controls">
                <button
                  onClick={() => setCurrentMonth(subMonths(currentMonth, 1))}
                >
                  {"<"}
                </button>
                <h4>{format(currentMonth, "MMMM yyyy", { locale: ptBR })}</h4>
                <button
                  onClick={() => setCurrentMonth(addMonths(currentMonth, 1))}
                >
                  {">"}
                </button>
              </div>

              <div className="calendar-grid">
                {["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"].map(
                  (day, idx) => (
                    <div key={idx} className="calendar-header">
                      {day}
                    </div>
                  )
                )}

                {daysInMonth.map((date, idx) => {
                  const dateKey = format(date, "yyyy-MM-dd");
                  const emotionEntries = emotionsByDate[dateKey] || [];
                  const isOutsideMonth =
                    date.getMonth() !== currentMonth.getMonth();

                  return (
                    <div
                      key={idx}
                      className={`calendar-cell ${
                        isOutsideMonth ? "outside" : ""
                      }`}
                    >
                      <div className="calendar-date">{getDate(date)}</div>
                      {emotionEntries.map((entry, i) => (
                        <div key={i} className="emotion-entry">
                          <span>{entry.morning_afternoon_evening}</span>:{" "}
                          <strong>{entry.emotion_today}</strong>
                        </div>
                      ))}
                    </div>
                  );
                })}
              </div>
            </>
          )}
        </>
      ) : null}
    </Modal>
  );
}
