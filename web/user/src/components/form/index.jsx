import { useState } from "react";
import { toastFail, toastSuccess } from "../../functions/toast";
import axios from "../../service/api";
import Input from "./input";
import "./style.css";

export default function Form() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });
  const onSubmit = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    try {
      if (!form.email || !form.password)
        throw new Error("Todos os campos são obrigatórios!");

      const response = await axios.post("/login", {
        email: form.email,
        senha: form.password,
      });

      if (response.data.type_user !== "admin")
        throw new Error("Apenas administradores podem acessar essa área!");

      localStorage.setItem("token", response.data.token);
      toastSuccess("Login efetuado com sucesso!", 3000, "top-center");
      setTimeout(() => {
        window.location.href = "/admin";
      }, 3000);
    } catch (error) {
      console.log({ error });

      return toastFail(
        error?.response?.status === 401
          ? error.response.data.error
          : error.message,
        3000,
        "top-center"
      );
    } finally {
      setForm({
        email: "",
        password: "",
      });
    }
  };

  return (
    <form className="form" onSubmit={onSubmit}>
      <Input
        label="E-mail"
        type="email"
        placeholder="Digite seu email"
        value={form.email}
        onChange={(e) =>
          setForm((prev) => ({ ...prev, email: e.target.value }))
        }
        name="email"
      />
      <Input
        label="Senha"
        type="password"
        placeholder="Digite sua senha"
        value={form.password}
        onChange={(e) =>
          setForm((prev) => ({ ...prev, password: e.target.value }))
        }
        name="senha"
      />
      <button type="submit" className="btn">
        Entrar
      </button>
    </form>
  );
}
