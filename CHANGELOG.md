
# 📝 Changelog - Sistema de Biblioteca

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Planejado
- Sistema de notificações por email
- API REST completa
- Interface mobile nativa
- Integração com bibliotecas externas
- Sistema de avaliação de livros
- Relatórios avançados com gráficos

---

## [1.0.0] - 2024-01-25

### 🎉 Lançamento Inicial

**Desenvolvido por:** Gabriel Santos do Nascimento  
**Projeto:** Trabalho de Conclusão de Curso - Faculdade

### ✨ Adicionado
- **Sistema completo de gerenciamento de biblioteca**
- **Gestão de Livros**
  - Cadastro, edição e remoção de livros
  - Controle de estoque (total e disponível)
  - Sistema de categorização
  - Busca por título, autor, ISBN e categoria
  - Validação de ISBN
- **Gestão de Usuários**
  - Cadastro de bibliotecários e usuários finais
  - Sistema de autenticação seguro
  - Perfis de acesso (ADMIN/USER)
  - Controle de limite de empréstimos por usuário
- **Sistema de Empréstimos**
  - Emissão e devolução de livros
  - Controle automático de prazos
  - Cálculo automático de multas por atraso ($1/dia)
  - Validação de disponibilidade
  - Histórico completo de empréstimos
- **Sistema de Reservas**
  - Reserva de livros indisponíveis
  - Fila de reservas por ordem de chegada
  - Controle de prioridade
- **Interface Web Responsiva**
  - Design adaptável para desktop e mobile
  - Páginas JSP com JSTL
  - Navegação intuitiva
  - Mensagens de feedback ao usuário
- **Segurança**
  - Filtro de autenticação
  - Controle de sessão
  - Validação de permissões
  - Codificação UTF-8
- **Banco de Dados**
  - H2 Database em memória
  - Schema automaticamente criado
  - Dados de exemplo pré-carregados
- **Arquitetura**
  - Padrão MVC com Servlets
  - Camada DAO para acesso a dados
  - Separação clara de responsabilidades
  - Código modular e extensível

### 🛠️ Tecnologias Utilizadas
- **Backend:** Java 11, Servlets 4.0, JSP 2.3, JSF 2.3
- **Frontend:** HTML5, CSS3, JavaScript, Bootstrap
- **Banco:** H2 Database 2.1.214
- **Build:** Maven 3.6+
- **Server:** Apache Tomcat Embedded 9.0.75
- **Container:** Docker support

### 📁 Estrutura Implementada
```
src/main/java/com/library/
├── model/              # Entidades de domínio
│   ├── Book.java       # Modelo de livro
│   ├── User.java       # Modelo de usuário
│   ├── Loan.java       # Modelo de empréstimo
│   └── Reservation.java # Modelo de reserva
├── dao/                # Camada de acesso a dados
│   ├── BookDAO.java    # DAO para livros
│   ├── UserDAO.java    # DAO para usuários
│   ├── LoanDAO.java    # DAO para empréstimos
│   └── ReservationDAO.java # DAO para reservas
├── servlet/            # Controladores web
│   ├── LoginServlet.java # Autenticação
│   ├── LogoutServlet.java # Logout
│   ├── BookServlet.java  # Gestão de livros
│   ├── LoanServlet.java  # Gestão de empréstimos
│   ├── UserServlet.java  # Gestão de usuários
│   └── DashboardServlet.java # Dashboard
├── filter/             # Filtros de requisição
│   ├── AuthenticationFilter.java # Filtro de auth
│   └── CharacterEncodingFilter.java # Filtro UTF-8
└── util/               # Utilitários
    └── DatabaseConnection.java # Conexão DB
```

### 🔐 Funcionalidades de Segurança
- **Autenticação baseada em sessão**
- **Controle de acesso por perfil (ADMIN/USER)**
- **Validação de dados de entrada**
- **Proteção contra SQL Injection (PreparedStatements)**
- **Timeout de sessão configurável**

### 📊 Funcionalidades do Dashboard
- **Estatísticas gerais do sistema**
  - Total de livros cadastrados
  - Total de usuários ativos
  - Empréstimos ativos
  - Empréstimos em atraso
- **Atividades recentes**
- **Livros mais emprestados**
- **Usuários com pendências**

### 🎯 Casos de Uso Implementados

#### Para Bibliotecários (ADMIN)
1. **Gerenciar Acervo**
   - Cadastrar novos livros
   - Atualizar informações de livros
   - Remover livros do sistema
   - Buscar livros por diversos critérios
2. **Gerenciar Usuários**
   - Cadastrar novos usuários
   - Visualizar perfis de usuários
   - Controlar limites de empréstimo
3. **Controlar Empréstimos**
   - Emprestar livros para usuários
   - Processar devoluções
   - Calcular e aplicar multas
   - Visualizar histórico completo
4. **Gerar Relatórios**
   - Listar empréstimos em atraso
   - Acompanhar estatísticas do sistema

