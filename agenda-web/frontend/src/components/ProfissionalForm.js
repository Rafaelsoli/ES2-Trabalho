import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [profissional, setProfissional] = useState({
    nome: '', telefone: '', endereco: '', categoria: ''
  });

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id)
        .then(res => setProfissional(res.data))
        .catch(err => console.error('Erro ao buscar profissional:', err));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await profissionalService.atualizar(id, profissional);
      } else {
        await profissionalService.criar(profissional);
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
          <input type="text" value={profissional.nome} required
            onChange={e => setProfissional({...profissional, nome: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Telefone</label>
          <input type="text" value={profissional.telefone}
            onChange={e => setProfissional({...profissional, telefone: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Endereço</label>
          <input type="text" value={profissional.endereco}
            onChange={e => setProfissional({...profissional, endereco: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Categoria *</label>
          <input type="text" value={profissional.categoria} required
            onChange={e => setProfissional({...profissional, categoria: e.target.value})} 
            placeholder="Ex: Cardiologista, Dentista..." />
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/profissionais')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ProfissionalForm;
