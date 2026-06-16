import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  
  const [atendimento, setAtendimento] = useState({
    data: '', 
    horario: '', 
    problemaTexto: '', 
    receitaSaude: '', 
    profissionalDeSaude: null
  });
  
  const [profissionais, setProfissionais] = useState([]);
  const [categoriaSelecionada, setCategoriaSelecionada] = useState('');

  useEffect(() => {
    // Carrega a lista de profissionais para o Select
    profissionalService.listar().then(res => setProfissionais(res.data));

    // Se for edição, busca os dados do atendimento
    if (id) {
      atendimentoService.buscar(id).then(res => {
        setAtendimento(res.data);
        if (res.data.profissionalDeSaude) {
          setCategoriaSelecionada(res.data.profissionalDeSaude.categoria || '');
        }
      });
    }
  }, [id]);

  // Gerencia a troca do profissional e mapeia sua categoria
  const handleProfissionalChange = (e) => {
    const profId = e.target.value ? parseInt(e.target.value) : null;
    const profSelecionado = profissionais.find(p => p.id === profId);

    setAtendimento({
      ...atendimento,
      profissionalDeSaude: profSelecionado ? { id: profId } : null
    });

    // Atualiza a categoria para modificar o campo de receita dinamicamente
    setCategoriaSelecionada(profSelecionado ? profSelecionado.categoria : '');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await atendimentoService.atualizar(id, atendimento);
      } else {
        await atendimentoService.criar(atendimento);
      }
      navigate('/atendimentos');
    } catch (error) {
      console.error('Erro ao salvar atendimento:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Atendimento' : 'Novo Atendimento'}</h2>
      <form onSubmit={handleSubmit} className="form">
        
        <div className="form-group">
          <label>Data *</label>
          <input type="date" value={atendimento.data} required
            onChange={e => setAtendimento({...atendimento, data: e.target.value})} />
        </div>

        <div className="form-group">
          <label>Horário *</label>
          <input type="time" value={atendimento.horario} required
            onChange={e => setAtendimento({...atendimento, horario: e.target.value})} />
        </div>

        <div className="form-group">
          <label>Problema Relatado *</label>
          <textarea value={atendimento.problemaTexto} required
            onChange={e => setAtendimento({...atendimento, problemaTexto: e.target.value})} 
            placeholder="Descreva os sintomas ou o problema relatado..." />
        </div>

        <div className="form-group">
          <label>Profissional de Saúde Vinculado *</label>
          <select value={atendimento.profissionalDeSaude?.id || ''} required
            onChange={handleProfissionalChange}>
            <option value="">Selecione um profissional</option>
            {profissionais.map(p => (
              <option key={p.id} value={p.id}>{p.nome} ({p.categoria})</option>
            ))}
          </select>
        </div>

        {/* Campo de receita dinâmico com base na categoria do profissional */}
        <div className="form-group">
          <label>
            {categoriaSelecionada 
              ? `Receita de Saúde — Especificação para ${categoriaSelecionada}` 
              : 'Receita de Saúde'}
          </label>
          <textarea value={atendimento.receitaSaude}
            onChange={e => setAtendimento({...atendimento, receitaSaude: e.target.value})} 
            placeholder={categoriaSelecionada 
              ? `Insira as recomendações e prescrições adequadas para um(a) ${categoriaSelecionada}...` 
              : 'Selecione um profissional para liberar as diretrizes de receita.'}
            disabled={!categoriaSelecionada} />
        </div>

        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
      </form>
    </div>
  );
}

export default AtendimentoForm;