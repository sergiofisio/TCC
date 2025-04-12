import { useEffect, useState } from "react";
import Sidebar from "../../components/sidebar";
import UsersView from "../../components/views/users";
import UserModal from "../../components/modal/user";
import axios from "./../../service/api";
import { toastFail } from "../../functions/toast";
import ClipLoader from "react-spinners/ClipLoader";
import "./style.css";
import ChartsView from "../../components/views/charts";

export default function Dashboard({ setIsAdmin }) {
  const [view, setView] = useState("users");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState("");
  const [selectedUser, setSelectedUser] = useState(null);

  async function getAllUsers() {
    setLoading(true);
    try {
      const response = await axios.get("/auth/infoDb", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setUsers(response.data.users);
    } catch (error) {
      toastFail("Erro ao buscar usuários", 3000, "top-right");
      console.log(error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    getAllUsers();
  }, []);

  const handleAction = (type, user) => {
    const userInfo = users.find((u) => u.id_user === user.id_user);
    setSelectedUser(userInfo);
    setModalAction(type);
    setModalOpen(true);
  };

  const handleSave = async (id, data) => {
    setUsers((prevUsers) =>
      prevUsers.map((user) =>
        user.id_user === id ? { ...user, ...data } : user
      )
    );
    const response = await axios.delete(`/auth/user/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      data: {
        active: false,
      },
    });
    console.log({ response });

    setModalOpen(false);
  };

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
