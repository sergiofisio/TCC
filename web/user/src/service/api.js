import axios from "axios";

// Cria uma instância do axios com a URL base e o tempo limite
export default axios.create({
  baseURL: import.meta.env.VITE_URL_DEPLOY,
  timeout: 200000,
  headers: { "Content-Type": "application/json" },
});
