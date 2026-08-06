import axios from "axios";

const api = axios.create({
  baseURL: "http://10.9.77.127:8082/api",
});

export default api;