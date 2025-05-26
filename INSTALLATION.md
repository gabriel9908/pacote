
# 🛠️ Guia de Instalação - Sistema de Biblioteca

## 📋 Pré-requisitos

### Requisitos de Sistema
- **Sistema Operacional**: Windows 10+, macOS 10.14+, ou Linux (Ubuntu 18+)
- **RAM**: Mínimo 4GB, Recomendado 8GB
- **Espaço em Disco**: 500MB livres
- **Conexão**: Internet (para download de dependências)

### Software Necessário

#### 1. Java Development Kit (JDK)
```bash
# Verificar se Java está instalado
java -version
javac -version

# Deve mostrar versão 11 ou superior
```

**Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) ou [OpenJDK](https://openjdk.java.net/)

#### 2. Apache Maven
```bash
# Verificar instalação do Maven
mvn -version

# Deve mostrar versão 3.6 ou superior
```

**Download**: [Apache Maven](https://maven.apache.org/download.cgi)

#### 3. Git
```bash
# Verificar instalação do Git
git --version
```

**Download**: [Git SCM](https://git-scm.com/downloads)

#### 4. Docker (Opcional)
```bash
# Verificar instalação do Docker
docker --version
docker-compose --version
```

**Download**: [Docker Desktop](https://www.docker.com/products/docker-desktop)

## 🚀 Instalação Passo a Passo

### Método 1: Instalação Manual

#### Passo 1: Clone o Repositório
```bash
# Clone o projeto
git clone https://github.com/gabrielsantos/sistema-biblioteca.git

# Entre no diretório
cd sistema-biblioteca

# Verificar arquivos
ls -la
```

#### Passo 2: Configurar Variáveis de Ambiente
```bash
# Linux/macOS
export JAVA_HOME=/caminho/para/jdk
export MAVEN_HOME=/caminho/para/maven
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH

# Windows (Command Prompt)
set JAVA_HOME=C:\Program Files\Java\jdk-11
set MAVEN_HOME=C:\Program Files\Apache\maven
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
```

#### Passo 3: Instalar Dependências
```bash
# Baixar e instalar dependências
mvn clean install

# Verificar se compilou corretamente
mvn compile
```

#### Passo 4: Executar a Aplicação
```bash
# Método 1: Usando Maven
mvn exec:java -Dexec.mainClass="com.library.Main"

# Método 2: Usando Java diretamente
javac -classpath .:target/dependency/* -d . $(find . -type f -name '*.java')
java -classpath .:target/dependency/* Main
```

#### Passo 5: Acessar o Sistema
```
URL: http://localhost:5000
Usuário Admin: admin / admin123
Usuário Normal: user / user123
```

### Método 2: Instalação com Docker

#### Passo 1: Preparar Docker
```bash
# Verificar se Docker está rodando
docker ps

# Clone o repositório (se ainda não fez)
git clone https://github.com/gabrielsantos/sistema-biblioteca.git
cd sistema-biblioteca
```

#### Passo 2: Build da Imagem
```bash
# Construir imagem Docker
docker build -t biblioteca-sistema .

# Verificar se a imagem foi criada
docker images
```

#### Passo 3: Executar Container
```bash
# Executar em foreground (para debug)
docker run -p 5000:5000 biblioteca-sistema

# Executar em background
docker run -d -p 5000:5000 --name biblioteca biblioteca-sistema

# Verificar se está rodando
docker ps
```

#### Passo 4: Gerenciar Container
```bash
# Parar container
docker stop biblioteca

# Iniciar container parado
docker start biblioteca

# Remover container
docker rm biblioteca

# Ver logs
docker logs biblioteca
```

### Método 3: Docker Compose (Recomendado)

#### Passo 1: Usar Docker Compose
```bash
# Executar com docker-compose
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down
```

## ⚙️ Configurações Avançadas

### Configuração de Porta
Para alterar a porta padrão (5000), edite o arquivo `src/main/java/Main.java`:

```java
// Linha 11
tomcat.setPort(8080); // Altere para a porta desejada
```

### Configuração de Banco de Dados

#### H2 Database (Padrão)
```java
// src/main/java/com/library/util/DatabaseConnection.java
private static final String DB_URL = "jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1";
private static final String DB_USER = "sa";
private static final String DB_PASSWORD = "";
```

#### MySQL (Alternativa)
```java
// Alterar configurações para MySQL
private static final String DB_URL = "jdbc:mysql://localhost:3306/biblioteca";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "senha";
```

### Configuração de Logs
Criar arquivo `log4j.properties` em `src/main/resources/`:

```properties
log4j.rootLogger=INFO, stdout, file

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=logs/application.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

## 🐛 Solução de Problemas

### Problema: "java: command not found"
**Solução**:
```bash
# Verificar se JAVA_HOME está configurado
echo $JAVA_HOME

# Se não estiver, configure:
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Problema: "mvn: command not found"
**Solução**:
```bash
# Baixar Maven manualmente
wget https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
tar -xzf apache-maven-3.8.6-bin.tar.gz
export MAVEN_HOME=/path/to/apache-maven-3.8.6
export PATH=$MAVEN_HOME/bin:$PATH
```

### Problema: Porta 5000 em uso
**Solução**:
```bash
# Verificar o que está usando a porta
lsof -i :5000

# Matar processo
kill -9 <PID>

# Ou alterar porta no código
```

### Problema: Dependências não baixam
**Solução**:
```bash
# Limpar cache do Maven
mvn dependency:purge-local-repository

# Forçar atualização
mvn clean install -U
```

### Problema: Erro de permissão (Linux/macOS)
**Solução**:
```bash
# Dar permissão de execução
chmod +x scripts/*.sh

# Executar com sudo se necessário
sudo mvn clean install
```

## 📊 Verificação da Instalação

### Checklist de Verificação
- [ ] Java 11+ instalado e configurado
- [ ] Maven 3.6+ instalado
- [ ] Projeto clonado com sucesso
- [ ] Dependências baixadas (`target/dependency/` existe)
- [ ] Aplicação compila sem erros
- [ ] Aplicação executa na porta 5000
- [ ] Interface web acessível
- [ ] Login funciona com credenciais padrão
- [ ] Database inicializa corretamente

### Comandos de Teste
```bash
# Testar compilação
mvn clean compile

# Testar empacotamento
mvn clean package

# Testar execução
timeout 10s mvn exec:java -Dexec.mainClass="com.library.Main"

# Testar conectividade
curl -I http://localhost:5000
```

## 🔄 Atualizações

### Atualizar o Sistema
```bash
# Puxar atualizações do repositório
git pull origin main

# Recompilar
mvn clean install

# Reiniciar aplicação
```

### Backup de Dados
```bash
# Criar backup do banco H2 (se estiver usando arquivo)
cp database.mv.db backup_$(date +%Y%m%d_%H%M%S).mv.db
```

## 📞 Suporte

Em caso de problemas durante a instalação:

1. **Verifique os logs**: `logs/application.log`
2. **Consulte a documentação**: `DOCUMENTATION.md`
3. **Verifique issues conhecidas**: `TROUBLESHOOTING.md`
4. **Contate o desenvolvedor**: gabriel.santos@estudante.faculdade.edu.br

---

✅ **Instalação concluída com sucesso!**  
Agora você pode começar a usar o Sistema de Gerenciamento de Biblioteca.
