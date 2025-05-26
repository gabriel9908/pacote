# 📋 Documentação Técnica - Sistema de Biblioteca

## 🏗️ Arquitetura do Sistema

### Padrão Arquitetural
O sistema segue o padrão **MVC (Model-View-Controller)** com as seguintes camadas:

- **Model**: Classes de entidade (`Book`, `User`, `Loan`, `Reservation`)
- **View**: Páginas JSP para interface do usuário
- **Controller**: Servlets para processamento de requisições

### Diagrama de Arquitetura
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Controller    │    │     Model       │
│   (JSP/HTML)    │◄──►│   (Servlets)     │◄──►│   (Entities)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │       DAO        │
                       │ (Data Access)    │
                       └──────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │   H2 Database    │
                       │   (In-Memory)    │
                       └──────────────────┘
```

## 📦 Componentes Principais

### 1. Modelos de Dados

#### Book.java
```java
// Representa um livro no sistema
- id: int (chave primária)
- isbn: String (código único)
- title: String
- author: String
- publisher: String
- publishDate: LocalDate
- category: String
- totalCopies: int
- availableCopies: int
- description: String
- status: String
```

#### User.java
```java
// Representa usuários do sistema
- id: int
- username: String (único)
- password: String (hash)
- firstName: String
- lastName: String
- email: String (único)
- phone: String
- address: String
- role: String (ADMIN/USER)
- registrationDate: LocalDate
- status: String (ACTIVE/INACTIVE)
- maxBooks: int (limite de empréstimos)
```

#### Loan.java
```java
// Representa empréstimos de livros
- id: int
- userId: int (FK)
- bookId: int (FK)
- loanDate: LocalDate
- dueDate: LocalDate
- returnDate: LocalDate
- fine: double
- status: String (ACTIVE/RETURNED/OVERDUE)
```

### 2. Camada DAO (Data Access Object)

#### BookDAO.java
```java
- addBook(Book book): boolean
- getBookById(int id): Book
- getAllBooks(): List<Book>
- updateBook(Book book): boolean
- deleteBook(int id): boolean
- searchBooks(String query): List<Book>
- updateAvailability(int bookId, int change): boolean
```

#### UserDAO.java
```java
- addUser(User user): boolean
- getUserById(int id): User
- getUserByUsername(String username): User
- getAllUsers(): List<User>
- updateUser(User user): boolean
- deleteUser(int id): boolean
- authenticateUser(String username, String password): User
```

#### LoanDAO.java
```java
- addLoan(Loan loan): boolean
- getLoanById(int id): Loan
- getAllLoans(): List<Loan>
- getLoansByUserId(int userId): List<Loan>
- getActiveLoanCount(int userId): int
- hasActiveLoan(int userId, int bookId): boolean
- returnBook(int loanId): boolean
- getOverdueLoans(): List<Loan>
- calculateFine(Loan loan): double
```

### 3. Servlets (Controladores)

#### BookServlet.java
**Endpoint**: `/books/*`
- `GET /books/` - Lista todos os livros
- `GET /books/add` - Formulário de adição
- `POST /books/add` - Adiciona novo livro
- `GET /books/edit?id=X` - Formulário de edição
- `POST /books/edit` - Atualiza livro
- `GET /books/delete?id=X` - Remove livro
- `GET /books/search?q=termo` - Busca livros

#### LoanServlet.java
**Endpoint**: `/loans/*`
- `GET /loans/` - Lista empréstimos
- `GET /loans/issue` - Formulário de empréstimo
- `POST /loans/issue` - Processa empréstimo
- `GET /loans/return` - Formulário de devolução
- `POST /loans/return` - Processa devolução
- `GET /loans/overdue` - Lista atrasos

#### UserServlet.java
**Endpoint**: `/users/*`
- `GET /users/` - Lista usuários
- `GET /users/add` - Formulário de cadastro
- `POST /users/add` - Adiciona usuário
- `GET /users/profile?id=X` - Perfil do usuário

### 4. Filtros de Segurança

#### AuthenticationFilter.java
- Verifica autenticação em rotas protegidas
- Redireciona para login se não autenticado
- Aplica-se a: `/admin/*`, `/books/*`, `/loans/*`, `/users/*`

#### CharacterEncodingFilter.java
- Define codificação UTF-8 para todas as requisições
- Garante suporte a caracteres especiais

## 🗄️ Banco de Dados

### Schema das Tabelas

```sql
-- Tabela de Usuários
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) DEFAULT 'USER',
    registration_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    max_books INT DEFAULT 5
);

-- Tabela de Livros
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    publish_date DATE,
    category VARCHAR(50),
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    description TEXT,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

-- Tabela de Empréstimos
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Tabela de Reservas
CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    reservation_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    priority INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

### Relacionamentos
- **User** 1:N **Loan** (Um usuário pode ter vários empréstimos)
- **Book** 1:N **Loan** (Um livro pode ser emprestado várias vezes)
- **User** 1:N **Reservation** (Um usuário pode ter várias reservas)
- **Book** 1:N **Reservation** (Um livro pode ter várias reservas)

## 🔧 Configurações

### Configuração do Maven (pom.xml)
- **Java Version**: 11
- **Packaging**: WAR
- **Dependencies**: Servlet API, JSP API, JSTL, JSF, H2, MySQL, Tomcat Embed

### Configuração Web (web.xml)
```xml
- Servlet Mappings para todos os controladores
- Filter Mappings para segurança
- Welcome Files configuration
- Session timeout: 30 minutos
```

### Configuração do Banco (DatabaseConnection.java)
```java
DB_URL: "jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1"
DB_USER: "sa"
DB_PASSWORD: ""
```

## 🚀 Deploy e Execução

### Modo Desenvolvimento
```bash
mvn clean compile
java -cp "target/classes:target/dependency/*" com.library.Main
```

### Modo Produção
```bash
mvn clean package
docker build -t biblioteca .
docker run -p 5000:5000 biblioteca
```

## 🔐 Segurança

### Autenticação
- Senhas armazenadas com hash (implementar BCrypt em produção)
- Sessões com timeout de 30 minutos
- Filtro de autenticação em rotas protegidas

### Autorização
- Perfis: ADMIN (acesso total) e USER (acesso limitado)
- Controle de acesso baseado em roles
- Validação de permissões nos servlets

### Validações
- Validação de entrada nos formulários
- Sanitização de dados para evitar SQL Injection
- Controle de sessão ativa

## 📈 Performance

### Otimizações Implementadas
- Connection pooling com H2
- Prepared Statements para todas as queries
- Lazy loading em relacionamentos
- Cache de sessão para dados do usuário

### Métricas Esperadas
- Tempo de resposta: < 200ms para operações simples
- Capacidade: 100 usuários simultâneos
- Throughput: 1000 requisições/minuto

## 🧪 Testes

### Estratégia de Testes
- **Unit Tests**: DAOs e Servlets
- **Integration Tests**: Fluxos completos
- **Manual Tests**: Interface do usuário

### Cenários de Teste
1. Cadastro e autenticação de usuários
2. CRUD completo de livros
3. Fluxo de empréstimo e devolução
4. Cálculo de multas
5. Sistema de reservas

**GitHub:** [Sistema de Biblioteca](https://github.com/gabriel9908/pacote)