
# 🌐 Documentação da API - Sistema de Biblioteca

## 📡 Visão Geral

O Sistema de Biblioteca oferece uma API REST completa para gerenciamento de livros, usuários, empréstimos e reservas. Esta documentação detalha todos os endpoints disponíveis, parâmetros, respostas e códigos de erro.

**Base URL**: `http://localhost:5000`
**Formato**: JSON
**Autenticação**: Session-based

## 🔐 Autenticação

### POST /login
Autentica um usuário no sistema.

**Parâmetros**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "user": {
    "id": 1,
    "username": "admin",
    "firstName": "Gabriel",
    "lastName": "Santos",
    "role": "ADMIN",
    "email": "gabriel@biblioteca.com"
  },
  "sessionId": "ABC123XYZ"
}
```

**Resposta de Erro (401)**:
```json
{
  "success": false,
  "error": "Credenciais inválidas"
}
```

### POST /logout
Encerra a sessão do usuário atual.

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Logout realizado com sucesso"
}
```

---

## 📚 Endpoints de Livros

### GET /books
Lista todos os livros cadastrados.

**Parâmetros de Query**:
- `page` (opcional): Número da página (padrão: 1)
- `limit` (opcional): Itens por página (padrão: 20)
- `category` (opcional): Filtrar por categoria
- `author` (opcional): Filtrar por autor
- `available` (opcional): Apenas livros disponíveis (true/false)

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "isbn": "978-85-7522-447-7",
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "publisher": "Alta Books",
      "publishDate": "2019-07-30",
      "category": "Programação",
      "totalCopies": 5,
      "availableCopies": 3,
      "description": "Manual do código limpo",
      "status": "AVAILABLE"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 45,
    "totalPages": 3
  }
}
```

### GET /books/{id}
Busca um livro específico por ID.

**Resposta (200)**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "isbn": "978-85-7522-447-7",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "publisher": "Alta Books",
    "publishDate": "2019-07-30",
    "category": "Programação",
    "totalCopies": 5,
    "availableCopies": 3,
    "description": "Manual do código limpo",
    "status": "AVAILABLE"
  }
}
```

**Resposta de Erro (404)**:
```json
{
  "success": false,
  "error": "Livro não encontrado"
}
```

### POST /books
Cadastra um novo livro (requer permissão ADMIN).

**Parâmetros**:
```json
{
  "isbn": "978-85-7522-447-7",
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publisher": "Alta Books",
  "publishDate": "2019-07-30",
  "category": "Programação",
  "totalCopies": 5,
  "description": "Manual do código limpo"
}
```

**Resposta (201)**:
```json
{
  "success": true,
  "data": {
    "id": 15,
    "message": "Livro cadastrado com sucesso"
  }
}
```

### PUT /books/{id}
Atualiza um livro existente (requer permissão ADMIN).

**Parâmetros**: Mesmos do POST

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Livro atualizado com sucesso"
}
```

### DELETE /books/{id}
Remove um livro (requer permissão ADMIN).

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Livro removido com sucesso"
}
```

### GET /books/search
Busca livros por critérios específicos.

**Parâmetros de Query**:
- `q`: Termo de busca (título, autor, ISBN)
- `category`: Categoria específica
- `year`: Ano de publicação

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "relevanceScore": 0.95
    }
  ],
  "searchTerm": "clean",
  "resultCount": 1
}
```

---

## 👥 Endpoints de Usuários

### GET /users
Lista todos os usuários (requer permissão ADMIN).

**Parâmetros de Query**:
- `page` (opcional): Número da página
- `limit` (opcional): Itens por página
- `role` (opcional): Filtrar por perfil (ADMIN/USER)
- `status` (opcional): Filtrar por status (ACTIVE/INACTIVE)

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "username": "gabriel.santos",
      "firstName": "Gabriel",
      "lastName": "Santos",
      "email": "gabriel@biblioteca.com",
      "phone": "(11) 99999-9999",
      "role": "ADMIN",
      "registrationDate": "2024-01-15",
      "status": "ACTIVE",
      "maxBooks": 10,
      "currentLoans": 2
    }
  ]
}
```

