//importação de bibliotecas
import { useEffect, useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import "./App.css";

import Modal from "./components/modal";
import Header from "./components/header";
import Footer from "./components/footer";

import Home from "./pages/home";
import Admin from "./pages/admin";
import axios from "./service/api";
import Lost from "./pages/lost";
import { getItem } from "./functions/token";

//incialização do react
function App() {
  const [modal, setModal] = useState(false);
  const [contato, setContato] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);

  //verifica se o token é válido e se o usuário é admin
  useEffect(() => {
    const token = getItem("token");

    //função para verificar o token
    async function verifyToken() {
      try {
        const response = await axios.get("auth/verify", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log({ response });

        setIsAdmin(response.data.verifyToken);
      } catch (error) {
        console.log(error);
        setIsAdmin(false);
      }
    }

    if (token) {
      verifyToken();
    }
  }, []);

  return (
    //criação de rotas
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <Header setModal={setModal} isAdmin={isAdmin} />
              <Home />
              <Footer setModal={setContato} />
              {modal && <Modal setModal={setModal} contato={false} />}
              {contato && <Modal setModal={setContato} contato={true} />}
            </>
          }
        />
        <Route
          path="/admin"
          element={
            isAdmin ? (
              <Admin setIsAdmin={setIsAdmin} />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />
        <Route path="lostpassword/:id/:token" element={<Lost />} />
      </Routes>
    </Router>
  );
}

export default App;
