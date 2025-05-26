
# 🔧 Guia de Solução de Problemas - Sistema de Biblioteca

Este documento contém soluções para problemas comuns encontrados durante a instalação, configuração e uso do Sistema de Gerenciamento de Biblioteca.

## 🚨 Problemas Comuns de Instalação

### ❌ Erro: "java: command not found"

**Problema**: Java não está instalado ou configurado corretamente.

**Sintomas**:
```bash
$ java -version
bash: java: command not found
```

**Soluções**:

#### Linux (Ubuntu/Debian)
```bash
# Instalar OpenJDK 11
sudo apt update
sudo apt install openjdk-11-jdk

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Verificar
java -version
javac -version
```

#### macOS
```bash
# Instalar via Homebrew
brew install openjdk@11

# Configurar JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 11)' >> ~/.zshrc
source ~/.zshrc

# Verificar
java -version
```

#### Windows
```cmd
# Baixar do site oficial
# https://www.oracle.com/java/technologies/downloads/

# Configurar variáveis de ambiente
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.x
set PATH=%JAVA_HOME%\bin;%PATH%

# Verificar
java -version
```

---

### ❌ Erro: "mvn: command not found"

**Problema**: Maven não está instalado.

**Soluções**:

#### Linux
```bash
# Ubuntu/Debian
sudo apt install maven

# CentOS/RHEL
sudo yum install maven

# Verificar
mvn -version
```

#### macOS
```bash
# Via Homebrew
brew install maven

# Verificar
mvn -version
```

#### Windows
```cmd
# Baixar do site oficial
# https://maven.apache.org/download.cgi

# Extrair e configurar
set MAVEN_HOME=C:\apache-maven-3.8.6
set PATH=%MAVEN_HOME%\bin;%PATH%

# Verificar
mvn -version
```

---

### ❌ Erro: "No compiler is provided in this environment"

**Problema**: JDK não está disponível, apenas JRE.

**Sintomas**:
```
[ERROR] No compiler is provided in this environment. 
Perhaps you are running on a JRE rather than a JDK?
```

**Soluções**:
```bash
# Verificar se é JDK (deve mostrar javac)
javac -version

# Se não tiver javac, instalar JDK completo
# Linux
sudo apt install openjdk-11-jdk-headless

# Configurar JAVA_HOME para JDK (não JRE)
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
```

---

## 🗄️ Problemas de Banco de Dados

### ❌ Erro: "Database connection failed"

**Problema**: Falha na conexão com H2 Database.

**Sintomas**:
```
java.sql.SQLException: Database connection failed
```

**Soluções**:

#### Verificar Dependências
```bash
# Verificar se H2 está no classpath
mvn dependency:tree | grep h2

# Se não estiver, adicionar ao pom.xml
```

#### Verificar Configuração
```java
// DatabaseConnection.java - Verificar URL
private static final String DB_URL = 
    "jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1;MODE=MySQL";
```

#### Logs Detalhados
```java
// Adicionar logging para debug
System.out.println("Tentando conectar com: " + DB_URL);
Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
System.out.println("Conexão estabelecida: " + conn.isValid(5));
```

---

### ❌ Erro: "Table not found"

**Problema**: Tabelas não foram criadas automaticamente.

**Sintomas**:
```
org.h2.jdbc.JdbcSQLSyntaxErrorException: Table "BOOKS" not found
```

**Soluções**:

#### Verificar Inicialização
```java
// DatabaseConnection.java - Verificar se initializeDatabase() é chamado
static {
    try {
        Class.forName("org.h2.Driver");
        initializeDatabase(); // Esta linha deve estar presente
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

#### Executar Script Manualmente
```sql
-- Conectar ao H2 Console: http://localhost:8082
-- URL: jdbc:h2:mem:librarydb
-- User: sa
-- Password: (vazio)

-- Executar scripts de criação das tabelas
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    -- resto da definição
);
```

---

## 🌐 Problemas do Servidor Web

### ❌ Erro: "Port 5000 already in use"

**Problema**: Porta 5000 já está sendo usada.

**Sintomas**:
```
java.net.BindException: Address already in use: bind
```

**Soluções**:

#### Encontrar Processo na Porta
```bash
# Linux/macOS
lsof -i :5000
netstat -tlnp | grep :5000

# Windows
netstat -ano | findstr :5000
```

#### Matar Processo
```bash
# Linux/macOS
kill -9 <PID>

