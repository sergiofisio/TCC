//importando react e react-dom
import { createRoot } from "react-dom/client";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import App from "./App.jsx";
import "./index.css";

//inicialização do react
createRoot(document.getElementById("root")).render(
  <>
    <App />
    <ToastContainer
      autoClose={3000}
      hideProgressBar={true}
      closeOnClick={true}
      pauseOnHover={false}
      draggable={false}
      theme="colored"
    />
  </>
);
