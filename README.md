
# 📚 Sistema de Gerenciamento de Biblioteca

**Desenvolvido por:** Gabriel Santos do Nascimento  
**Projeto:** Trabalho de Conclusão de Curso - Faculdade  
**Tecnologias:** Java, JSP, JSF, Servlets, Maven, H2 Database, Docker

---

## 🎯 Sobre o Projeto

Este é um sistema completo de gerenciamento de biblioteca desenvolvido como projeto acadêmico. O sistema oferece funcionalidades robustas para administração de livros, usuários, empréstimos e reservas, construído com tecnologias Java EE modernas.

## ✨ Funcionalidades Principais

### 📖 Gestão de Livros
- ✅ Cadastro completo de livros (ISBN, título, autor, editora, etc.)
- ✅ Controle de estoque e disponibilidade
- ✅ Sistema de categorização
- ✅ Busca avançada por múltiplos critérios
- ✅ Edição e remoção de registros

### 👥 Gestão de Usuários
- ✅ Cadastro de bibliotecários e usuários
- ✅ Sistema de autenticação seguro
- ✅ Perfis de acesso diferenciados
- ✅ Controle de limites de empréstimo

### 📋 Sistema de Empréstimos
- ✅ Emissão e devolução de livros
- ✅ Controle de prazos e multas
- ✅ Histórico completo de empréstimos
- ✅ Relatórios de livros em atraso

### 🔒 Sistema de Reservas
- ✅ Reserva de livros indisponíveis
- ✅ Notificações de disponibilidade
- ✅ Fila de reservas por livro

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| **Java** | 11+ | Linguagem principal |
| **Maven** | 3.x | Gerenciamento de dependências |
| **Servlets** | 4.0 | Controle de requisições |
| **JSP** | 2.3 | Interface de usuário |
| **JSF** | 2.3 | Framework web |
| **H2 Database** | 2.1 | Banco de dados em memória |
| **Apache Tomcat** | 9.0 | Servidor de aplicação |
| **Docker** | Latest | Containerização |

## 🚀 Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6+
- Docker (opcional)

### Execução Local
```bash
# Clone o repositório
git clone https://github.com/gabriel9908/pacote
cd pacote

# Compile o projeto
mvn clean compile

# Execute a aplicação
mvn exec:java -Dexec.mainClass="com.library.Main"
```

### Execução com Docker
```bash
# Build da imagem
docker build -t biblioteca-sistema .

# Execute o container
docker run -p 5000:5000 biblioteca-sistema
```

Acesse: `http://localhost:5000`

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/library/
│   │   ├── model/          # Modelos de dados
│   │   ├── dao/            # Acesso a dados
│   │   ├── servlet/        # Controladores
│   │   ├── filter/         # Filtros de segurança
│   │   └── util/           # Utilitários
│   └── webapp/
│       ├── WEB-INF/jsp/    # Páginas JSP
│       └── index.jsp       # Página inicial
```

## 🔐 Usuários Padrão

| Tipo | Username | Senha | Descrição |
|------|----------|-------|-----------|
| Admin | admin | admin123 | Acesso completo ao sistema |
| User | user | user123 | Acesso limitado (empréstimos) |

## 📊 Funcionalidades Detalhadas

### Dashboard Administrativo
- Resumo de estatísticas do sistema
- Livros mais emprestados
- Usuários com pendências
- Relatórios gerenciais

### Controle de Multas
- Cálculo automático de multas por atraso
- Histórico de pagamentos
- Bloqueio automático de usuários inadimplentes

### Relatórios
- Relatório de empréstimos por período
- Livros mais populares
- Usuários mais ativos
- Estatísticas de categoria

## 🤝 Contribuição

Este é um projeto acadêmico, mas sugestões são bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato

**Gabriel Santos do Nascimento**
- 📧 Email: gabrielnascimento.01@hotmail.com


---

⭐ **Se este projeto foi útil para você, deixe uma estrela!**
