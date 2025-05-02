//função de criação do componente de botões de aba
export default function TabButtons({ setTab }) {
  return (
    <div className="tab-buttons">
      <button onClick={() => setTab("usuario")}>Usuário</button>
      <button onClick={() => setTab("exercicios")}>Exercícios</button>
      <button onClick={() => setTab("emocao")}>Emoções</button>
    </div>
  );
}
