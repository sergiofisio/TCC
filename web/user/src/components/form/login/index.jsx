import { useState } from "react";
import { toastFail, toastSuccess } from "../../../functions/toast";
import axios from "../../../service/api";
import Input from "../input";
import "./style.css";
import Button from "../../button";
import Form from "./../index";

export default function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState(false);

  const onSubmit = async (e, data) => {
    setLoading(true);
    e.preventDefault();
    e.stopPropagation();

    const { email, password } = data;
    try {
      if (!email || !password)
        throw new Error("Todos os campos são obrigatórios!");

      const response = await axios.post("/login", {
        email,
        senha,
      });

      if (response.data.type_user !== "admin")
        throw new Error("Apenas administradores podem acessar essa área!");

      sessionStorage.setItem("token", response.data.token);
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
      setLoading(false);
    }
  };

  return (
    <Form onSubmit={onSubmit} data={form} loading={loading} buttonText="Entrar">
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
    </Form>
  );
}
