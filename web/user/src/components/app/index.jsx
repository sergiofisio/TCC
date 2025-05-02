import imgAndroid from "../../assets/android.svg";
import imgApple from "../../assets/IOS.svg";
import Os from "./../os";
import "./appSection.css";

// criação de um array com as informações do aplicativo
const osInfo = [
  {
    id: 1,
    name: "Android",
    img: imgAndroid,
    url: "#teste",
  },
  {
    id: 2,
    name: "iOS",
    img: imgApple,
    url: "",
  },
];

// Função principal que renderiza a seção do aplicativo
export default function AppSection() {
  return (
    <section className="app">
      <h1>Conheça nosso aplicativo</h1>
      <div>
        <p>
          Descubra como a EMOTION HARMONY pode ajudar você a alcançar um
          equilíbrio emocional a qualquer momento! Nosso aplicativo, disponível
          para Android, oferece ferramentas práticas para acompanhar sua jornada
          de bem-estar, com meditações guiadas, exercícios de autocuidado e
          muito mais. Tudo na palma da sua mão.
        </p>
        <p>
          Estamos trabalhando para trazer a experiência também para iOS. Em
          breve, todos poderão se beneficiar do nosso app, independentemente do
          sistema operacional.
        </p>
      </div>
      <div className="divOs">
        {osInfo.map(({ id, name, img, url }) => {
          return <Os key={id} title={name} img={img} url={url} />;
        })}
      </div>
    </section>
  );
}
