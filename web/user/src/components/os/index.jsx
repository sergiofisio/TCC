import "./style.css";

export default function Os({ img, title, url }) {
  console.log(url);
  return (
    <div className={`os ${!url && "relative"}`}>
      <h2 className={!url ? "emBreve" : "existe"}>EM BREVE</h2>
      <img src={img} alt={title} />
    </div>
  );
}
