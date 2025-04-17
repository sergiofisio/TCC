import Button from "../button";

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