#### Para Usuários (USER)
1. **Buscar Livros**
   - Pesquisar por título, autor ou categoria
   - Visualizar disponibilidade
2. **Consultar Empréstimos**
   - Ver histórico pessoal
   - Verificar prazos de devolução
   - Consultar multas pendentes
3. **Fazer Reservas**
   - Reservar livros indisponíveis
   - Acompanhar posição na fila

### 🧪 Validações Implementadas
- **Livros**
  - ISBN único e formato válido
  - Título e autor obrigatórios
  - Quantidade de cópias positiva
- **Usuários**
  - Username único
  - Email válido e único
  - Senha com mínimo de segurança
- **Empréstimos**
  - Verificação de disponibilidade
  - Respeito ao limite por usuário
  - Validação de usuário ativo
- **Reservas**
  - Não permitir reserva de livro disponível
  - Máximo uma reserva por usuário por livro

### 📱 Interface Responsiva
- **Layout adaptativo** para diferentes tamanhos de tela
- **Navegação móvel** otimizada
- **Formulários responsivos**
- **Tabelas com scroll horizontal** em dispositivos pequenos
- **Botões e inputs** com tamanho adequado para touch

### 🔧 Configurações Padrão
- **Porta do servidor:** 5000
- **Timeout de sessão:** 30 minutos
- **Limite padrão de livros por usuário:** 5
- **Multa diária:** $1.00
- **Período padrão de empréstimo:** 14 dias

### 📦 Dados de Exemplo
O sistema vem pré-configurado com:
- **2 usuários padrão:**
  - Admin: `admin/admin123` (Bibliotecário)
  - User: `user/user123` (Usuário normal)
- **Livros de exemplo** em diversas categorias
- **Empréstimos de demonstração**

### 🚀 Deploy
- **Desenvolvimento:** Tomcat Embedded + H2 em memória
- **Produção:** WAR deployável em qualquer servidor Java EE
- **Docker:** Imagem container disponível
- **Porta padrão:** 5000 (configurável)

### 📋 Arquivos de Configuração
- **pom.xml:** Dependências Maven
- **web.xml:** Configuração da aplicação web
- **DatabaseConnection.java:** Schema e dados iniciais

---

## 🔮 Próximas Versões Planejadas

### [1.1.0] - Planejado para Março 2024
- **Sistema de notificações**
  - Email para prazos de devolução
  - Alertas de disponibilidade de reservas
- **Relatórios avançados**
  - Exportação para PDF e Excel
  - Gráficos de estatísticas
- **Melhorias na interface**
  - Tema escuro/claro
  - Mais filtros de busca

### [1.2.0] - Planejado para Maio 2024
- **API REST completa**
  - Endpoints para todas as funcionalidades
  - Documentação OpenAPI/Swagger
- **Aplicação mobile**
  - App nativo para Android/iOS
- **Integração externa**
  - Busca em catálogos de bibliotecas
  - Import/export de dados

### [2.0.0] - Planejado para Agosto 2024
- **Sistema de avaliações**
  - Usuários podem avaliar livros
  - Recomendações personalizadas
- **Funcionalidades sociais**
  - Listas de leitura compartilhadas
  - Grupos de leitura
- **Analytics avançado**
  - Dashboard com métricas detalhadas
  - Previsão de demanda

---

## 📊 Estatísticas da Versão 1.0.0

### Linhas de Código
- **Java:** ~3,500 linhas
- **JSP/HTML:** ~1,200 linhas
- **CSS:** ~800 linhas
- **JavaScript:** ~300 linhas
- **SQL:** ~200 linhas
- **Total:** ~6,000 linhas

### Arquivos por Tipo
- **Classes Java:** 15 arquivos
- **Páginas JSP:** 8 arquivos
- **Arquivos de configuração:** 3 arquivos
- **Documentação:** 15 arquivos

### Funcionalidades
- **25 endpoints** implementados
- **8 casos de uso** principais
- **15 validações** de negócio
- **4 perfis de usuário** suportados

---

## 🏆 Créditos

**Desenvolvedor Principal:** Gabriel Santos do Nascimento  
**Orientador:** Prof. [Nome do Orientador]  
**Instituição:** [Nome da Faculdade]  
**Curso:** Engenharia de Software / Ciência da Computação  
**Período:** 2024.1

### Agradecimentos
- Professores do curso de Engenharia de Software
- Colegas de classe pelas sugestões e testes
- Bibliotecárias da faculdade pelas especificações funcionais
- Comunidade open source pelas tecnologias utilizadas

---

## 📜 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

**🎓 Projeto desenvolvido como Trabalho de Conclusão de Curso**  
**📚 Sistema de Gerenciamento de Biblioteca - Versão 1.0.0**  
**👨‍💻 Por Gabriel Santos do Nascimento**

---

> "A biblioteca é o lugar onde se encontra a alma de uma universidade" - Gabriel Santos
