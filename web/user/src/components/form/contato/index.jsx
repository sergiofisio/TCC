import { useState } from "react";
import { toastFail, toastSuccess } from "../../../functions/toast";
import Input from "../input";
import emailjs from "@emailjs/browser";
import Form from "./../index";

export default function Contato() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    telefone: "",
    mensagem: "",
  });

  const [loading, setLoading] = useState(false);

  const onSubmit = async (e, data) => {
    e.preventDefault();
    e.stopPropagation();
    const { name, email, telefone, mensagem } = data;
    setLoading(true);

    try {
      if (!name || !email || !mensagem || !telefone)
        throw new Error("Todos os campos são obrigatórios!");

      const res = await emailjs.send(
        import.meta.env.VITE_EMAILJS_SERVICE_ID,
        import.meta.env.VITE_EMAILJS_TEMPLATE_ID,
        {
          name,
          email,
          telefone,
          mensagem,
        },
        import.meta.env.VITE_EMAILJS_USER_ID
      );

      toastSuccess("Mensagem enviada com sucesso!", 3000, "top-center");
      console.log(res);
    } catch (error) {
      console.log({ error });
      toastFail(
        error?.response?.status === 401
          ? error.response.data.error
          : error.message,
        3000,
        "top-center"
      );
    } finally {
      setForm({
        nome: "",
        email: "",
        mensagem: "",
      });
      setLoading(false);
    }
  };

  return (
    <Form onSubmit={onSubmit} data={form} loading={loading} buttonText="Enviar">
      <Input
        label="Nome"
        type="text"
        placeholder="Digite seu nome"
        value={form.name}
        onChange={(e) => setForm((prev) => ({ ...prev, name: e.target.value }))}
        name="nome"
      />
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
        label="Celular"
        type="text"
        mask="(99) 99999-9999"
        placeholder="Digite seu telefone"
        value={form.telefone}
        onChange={(e) =>
          setForm((prev) => ({ ...prev, telefone: e.target.value }))
        }
        name="telefone"
      />
      <Input
        label="Mensagem"
        type="textarea"
        placeholder="Digite sua mensagem"
        value={form.mensagem}
        onChange={(e) =>
          setForm((prev) => ({ ...prev, mensagem: e.target.value }))
        }
        name="mensagem"
      />
    </Form>
  );
}
