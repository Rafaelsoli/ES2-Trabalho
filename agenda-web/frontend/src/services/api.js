import axios from 'axios';

// Resolve a URL base da API.
function resolveApiUrl() {
  const raw = process.env.REACT_APP_API_URL;
  if (!raw) return 'http://localhost:8080/api';
  let url = raw.trim();
  if (!/^https?:\/\//i.test(url)) {
    url = `https://${url}`;
  }
  url = url.replace(/\/+$/, '');
  if (!/\/api$/i.test(url)) {
    url = `${url}/api`;
  }
  return url;
}

const API_URL = resolveApiUrl();

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

// ========== PROFISSIONAIS DE SAÚDE ==========
export const profissionalService = {
  listar: () => api.get('/profissionais'),
  buscar: (id) => api.get(`/profissionais/${id}`),
  criar: (profissional) => api.post('/profissionais', profissional),
  atualizar: (id, profissional) => api.put(`/profissionais/${id}`, profissional),
  deletar: (id) => api.delete(`/profissionais/${id}`)
};

// ========== ATENDIMENTOS ==========
export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  criar: (atendimento) => api.post('/atendimentos', atendimento),
  atualizar: (id, atendimento) => api.put(`/atendimentos/${id}`, atendimento),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

// ========== EXAMES LABORATORIAIS (CORRIGIDO SEM O 'N') ==========
export const exameLabService = { 
  listar: (params) => api.get('/exames-lab', { params }),
  buscar: (id) => api.get(`/exames-lab/${id}`),
  criar: (exameLab) => api.post('/exames-lab', exameLab),
  atualizar: (id, exameLab) => api.put(`/exames-lab/${id}`, exameLab),
  deletar: (id) => api.delete(`/exames-lab/${id}`)
};

export default api;
