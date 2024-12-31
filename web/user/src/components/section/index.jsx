import "./style.css";

export default function Section({ title, className, text, img }) {
  return (
    <section className="section">
      <h1>{title}</h1>
      <div className={className}>
        <img src={img} alt={`imagem ${title}`} />
        <p>{text}</p>
      </div>
    </section>
  );
}
