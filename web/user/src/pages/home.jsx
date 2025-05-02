import "./home.css";
import Section from "../components/section";
import imgHistory from "../assets/historia.svg";
import imgMission from "../assets/mission.svg";
import AppSection from "../components/app";

//informação de cada seção
const sections = [
  {
    id: 1,
    title: "NOSSA HISTÓRIA",
    text: "A EMOTION HARMONY  nasceu durante o processo do trabalho de conclusão do curso de Desenvolvimento de Sistemas da ETEC Lauro Gomes por uma necessidade do grupo para melhorar a saúde emocional da população Brasileiro, principalmente após a pandemia da COVID-19",
    img: imgHistory,
    className: "left",
  },
  {
    id: 2,
    title: "NOSSA MISSÃO",
    text: "A missão da EMOTION HARMONY é promover o bem-estar emocional e mental da população brasileira, ajudando as pessoas a encontrarem equilíbrio, felicidade e autoconhecimento. Acreditamos que, por meio da tecnologia e do desenvolvimento humano, podemos construir um mundo onde a saúde emocional seja prioridade. Nossa abordagem é centrada em oferecer ferramentas acessíveis e eficazes para que cada indivíduo possa superar desafios emocionais e viver uma vida plena e saudável.",
    img: imgMission,
    className: "right",
  },
];

// Função principal que renderiza a página inicial
export default function Home() {
  return (
    <main className="home">
      //renderiza cada seção
      {sections.map((section) => (
        <Section key={section.id} {...section} />
      ))}
      <AppSection />
    </main>
  );
}
