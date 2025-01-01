import { toast } from "react-toastify";
import "./style.css";

export default function Os({ img, title, url }) {
  function handleClick(e, link) {
    console.log(!link);

    e.preventDefault();
    e.stopPropagation();
    try {
      if (!link) {
        throw new Error("Em breve teremos esta funcionalidade");
      } else {
        toast.info("Redirecionando...", {
          position: "top-left",
        });
      }
    } catch (error) {
      toast.error(error.message, {
        position: "top-center",
      });
    }
  }

  return (
    <div
      className={`os ${!url && "relative"}`}
      onClick={(e) => handleClick(e, url)}
    >
      <h2 className={!url ? "emBreve" : "existe"}>
        {!url ? "EM BREVE" : title}
      </h2>
      <img src={img} alt={title} />
    </div>
  );
}
