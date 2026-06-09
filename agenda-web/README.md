# Agenda Web - DEV 1 (Ana) - Módulo de Contatos

## Branch: `feature/contatos`

Este é o código da **Desenvolvedora 1 (Ana)**, responsável pelo **CRUD de Contatos**.

## O que este módulo contém

### Backend (Java / Spring Boot)
- `Contato.java` — Entity JPA (modelo de dados)
- `ContatoRepository.java` — Interface de acesso ao banco (Spring Data JPA)
- `ContatoController.java` — REST API com endpoints CRUD
- `ContatoControllerTest.java` — Testes unitários (JUnit 5 + Mockito)

### Frontend (React)
- `ContatoList.js` — Componente de listagem de contatos
- `ContatoForm.js` — Formulário de criação/edição de contato
- `api.js` — Serviço de comunicação com o backend (Axios)

### Endpoints da API
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /api/contatos | Listar todos os contatos |
| GET | /api/contatos/{id} | Buscar contato por ID |
| POST | /api/contatos | Criar novo contato |
| PUT | /api/contatos/{id} | Atualizar contato |
| DELETE | /api/contatos/{id} | Excluir contato |

## Como executar

```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend
cd frontend
npm install
npm start
```

## Como rodar os testes

```bash
cd backend
mvn test -Dtest="ContatoControllerTest"
```

## Comandos Git para Ana

```bash
git checkout -b feature/contatos
# ... desenvolver ...
git add .
git commit -m "feat: implementa CRUD de contatos (Spring Boot + React)"
git push origin feature/contatos
# Abrir Pull Request no GitHub
```
