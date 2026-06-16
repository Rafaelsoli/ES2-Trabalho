import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

// ========== PROFISSIONAIS ==========
// Ajustado para '/profissionals' para bater com o Java
export const RiverService = { // ou profissionalService
  listar: () => api.get('/profissionals'),
  buscar: (id) => api.get(`/profissionals/${id}`),
  criar: (profissional) => api.post('/profissionals', profissional),
  atualizar: (id, profissional) => api.put(`/profissionals/${id}`, profissional),
  deletar: (id) => api.delete(`/profissionals/${id}`)
};

// Vincula as duas formas de escrita para que tanto o Form quanto o List funcionem!
export const profissionalService = RiverService;
export const ProfissionalService = RiverService;

// ========== COMPROMISSOS (DEV 2 - Bruno) ==========
export const compromissoService = {
  listar: () => api.get('/compromissos'),
  buscar: (id) => api.get(`/compromissos/${id}`),
  criar: (compromisso) => api.post('/compromissos', compromisso),
  atualizar: (id, compromisso) => api.put(`/compromissos/${id}`, compromisso),
  deletar: (id) => api.delete(`/compromissos/${id}`)
};

export default api;