### GET /users/{id}
Busca um usuário específico.

**Resposta (200)**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "gabriel.santos",
    "firstName": "Gabriel",
    "lastName": "Santos",
    "email": "gabriel@biblioteca.com",
    "phone": "(11) 99999-9999",
    "address": "Rua das Flores, 123",
    "role": "ADMIN",
    "registrationDate": "2024-01-15",
    "status": "ACTIVE",
    "maxBooks": 10,
    "currentLoans": 2,
    "totalLoans": 25,
    "fines": 0.00
  }
}
```

### POST /users
Cadastra um novo usuário.

**Parâmetros**:
```json
{
  "username": "joao.silva",
  "password": "senha123",
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@email.com",
  "phone": "(11) 88888-8888",
  "address": "Av. Principal, 456",
  "role": "USER"
}
```

**Resposta (201)**:
```json
{
  "success": true,
  "data": {
    "id": 10,
    "message": "Usuário cadastrado com sucesso"
  }
}
```

### PUT /users/{id}
Atualiza dados de um usuário.

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Usuário atualizado com sucesso"
}
```

### DELETE /users/{id}
Remove um usuário (requer permissão ADMIN).

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Usuário removido com sucesso"
}
```

---

## 📋 Endpoints de Empréstimos

### GET /loans
Lista todos os empréstimos.

**Parâmetros de Query**:
- `status`: ACTIVE, RETURNED, OVERDUE
- `userId`: Empréstimos de um usuário específico
- `bookId`: Empréstimos de um livro específico
- `startDate`: Data inicial
- `endDate`: Data final

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "userId": 5,
      "userName": "João Silva",
      "bookId": 12,
      "bookTitle": "Clean Code",
      "loanDate": "2024-01-15",
      "dueDate": "2024-01-29",
      "returnDate": null,
      "fine": 0.00,
      "status": "ACTIVE",
      "daysOverdue": 0
    }
  ]
}
```

### GET /loans/{id}
Busca um empréstimo específico.

**Resposta (200)**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "user": {
      "id": 5,
      "name": "João Silva",
      "email": "joao@email.com"
    },
    "book": {
      "id": 12,
      "title": "Clean Code",
      "author": "Robert C. Martin"
    },
    "loanDate": "2024-01-15",
    "dueDate": "2024-01-29",
    "returnDate": null,
    "fine": 0.00,
    "status": "ACTIVE",
    "daysOverdue": 0
  }
}
```

### POST /loans
Realiza um novo empréstimo.

**Parâmetros**:
```json
{
  "userId": 5,
  "bookId": 12,
  "loanPeriod": 14
}
```

**Resposta (201)**:
```json
{
  "success": true,
  "data": {
    "loanId": 25,
    "dueDate": "2024-02-15",
    "message": "Empréstimo realizado com sucesso"
  }
}
```

**Resposta de Erro (400)**:
```json
{
  "success": false,
  "error": "Livro não está disponível para empréstimo"
}
```

### PUT /loans/{id}/return
Processa a devolução de um livro.

**Parâmetros (opcional)**:
```json
{
  "condition": "GOOD|DAMAGED|LOST",
  "notes": "Observações sobre a devolução"
}
```

**Resposta (200)**:
```json
{
  "success": true,
  "data": {
    "returnDate": "2024-01-25",
    "fine": 0.00,
    "message": "Devolução processada com sucesso"
  }
}
```

### GET /loans/overdue
Lista empréstimos em atraso.

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 8,
      "userId": 3,
      "userName": "Maria Santos",
      "bookTitle": "Java: The Complete Reference",
      "dueDate": "2024-01-10",
      "daysOverdue": 15,
      "fine": 15.00,
      "status": "OVERDUE"
    }
  ]
}
```

---

## 🔖 Endpoints de Reservas

