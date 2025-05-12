import { useEffect, useState } from "react";
import Sidebar from "../../components/sidebar";
import UsersView from "../../components/views/users";
import UserModal from "../../components/modal/user";
import axios from "./../../service/api";
import { toastFail } from "../../functions/toast";
import ClipLoader from "react-spinners/ClipLoader";
import "./style.css";
import ChartsView from "../../components/views/charts";
import { getItem } from "../../functions/token";

// Função principal que renderiza o componente Dashboard
export default function Dashboard({ setIsAdmin }) {
  // Hooks para gerenciar o estado do componente
  const [view, setView] = useState("users");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState("");
  const [selectedUser, setSelectedUser] = useState(null);

  // Função para buscar todos os usuários
  async function getAllUsers() {
    setLoading(true);
    try {
      const response = await axios.get("/user/admin/all", {
        headers: {
          Authorization: `Bearer ${getItem("token")}`,
        },
      });
      setUsers(response.data);
    } catch (error) {
      toastFail("Erro ao buscar usuários", 3000, "top-right");
      console.log(error);
    } finally {
      setLoading(false);
    }
  }

  // Efeito colateral para buscar usuários quando o componente é montado
  useEffect(() => {
    getAllUsers();
  }, []);

  // Função para lidar com ações de editar ou excluir usuários
  const handleAction = (type, user) => {
    const userInfo = users.find((u) => u.id_user === user.id_user);
    setSelectedUser(userInfo);
    setModalAction(type);
    setModalOpen(true);
  };

  // Função para lidar com a ação de salvar as alterações no usuário
  const handleSave = async (id, data) => {
    setUsers((prevUsers) =>
      prevUsers.map((user) =>
        user.id_user === id ? { ...user, ...data } : user
      )
    );
    const response = await axios.delete(`/auth/user/${id}`, {
      headers: {
        Authorization: `Bearer ${getItem("token")}`,
      },
      data: {
        active: false,
      },
    });
    console.log({ response });

    setModalOpen(false);
  };

  // renderiza o componente Dashboard
  return (
    <div className="dashboard">
      <Sidebar setView={setView} setIsAdmin={setIsAdmin} />

      <main className="dashboard-content">
        {loading ? (
          <div className="spinner-container">
            <ClipLoader color="#000" loading={loading} size={50} />
          </div>
        ) : (
          <>
            {view === "users" ? (
              <UsersView users={users} action={handleAction} />
            ) : (
              <ChartsView users={users} />
            )}
          </>
        )}

        <UserModal
          isOpen={modalOpen}
          onClose={() => setModalOpen(false)}
          user={selectedUser}
          action={modalAction}
          onSave={handleSave}
        />
      </main>
    </div>
  );
}
