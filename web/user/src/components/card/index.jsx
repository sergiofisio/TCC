import "./style.css";
import allUsers from "../../assets/allUsers.svg";
import usersActive from "../../assets/userActive.svg";
import usersInactive from "../../assets/userInactive.svg";

export default function Card({ description, number }) {
  return (
    <div
      className={`card ${description
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/\s/g, "")}`}
    >
      <img
        src={
          description === "Total Usuários"
            ? allUsers
            : description === "Usuários Ativos"
            ? usersActive
            : usersInactive
        }
      />
      <h3>{description}</h3>
      <p>{number}</p>
    </div>
  );
}
