
# 🤝 Guia de Contribuição - Sistema de Biblioteca

Obrigado por seu interesse em contribuir com o Sistema de Gerenciamento de Biblioteca! Este documento orienta você sobre como contribuir de forma efetiva para o projeto.

## 🎯 Como Contribuir

### Tipos de Contribuição
- 🐛 **Correção de Bugs**: Relatar e corrigir problemas
- ✨ **Novas Funcionalidades**: Implementar recursos solicitados
- 📚 **Documentação**: Melhorar docs e exemplos
- 🧪 **Testes**: Adicionar ou melhorar testes
- 🎨 **Interface**: Melhorias na UI/UX
- ⚡ **Performance**: Otimizações de código

## 🔧 Configuração do Ambiente

### 1. Fork e Clone
```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/SEU_USERNAME/sistema-biblioteca.git
cd sistema-biblioteca

# Adicione o repositório original como upstream
git remote add upstream https://github.com/gabrielsantos/sistema-biblioteca.git
```

### 2. Configurar Ambiente de Desenvolvimento
```bash
# Instalar dependências
mvn clean install

# Verificar se tudo está funcionando
mvn clean compile
mvn test
```

### 3. Configurar IDE
**Recomendado**: IntelliJ IDEA, Eclipse ou VS Code

**Configurações**:
- **Encoding**: UTF-8
- **Line Endings**: LF (Unix)
- **Tab Size**: 4 espaços
- **Java Version**: 11+

## 📋 Processo de Contribuição

### 1. Criar Issue (Opcional, mas recomendado)
Antes de implementar, crie uma issue descrevendo:
- **Problema**: O que está errado ou faltando?
- **Solução**: Como pretende resolver?
- **Benefícios**: Por que essa mudança é importante?

### 2. Criar Branch de Feature
```bash
# Sempre crie uma nova branch a partir da main
git checkout main
git pull upstream main
git checkout -b feature/nome-da-funcionalidade

# Ou para bug fixes
git checkout -b bugfix/descricao-do-bug
```

**Convenção de Nomes**:
- `feature/adicionar-sistema-multas`
- `bugfix/corrigir-login-erro-500`
- `docs/atualizar-readme`
- `test/adicionar-testes-bookdao`

### 3. Implementar Mudanças

#### Padrões de Código
```java
// ✅ BOM
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
    
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        
        LOGGER.info("Searching books with query: {}", query);
        return bookDAO.searchByTitle(query);
    }
}

// ❌ RUIM
public class bookservice {
    public List<Book> search(String q) {
        return bookDAO.search(q); // Sem validação
    }
}
```

#### Padrões JSP
```jsp
<!-- ✅ BOM -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Livros</title>
</head>

<!-- ❌ RUIM -->
<html>
<head><title>Books</title></head>
<!-- Sem DOCTYPE, sem charset, sem viewport -->
```

### 4. Testes
```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=BookDAOTest

# Verificar cobertura
mvn jacoco:report
```

#### Exemplo de Teste
```java
@Test
public void testAddBook_ValidBook_ShouldReturnTrue() {
    // Arrange
    Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                        "Test Publisher", LocalDate.now(), "Fiction", 5, "Description");
    
    // Act
    boolean result = bookDAO.addBook(book);
    
    // Assert
    assertTrue("Book should be added successfully", result);
    assertNotNull("Book ID should be generated", book.getId());
}

@Test
public void testAddBook_NullBook_ShouldThrowException() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> {
        bookDAO.addBook(null);
    });
}
```

