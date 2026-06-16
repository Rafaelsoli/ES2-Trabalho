import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

// Importações dos componentes de Profissionais
import ProfissionalList from './components/ProfissionalList';
import ProfissionalForm from './components/ProfissionalForm';

// Importações dos componentes de Atendimentos
import AtendimentoList from './components/AtendimentoList';
import AtendimentoForm from './components/AtendimentoForm';

// Importações dos componentes de Exames Laboratoriais
import ExameLabList from './components/ExameLabList';
import ExameLabForm from './components/ExameLabForm';

import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1>🏥 Clinica Médica</h1>
          <div className="nav-links">
            <Link to="/profissionais">Profissionais</Link>
            <Link to="/atendimentos">Atendimentos</Link>
            <Link to="/exames-lab">Exames Laboratoriais</Link>
          </div>
        </nav>

        <main className="container">
          <Routes>
            {/* Rota inicial padrão redirecionando para a listagem de profissionais */}
            <Route path="/" element={<ProfissionalList />} />

            {/* Rotas da entidade Profissional */}
            <Route path="/profissionais" element={<ProfissionalList />} />
            <Route path="/profissionais/novo" element={<ProfissionalForm />} />
            <Route path="/profissionais/editar/:id" element={<ProfissionalForm />} />

            {/* Rotas da entidade Atendimento */}
            <Route path="/atendimentos" element={<AtendimentoList />} />
            <Route path="/atendimentos/novo" element={<AtendimentoForm />} />
            <Route path="/atendimentos/editar/:id" element={<AtendimentoForm />} />

            {/* Rotas da entidade ExameLab */}
            <Route path="/exames-lab" element={<ExameLabList />} />
            <Route path="/exames-lab/novo" element={<ExameLabForm />} />
            <Route path="/exames-lab/editar/:id" element={<ExameLabForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;