### GET /reservations
Lista todas as reservas.

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "userId": 7,
      "userName": "Ana Costa",
      "bookId": 15,
      "bookTitle": "Design Patterns",
      "reservationDate": "2024-01-20",
      "status": "ACTIVE",
      "priority": 1,
      "estimatedAvailability": "2024-02-05"
    }
  ]
}
```

### POST /reservations
Cria uma nova reserva.

**Parâmetros**:
```json
{
  "userId": 7,
  "bookId": 15
}
```

**Resposta (201)**:
```json
{
  "success": true,
  "data": {
    "reservationId": 12,
    "priority": 3,
    "estimatedAvailability": "2024-02-15",
    "message": "Reserva criada com sucesso"
  }
}
```

### DELETE /reservations/{id}
Cancela uma reserva.

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Reserva cancelada com sucesso"
}
```

---

## 📊 Endpoints de Relatórios

### GET /reports/dashboard
Retorna dados do dashboard principal.

**Resposta (200)**:
```json
{
  "success": true,
  "data": {
    "totalBooks": 150,
    "totalUsers": 85,
    "activeLoans": 42,
    "overdueLoans": 8,
    "totalReservations": 15,
    "todayReturns": 5,
    "todayLoans": 12,
    "popularBooks": [
      {
        "bookId": 12,
        "title": "Clean Code",
        "loanCount": 25
      }
    ],
    "recentActivity": [
      {
        "type": "LOAN",
        "description": "João Silva emprestou 'Clean Code'",
        "timestamp": "2024-01-25T14:30:00"
      }
    ]
  }
}
```

### GET /reports/books/popular
Lista os livros mais emprestados.

**Parâmetros de Query**:
- `limit`: Quantidade de livros (padrão: 10)
- `period`: WEEK, MONTH, YEAR, ALL (padrão: MONTH)

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "bookId": 12,
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "loanCount": 25,
      "category": "Programação"
    }
  ],
  "period": "MONTH",
  "totalBooks": 10
}
```

### GET /reports/users/active
Lista usuários mais ativos.

**Resposta (200)**:
```json
{
  "success": true,
  "data": [
    {
      "userId": 5,
      "userName": "João Silva",
      "totalLoans": 18,
      "currentLoans": 3,
      "averageLoanPeriod": 12
    }
  ]
}
```

---

## ❌ Códigos de Erro

| Código | Descrição | Exemplo |
|--------|-----------|---------|
| **400** | Bad Request | Parâmetros inválidos |
| **401** | Unauthorized | Não autenticado |
| **403** | Forbidden | Sem permissão |
| **404** | Not Found | Recurso não encontrado |
| **409** | Conflict | ISBN já existe |
| **422** | Unprocessable Entity | Dados inválidos |
| **500** | Internal Server Error | Erro do servidor |

### Formato de Erro Padrão
```json
{
  "success": false,
  "error": "Descrição do erro",
  "code": "ERROR_CODE",
  "details": {
    "field": "Campo específico com erro",
    "message": "Mensagem detalhada"
  },
  "timestamp": "2024-01-25T14:30:00Z"
}
```

---

## 🔧 Headers Necessários

### Requisições Autenticadas
```
Authorization: Bearer {sessionToken}
Content-Type: application/json
Accept: application/json
```

### Todas as Requisições
```
Content-Type: application/json
Accept: application/json
X-Requested-With: XMLHttpRequest
```

---

## 📝 Exemplos de Uso

### Fluxo Completo de Empréstimo
```javascript
// 1. Fazer login
const loginResponse = await fetch('/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

// 2. Buscar livros disponíveis
const booksResponse = await fetch('/books?available=true');
const books = await booksResponse.json();

// 3. Realizar empréstimo
const loanResponse = await fetch('/loans', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    userId: 5,
    bookId: 12,
    loanPeriod: 14
  })
});
```

---

## 🚀 Rate Limiting

- **Geral**: 100 requisições por minuto
- **Login**: 5 tentativas por minuto
- **Busca**: 50 requisições por minuto

## 📱 Versionamento

**Versão Atual**: v1  
**URL Base**: `/api/v1/`

---

**Desenvolvido por Gabriel Santos do Nascimento**  
📧 gabriel.santos@estudante.faculdade.edu.br
