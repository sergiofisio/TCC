import Card from "./../../card/index";
import Table from "./../../table/index";

//função para criação do componente de visualização de usuários
export default function UsersView({ users, action }) {
  //filtro de usuários ativos e inativos
  const active = users.filter((u) => u.active_user);
  const inactive = users.filter((u) => !u.active_user);

  return (
    <>
      <h3>Painel de Usuários</h3>
      <div className="cards-container">
        <Card description="Total Usuários" number={users.length} />
        <Card description="Usuários Ativos" number={active.length} />
        <Card description="Usuários Inativos" number={inactive.length} />
      </div>
      <div className="users-container">
        <Table description="Usuários Ativos" users={active} action={action} />
        <Table
          description="Usuários Inativos"
          users={inactive}
          action={action}
        />
      </div>
    </>
  );
}
