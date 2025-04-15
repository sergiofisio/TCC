// UserModal.jsx
import { useEffect, useState } from "react";
import Modal from "../dashboard";

import { toastFail, toastSuccess } from "../../../functions/toast";
import { getItem } from "../../../functions/token";
import axios from "../../../service/api";
import StatusSwitch from "../../switch";
import ConfirmDelete from "../confirmDelete";
import TabButtons from "../../tabButtons";
import UserDetails from "./details";
import UserExercises from "./exercises";
import UserEmotionDiary from "./diary";

export default function UserModal({ user, isOpen, onClose, onSave, action }) {
  const isEdit = action === "Editar";
  const isDelete = action === "Deletar";
  const isVerificar = action === "Verificar";

  const [formData, setFormData] = useState("");
  const [tab, setTab] = useState("usuario");

  useEffect(() => {
    if (user) setFormData(user);
  }, [user]);

  const handleSubmit = async () => {
    try {
      await axios.delete(`/auth/user/${user.id_user}`, {
        headers: {
          Authorization: `Bearer ${getItem("token")}`,
        },
      });
      onSave(user.id_user, { active_user: formData.active_user });
      toastSuccess("Usuário atualizado com sucesso!", 3000, "top-center");
      onClose();
    } catch (error) {
      toastFail(
        error?.response?.status === 401
          ? error.response.data.error
          : error.message,
        3000,
        "top-center"
      );
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={action}>
      {isEdit && (
        <>
          <h3>Nome do usuário: {user.name_user}</h3>
          <StatusSwitch
            user={user}
            formData={formData}
            setFormData={setFormData}
          />
          <button onClick={handleSubmit} className="btn-edit">
            Salvar
          </button>
        </>
      )}

      {isDelete && <ConfirmDelete user={user} onConfirm={handleSubmit} />}

      {isVerificar && user && (
        <>
          <TabButtons setTab={setTab} />

          {tab === "usuario" && <UserDetails user={user} />}
          {tab === "exercicios" && <UserExercises user={user} />}
          {tab === "emocao" && <UserEmotionDiary user={user} />}
        </>
      )}
    </Modal>
  );
}
