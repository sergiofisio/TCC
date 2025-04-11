import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function ChartsView({ users }) {
  const chartData = [
    {
      name: "Total",
      total: users.length,
      ativos: users.filter((u) => u.active_user).length,
      inativos: users.filter((u) => !u.active_user).length,
    },
  ];

  return (
    <>
      <h3>Estatísticas</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="total" fill="#8884d8" />
          <Bar dataKey="ativos" fill="#82ca9d" />
          <Bar dataKey="inativos" fill="#f56565" />
        </BarChart>
      </ResponsiveContainer>
    </>
  );
}
