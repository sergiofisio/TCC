import { forwardRef } from "react";
import Button from "../button";

const Form = forwardRef(function Form(
  { onSubmit, children, data, loading, buttonText },
  ref
) {
  return (
    <form className="form" ref={ref} onSubmit={(e) => onSubmit(e, data)}>
      {children}
      <Button text={buttonText} type="submit" loading={loading} />
    </form>
  );
});

export default Form;
