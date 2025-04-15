import "./style.css";

import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import logo from "../../assets/iconePrincipal.svg";
import Input from "../../components/form/input";
import axios from "./../../service/api";
import { toastFail, toastSuccess } from "../../functions/toast";

export default function Lost() {
  const navigate = useNavigate();
  const { id, token } = useParams();
  const [newPass, setNewPass] = useState({ password: "", confirmPassword: "" });

  const onSubmit = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    try {
      if (!newPass.password || !newPass.confirmPassword)
        throw new Error("Todos os campos são obrigatórios!");

      if (newPass.password !== newPass.confirmPassword)
        throw new Error("As senhas não conferem!");

      await axios.post(`/lostpassword/${id}/${token}`, {
        password: newPass.password,
      });

      toastSuccess("Senha alterada com sucesso!", 3000, "top-center");
      setTimeout(() => {
        navigate("/");
      }, 3000);
    } catch (error) {
      console.log({ error });
      return toastFail(
        error?.response?.status ? error.response.data.error : error.message,
        3000,
        "top-center"
      );
    }
  };

  return (
    <main className="conteiner-lost">
      <img src={logo} alt="Logo" className="logo" />
      <form onSubmit={onSubmit} className="form-lost">
        <h1>Digite sua nova senha</h1>
        <Input
          type="password"
          label="Senha"
          placeholder="Digite sua senha"
          value={newPass.password}
          onChange={(e) =>
            setNewPass((prev) => ({ ...prev, password: e.target.value }))
          }
          name="senha"
        />
        <Input
          type="password"
          label="Confirmar Senha"
          placeholder="Confirme sua senha"
          value={newPass.confirmPassword}
          onChange={(e) =>
            setNewPass((prev) => ({ ...prev, confirmPassword: e.target.value }))
          }
          name="senha"
        />
        <button type="submit" className="btn-lost">
          Alterar Senha
        </button>
      </form>
    </main>
  );
}
