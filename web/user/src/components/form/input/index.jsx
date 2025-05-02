import "./style.css";
import eye from "../../../assets/openEye.svg";
import eyeClose from "../../../assets/closeEye.svg";
import { useState } from "react";
import InputMask from "react-input-mask";

// Função principal que renderiza o componente Input
export default function Input({
  type,
  label,
  placeholder,
  value,
  onChange,
  name,
  mask,
}) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="input">
      <label>{label}</label>
      <div className="input-container relative">
        {type !== "textarea" ? (
          <>
            <InputMask
              type={
                (label === "Senha" || label === "Confirmar Senha") &&
                showPassword
                  ? "text"
                  : type
              }
              mask={mask || ""}
              placeholder={placeholder}
              value={value}
              onChange={onChange}
              name={name}
            />
            {(label === "Senha" || label === "Confirmar Senha") && (
              <img
                className="absolute"
                src={showPassword ? eyeClose : eye}
                alt={`imagem olho ${showPassword ? "fechado" : "aberto"}`}
                onClick={() => setShowPassword(!showPassword)}
              />
            )}
          </>
        ) : (
          <textarea
            placeholder={placeholder}
            value={value}
            onChange={onChange}
            name={name}
            cols={30}
            rows={10}
          />
        )}
      </div>
    </div>
  );
}