# Windows
taskkill /PID <PID> /F
```

#### Alterar Porta
```java
// Main.java - Alterar porta
tomcat.setPort(8080); // Trocar 5000 por 8080
```

---

### ❌ Erro: "404 Not Found" para JSPs

**Problema**: Páginas JSP não são encontradas.

**Sintomas**:
```
HTTP Status 404 - /WEB-INF/jsp/books/list.jsp
```

**Soluções**:

#### Verificar Estrutura de Diretórios
```
src/main/webapp/
├── WEB-INF/
│   ├── jsp/
│   │   ├── books/
│   │   │   ├── list.jsp ✅
│   │   │   └── add.jsp ✅
│   │   └── login.jsp ✅
│   └── web.xml ✅
└── index.jsp ✅
```

#### Verificar RequestDispatcher
```java
// BookServlet.java - Verificar caminho
request.getRequestDispatcher("/WEB-INF/jsp/books/list.jsp")
       .forward(request, response);
```

#### Verificar web.xml
```xml
<!-- web.xml - Verificar mapeamento de servlets -->
<servlet-mapping>
    <servlet-name>BookServlet</servlet-name>
    <url-pattern>/books/*</url-pattern>
</servlet-mapping>
```

---

## 🔐 Problemas de Autenticação

### ❌ Erro: "Invalid username or password"

**Problema**: Login não funciona com credenciais padrão.

**Soluções**:

#### Verificar Usuários Padrão
```java
// DatabaseConnection.java - Verificar se usuários são criados
stmt.execute("INSERT INTO users (username, password, first_name, last_name, email, role) " +
            "VALUES ('admin', 'admin123', 'Admin', 'User', 'admin@library.com', 'ADMIN')");
```

#### Reset de Senha
```sql
-- Conectar ao H2 Console e executar
UPDATE users SET password = 'admin123' WHERE username = 'admin';
UPDATE users SET password = 'user123' WHERE username = 'user';
```

#### Debug de Login
```java
// LoginServlet.java - Adicionar logs
LOGGER.info("Tentativa de login para usuário: {}", username);
User user = userDAO.authenticateUser(username, password);
LOGGER.info("Resultado da autenticação: {}", user != null ? "Sucesso" : "Falha");
```

---

### ❌ Erro: "Session expired"

**Problema**: Sessão expira muito rapidamente.

**Soluções**:

#### Aumentar Timeout
```xml
<!-- web.xml -->
<session-config>
    <session-timeout>60</session-timeout> <!-- 60 minutos -->
</session-config>
```

#### Verificar Filtro de Autenticação
```java
// AuthenticationFilter.java
HttpSession session = request.getSession(false);
if (session == null || session.getAttribute("user") == null) {
    // Redirecionar para login
}
```

---

## 🔄 Problemas de Compilação

### ❌ Erro: "Package does not exist"

**Problema**: Dependências não foram baixadas.

**Sintomas**:
```
error: package javax.servlet does not exist
error: package javax.servlet.http does not exist
```

**Soluções**:

#### Baixar Dependências
```bash
# Limpar e reinstalar dependências
mvn clean
mvn install

# Forçar atualização
mvn clean install -U
```

#### Verificar pom.xml
```xml
<!-- Verificar se todas as dependências estão presentes -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
```

---

### ❌ Erro: "Invalid target release: 11"

**Problema**: Maven não reconhece Java 11.

**Soluções**:
```xml
<!-- pom.xml - Verificar configuração do compilador -->
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```

```bash
# Verificar versão do Java que Maven está usando
mvn -version

# Se necessário, configurar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
```

---

## 🐳 Problemas com Docker

### ❌ Erro: "docker: command not found"

**Soluções**:

#### Linux
```bash
# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
newgrp docker
```

#### Windows/macOS
```bash
# Instalar Docker Desktop
# https://www.docker.com/products/docker-desktop
```

---

### ❌ Erro: "Permission denied" no Docker

**Soluções**:
```bash
# Linux - Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
sudo systemctl restart docker
logout
# Fazer login novamente
```

---

## 📱 Problemas de Interface

### ❌ Layout quebrado no mobile

**Problema**: Interface não responsiva.

**Soluções**:

#### Verificar Viewport
```html
<!-- Adicionar em todas as páginas JSP -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
```

#### CSS Responsivo
```css
/* Adicionar media queries */
@media (max-width: 768px) {
    .table-responsive {
        overflow-x: auto;
    }
    
    .btn {
        width: 100%;
        margin: 5px 0;
    }
}
```

---

### ❌ Caracteres especiais não aparecem

**Problema**: Encoding UTF-8 não está funcionando.

**Soluções**:

#### JSP
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
```

