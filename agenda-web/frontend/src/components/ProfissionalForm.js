import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ProfissionalService } from '../services/api';

function ProfissionalForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [Profissional, setProfissional] = useState({
    nome: '', telefone: '', email: '', endereco: '', categoria: ''
  });

  useEffect(() => {
    if (id) {
      ProfissionalService.buscar(id).then(res => setProfissional(res.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await ProfissionalService.atualizar(id, Profissional);
      } else {
        await ProfissionalService.criar(Profissional);
      }
      navigate('/profissionais');
    } catch (error) {
      console.error('Erro ao salvar Profissional:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Profissional' : 'Novo Profissional'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Nome *</label>
          <input type="text" value={Profissional.nome} required
            onChange={e => setProfissional({...Profissional, nome: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Telefone</label>
          <input type="text" value={Profissional.telefone}
            onChange={e => setProfissional({...Profissional, telefone: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Email</label>
          <input type="email" value={Profissional.email}
            onChange={e => setProfissional({...Profissional, email: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Endereço</label>
          <input type="text" value={Profissional.endereco}
            onChange={e => setProfissional({...Profissional, endereco: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Categoria *</label>
          <input type="text" value={Profissional.categoria} required
            onChange={e => setProfissional({...Profissional, categoria: e.target.value})} 
            placeholder="Ex: Cardiologista, Dentista..." />
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/profissionais')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ProfissionalForm;
