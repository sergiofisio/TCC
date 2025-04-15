import "./style.css";
import eye from "../../../assets/openEye.svg";
import eyeClose from "../../../assets/closeEye.svg";
import { useState } from "react";

export default function Input({
  type,
  label,
  placeholder,
  value,
  onChange,
  name,
}) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="input">
      <label>{label}</label>
      <div className="input-container relative">
        <input
          type={
            (label === "Senha" || label === "Confirmar Senha") && showPassword
              ? "text"
              : type
          }
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          name={name}
        />
        {(label === "Senha" || label === "Confirmar Senha") && (
          <img
            className="absolute"
            src={showPassword ? eyeClose : eye}
            alt={`imagem olho ${showPassword ? "fechaddo" : "aberto"}`}
            onClick={() => setShowPassword(!showPassword)}
          />
        )}
      </div>
    </div>
  );
}
