import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameLabService, atendimentoService } from '../services/api';

function ExameLabForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  
  const [exame, setExame] = useState({
    descricao: '',
    atendimento: null
  });
  const [atendimentos, setAtendimentos] = useState([]);

  useEffect(() => {
    // Carrega os atendimentos para preencher a seleção vinculada
    atendimentoService.listar().then(res => setAtendimentos(res.data));

    // Se for edição, busca os dados do exame laboratorial existente
    if (id) {
      exameLabService.buscar(id).then(res => setExame(res.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await exameLabService.atualizar(id, exame);
      } else {
        await exameLabService.criar(exame);
      }
      navigate('/exames-lab');
    } catch (error) {
      console.error('Erro ao salvar exame laboratorial:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Exame Laboratorial' : 'Novo Exame Laboratorial'}</h2>
      <form onSubmit={handleSubmit} className="form">
        
        <div className="form-group">
          <label>Descrição do Exame *</label>
          <textarea 
            value={exame.descricao} 
            required
            onChange={e => setExame({ ...exame, descricao: e.target.value })} 
            placeholder="Descreva as especificações do exame laboratorial solicitado..."
          />
        </div>

        <div className="form-group">
          <label>Atendimento Vinculado *</label>
          <select 
            value={exame.atendimento?.id || ''} 
            required
            onChange={e => setExame({
              ...exame,
              atendimento: e.target.value ? { id: parseInt(e.target.value) } : null
            })}
          >
            <option value="">Selecione um atendimento</option>
            {atendimentos.map(atend => (
              <option key={atend.id} value={atend.id}>
                Atendimento #{atend.id} — {atend.data} ({atend.profissionalDeSaude?.nome || 'Sem profissional'})
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/exames-lab')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ExameLabForm;