### 5. Commit
**Convenção de Commits**: [Conventional Commits](https://www.conventionalcommits.org/)

```bash
# Formato
type(scope): description

# Exemplos
feat(books): adicionar busca por categoria
fix(login): corrigir erro de autenticação
docs(readme): atualizar instruções de instalação
test(dao): adicionar testes para BookDAO
refactor(servlet): extrair validação para classe separada
```

**Tipos Válidos**:
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `test`: Testes
- `refactor`: Refatoração
- `style`: Formatação
- `perf`: Performance
- `chore`: Tarefas de manutenção

```bash
# Fazer commit das mudanças
git add .
git commit -m "feat(loans): implementar sistema de multas automáticas"

# Push para seu fork
git push origin feature/sistema-multas
```

### 6. Pull Request
1. **Vá para o GitHub** e abra um Pull Request
2. **Título**: Claro e descritivo
3. **Descrição**: Use o template abaixo

#### Template de PR
```markdown
## 📝 Descrição
Breve descrição do que foi implementado/corrigido.

## 🔄 Tipo de Mudança
- [ ] Bug fix (mudança que corrige um problema)
- [ ] Nova funcionalidade (mudança que adiciona funcionalidade)
- [ ] Breaking change (mudança que quebra compatibilidade)
- [ ] Documentação

## 🧪 Testes
- [ ] Testes unitários passando
- [ ] Testes de integração passando
- [ ] Testado manualmente

## 📋 Checklist
- [ ] Código segue os padrões do projeto
- [ ] Self-review realizado
- [ ] Comentários adicionados em código complexo
- [ ] Documentação atualizada
- [ ] Sem warnings ou erros

## 📸 Screenshots (se aplicável)
[Adicionar screenshots da interface]

## 🔗 Issues Relacionadas
Closes #123
Related to #456
```

## 🐛 Reportando Bugs

### Template de Bug Report
```markdown
**Descrição do Bug**
Descrição clara e concisa do problema.

**Passos para Reproduzir**
1. Vá para '...'
2. Clique em '....'
3. Faça scroll até '....'
4. Veja o erro

**Comportamento Esperado**
O que deveria acontecer.

**Comportamento Atual**
O que realmente acontece.

**Screenshots**
Adicione screenshots se aplicável.

**Ambiente:**
- OS: [Windows 10, macOS, Linux]
- Java: [versão]
- Browser: [Chrome, Firefox, Safari]
- Versão: [v1.0.0]

**Logs**
```
[Cole os logs relevantes aqui]
```

**Contexto Adicional**
Qualquer informação adicional.
```

## ✨ Solicitando Features

### Template de Feature Request
```markdown
**A feature está relacionada a um problema?**
Descrição clara do problema. Ex: "Sou sempre frustrado quando [...]"

**Descreva a solução que você gostaria**
Descrição clara e concisa do que você quer que aconteça.

**Descreva alternativas consideradas**
Descrição de soluções alternativas ou features consideradas.

**Contexto adicional**
Adicione qualquer contexto ou screenshots sobre a feature.

**Prioridade**
- [ ] Alta - Funcionalidade crítica
- [ ] Média - Melhoria importante
- [ ] Baixa - Nice to have
```

## 🎨 Padrões de Interface

### CSS/HTML
```css
/* ✅ BOM - Classes semânticas */
.book-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;
}

.book-card__title {
    font-size: 1.2em;
    font-weight: bold;
    color: #333;
}

.book-card__author {
    color: #666;
    font-style: italic;
}

/* ❌ RUIM - Classes genéricas */
.container1 {
    border: 1px solid #ddd;
}

.text1 {
    font-size: 1.2em;
}
```

### Responsividade
```css
/* Mobile First */
.book-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 16px;
}

/* Tablet */
@media (min-width: 768px) {
    .book-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

/* Desktop */
@media (min-width: 1024px) {
    .book-grid {
        grid-template-columns: repeat(3, 1fr);
    }
}
```

## 📚 Documentação

### JavaDoc
```java
/**
 * Busca livros por múltiplos critérios.
 * 
 * @param title Título do livro (pode ser parcial)
 * @param author Autor do livro (pode ser parcial)
 * @param category Categoria específica
 * @return Lista de livros que atendem aos critérios
 * @throws IllegalArgumentException se todos os parâmetros forem nulos
 * @since 1.0.0
 */
public List<Book> searchBooks(String title, String author, String category) {
    // implementação
}
```

### Comentários em Código
```java
// ✅ BOM - Explica o "porquê"
// Aplicamos desconto de 10% para estudantes cadastrados há mais de 1 ano
if (user.isStudent() && user.getRegistrationDate().isBefore(LocalDate.now().minusYears(1))) {
    fine = fine * 0.9;
}

// ❌ RUIM - Explica o "o que" (óbvio)
// Multiplica fine por 0.9
fine = fine * 0.9;
```

## 🧪 Testes

### Estrutura de Testes
```
src/test/java/
├── com/library/
│   ├── dao/
│   │   ├── BookDAOTest.java
│   │   ├── UserDAOTest.java
│   │   └── LoanDAOTest.java
│   ├── servlet/
│   │   ├── BookServletTest.java
│   │   └── LoginServletTest.java
│   └── util/
│       └── DatabaseConnectionTest.java
```

### Convenções de Teste
```java
public class BookDAOTest {
    
    @BeforeEach
    void setUp() {
        // Configuração antes de cada teste
    }
    
    @AfterEach
    void tearDown() {
        // Limpeza após cada teste
    }
    
    @Test
    @DisplayName("Deve adicionar livro com dados válidos")
    void shouldAddBookWithValidData() {
        // Given (Arrange)
        
        // When (Act)
        
        // Then (Assert)
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar livro com ISBN duplicado")
    void shouldThrowExceptionWhenAddingBookWithDuplicateISBN() {
        // Teste de caso de erro
    }
}
```

## 🔄 Review Process

### Critérios para Aprovação
1. **Funcionalidade**: Funciona conforme especificado
2. **Código**: Segue padrões e boas práticas
3. **Testes**: Cobertura adequada e testes passando
4. **Documentação**: Atualizada conforme necessário
5. **Performance**: Não impacta negativamente
6. **Segurança**: Não introduz vulnerabilidades

### Como Revisar
```bash
# Baixar PR para testar localmente
git fetch upstream pull/ID/head:pr-branch
git checkout pr-branch

# Testar funcionalidade
mvn clean test
mvn exec:java -Dexec.mainClass="com.library.Main"

# Verificar código
# Use ferramentas como SonarQube, SpotBugs, etc.
```

## 📞 Comunicação

### Canais
- **Issues**: Para bugs e feature requests
- **Discussions**: Para perguntas e ideias
- **Email**: gabriel.santos@estudante.faculdade.edu.br

### Código de Conduta
- Seja respeitoso e profissional
- Aceite feedback construtivo
- Foque no problema, não na pessoa
- Seja paciente com iniciantes

## 🎉 Reconhecimento

Contribuidores são reconhecidos no arquivo [CONTRIBUTORS.md](CONTRIBUTORS.md) e no README.

### Processo de Reconhecimento
1. **First-time contributor**: Badge especial
2. **Regular contributor**: Menção no README
3. **Major contributor**: Acesso de colaborador

---

## 📋 Checklist do Contribuidor

Antes de submeter seu PR, verifique:

- [ ] ✅ Fork criado e branch de feature/bugfix usada
- [ ] 🧪 Todos os testes passando (`mvn test`)
- [ ] 📝 Código documentado adequadamente
- [ ] 🎨 Padrões de código seguidos
- [ ] 📱 Interface responsiva (se aplicável)
- [ ] 🔒 Validações de segurança implementadas
- [ ] 📚 Documentação atualizada
- [ ] 💬 Commit messages seguem convenção
- [ ] 🔍 Self-review realizado
- [ ] 📝 PR description preenchida

---

**Obrigado por contribuir com o Sistema de Biblioteca! 🚀**

Cada contribuição, por menor que seja, faz a diferença e nos ajuda a construir um sistema melhor para todos.

---

**Desenvolvido por Gabriel Santos do Nascimento**  
📧 gabriel.santos@estudante.faculdade.edu.br  
🎓 Projeto de Faculdade - Engenharia de Software
