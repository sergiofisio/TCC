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

function App() {
  const [modal, setModal] = useState(false);
  const [isAdmin, setIsAdmin] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    async function verifyToken() {
      try {
        const response = await axios.get("auth/verify", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
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
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <Header setModal={setModal} isAdmin={isAdmin} />
              <Home />
              <Footer />
              {modal && <Modal setModal={setModal} />}
            </>
          }
        />
        <Route
          path="/admin"
          element={isAdmin ? <Admin /> : <Navigate to="/" replace />}
        />
      </Routes>
    </Router>
  );
}

export default App;
