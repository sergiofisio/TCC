import { useState } from "react";
import { toastFail, toastSuccess } from "../../../functions/toast";
import axios from "../../../service/api";
import Input from "../input";
import "./style.css";
import Button from "../../button";
import Form from "./../index";

// Função principal que renderiza o componente de Login
export default function Login() {
  // Estado para armazenar os dados do formulário
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  // Estado para controlar o carregamento do botão
  const [loading, setLoading] = useState(false);

  // Função para lidar com o envio do formulário
  const onSubmit = async (e, data) => {
    setLoading(true);
    e.preventDefault();
    e.stopPropagation();

    // Desestruturação dos dados do formulário
    const { email, password } = data;
    try {
      if (!email || !password)
        throw new Error("Todos os campos são obrigatórios!");

      // Envio dos dados para o servidor
      const response = await axios.post("/login", {
        email,
        senha,
      });

      // Verifica se o usuário é um administrador
      if (response.data.type_user !== "admin")
        throw new Error("Apenas administradores podem acessar essa área!");

      sessionStorage.setItem("token", response.data.token);
      toastSuccess("Login efetuado com sucesso!", 3000, "top-center");
      setTimeout(() => {
        window.location.href = "/admin";
      }, 3000);
    } catch (error) {
      console.log({ error });

      // Exibe mensagem de erro caso o login falhe
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