#### Servlet
```java
request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");
```

#### Filtro
```java
// CharacterEncodingFilter.java - Verificar se está configurado
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
```

---

## 🔍 Ferramentas de Debug

### Logs Detalhados
```java
// Adicionar logging detalhado
import java.util.logging.Logger;
import java.util.logging.Level;

private static final Logger LOGGER = Logger.getLogger(BookServlet.class.getName());

LOGGER.info("Processando requisição: " + request.getRequestURI());
LOGGER.severe("Erro crítico: " + e.getMessage());
```

### H2 Console
```java
// Habilitar H2 Console para debug do banco
// Adicionar ao main()
Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
server.start();
System.out.println("H2 Console: http://localhost:8082");
```

### Verificar Conectividade
```bash
# Testar se aplicação está respondendo
curl -I http://localhost:5000

# Verificar logs do Tomcat
tail -f logs/catalina.out
```

### Ferramentas de Desenvolvimento
```bash
# Verificar processos Java
jps -v

# Monitor de JVM
jconsole

# Profiling
java -Xmx1024m -Xms512m -XX:+PrintGCDetails Main
```

---

## 📞 Quando Buscar Ajuda

### Informações a Coletar
Antes de buscar ajuda, colete:

1. **Versão do Java**: `java -version`
2. **Versão do Maven**: `mvn -version`
3. **Sistema Operacional**: `uname -a` (Linux/macOS) ou `ver` (Windows)
4. **Logs completos**: Copie toda a stack trace
5. **Passos para reproduzir**: O que você estava fazendo quando o erro ocorreu

### Template de Relato de Bug
```markdown
**Ambiente:**
- OS: [Windows 10 / macOS Big Sur / Ubuntu 20.04]
- Java: [11.0.x]
- Maven: [3.x.x]
- Browser: [Chrome 97.x]

**Descrição do Problema:**
[Descreva o que está acontecendo]

**Passos para Reproduzir:**
1. [Primeiro passo]
2. [Segundo passo]
3. [Etc.]

**Comportamento Esperado:**
[O que deveria acontecer]

**Logs/Erros:**
```
[Cole aqui os logs completos]
```

**Screenshots:**
[Se aplicável]
```

### Canais de Suporte
- 📧 **Email**: gabriel.santos@estudante.faculdade.edu.br
- 🐙 **GitHub Issues**: Para bugs e sugestões
- 📚 **Documentação**: Consulte os arquivos README.md e DOCUMENTATION.md

---

## 🔄 Soluções Rápidas (Quick Fixes)

### Reset Completo
```bash
# Se nada mais funcionar, reset completo
git clean -fdx
mvn clean
rm -rf target/
mvn install
mvn exec:java -Dexec.mainClass="com.library.Main"
```

### Verificação de Saúde do Sistema
```bash
#!/bin/bash
# health-check.sh

echo "=== Verificação de Saúde do Sistema ==="

echo "1. Verificando Java..."
java -version || echo "❌ Java não encontrado"

echo "2. Verificando Maven..."
mvn -version || echo "❌ Maven não encontrado"

echo "3. Verificando estrutura do projeto..."
[ -f pom.xml ] && echo "✅ pom.xml encontrado" || echo "❌ pom.xml não encontrado"

echo "4. Verificando dependências..."
mvn dependency:resolve || echo "❌ Erro nas dependências"

echo "5. Verificando compilação..."
mvn compile || echo "❌ Erro na compilação"

echo "6. Verificando porta 5000..."
netstat -ln | grep :5000 && echo "⚠️ Porta 5000 em uso" || echo "✅ Porta 5000 livre"

echo "=== Verificação concluída ==="
```

---

**🔧 Última atualização:** 25 de Janeiro de 2024  
**👨‍💻 Por:** Gabriel Santos do Nascimento  
**📧 Suporte:** gabriel.santos@estudante.faculdade.edu.br

> "A maioria dos bugs são resultado de nossa própria ignorância" - Linus Torvalds
