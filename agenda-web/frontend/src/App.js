import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProfissionalList from './components/ProfissionalList';
import ProfissionalForm from './components/ProfissionalForm';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1>📅 Agenda Web</h1>
          <div className="nav-links">
            {/* Link atualizado para apontar para Profissionais */}
            <Link to="/profissionais">Profissionais</Link>
          </div>
        </nav>

        <main className="container">
          <Routes>
            {/* A rota inicial "/" agora carrega a lista de profissionais */}
            <Route path="/" element={<ProfissionalList />} />
            
            {/* Rotas de Profissionais (maiuscula e minuscula mapeadas por precaução) */}
            <Route path="/profissionais" element={<ProfissionalList />} />
            <Route path="/Profissionais" element={<ProfissionalList />} />
            
            <Route path="/profissionais/novo" element={<ProfissionalForm />} />
            
            <Route path="/profissionais/editar/:id" element={<ProfissionalForm />} />
            <Route path="/Profissionais/editar/:id" element={<ProfissionalForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
