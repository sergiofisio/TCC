import Button from "../button";

//função para renderizar o formulário
export default function Form({
  onSubmit,
  children,
  data,
  loading,
  buttonText,
}) {
  return (
    <form className="form" onSubmit={(e) => onSubmit(e, data)}>
      {children}
      <Button text={buttonText} type="submit" loading={loading} />
    </form>
  );
}
