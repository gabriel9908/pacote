
# Dockerfile - Sistema de Gerenciamento de Biblioteca
# Desenvolvido por Gabriel Santos do Nascimento
# Projeto de Faculdade - 2024

# Estágio 1: Build da aplicação
FROM maven:3.8.6-openjdk-11-slim AS builder

# Metadados da imagem
LABEL maintainer="Gabriel Santos do Nascimento <gabriel.santos@estudante.faculdade.edu.br>"
LABEL description="Sistema de Gerenciamento de Biblioteca - Java Web Application"
LABEL version="1.0.0"
LABEL project="Trabalho de Conclusão de Curso"

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven primeiro (para cache de layers)
COPY pom.xml .

# Baixar dependências (será cached se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN mvn clean compile -B

# Estágio 2: Runtime da aplicação
FROM openjdk:11-jre-slim

# Metadados do runtime
LABEL stage="runtime"

# Instalar utilitários necessários
RUN apt-get update && apt-get install -y \
    curl \
    netcat \
    && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root para segurança
RUN groupadd -r biblioteca && useradd -r -g biblioteca biblioteca

# Criar diretórios necessários
RUN mkdir -p /app/logs /app/data && \
    chown -R biblioteca:biblioteca /app

# Definir diretório de trabalho
WORKDIR /app

# Copiar dependências do estágio de build
COPY --from=builder /root/.m2/repository /home/biblioteca/.m2/repository

# Copiar aplicação compilada
COPY --from=builder /app/target/classes ./classes
COPY --from=builder /app/src ./src

# Criar script de inicialização
COPY --chown=biblioteca:biblioteca <<EOF /app/start.sh
#!/bin/bash

echo "🚀 Iniciando Sistema de Biblioteca..."
echo "📚 Desenvolvido por Gabriel Santos do Nascimento"
echo "🎓 Projeto de Faculdade - 2024"
echo ""

# Verificar conectividade da rede
echo "🔍 Verificando conectividade..."

# Definir variáveis de ambiente
export JAVA_OPTS="\${JAVA_OPTS:-
    -Xmx512m 
    -Xms256m 
    -XX:+UseG1GC 
    -XX:MaxGCPauseMillis=200 
    -Djava.awt.headless=true
    -Djava.security.egd=file:/dev/./urandom
    -Dfile.encoding=UTF-8
    -Duser.timezone=America/Sao_Paulo
}"

# Log das configurações
echo "📋 Configurações do sistema:"
echo "   Java Version: \$(java -version 2>&1 | head -1)"
echo "   Memory: \${JAVA_OPTS}"
echo "   User: \$(whoami)"
echo "   Working Dir: \$(pwd)"
echo ""

# Aguardar banco de dados (se externo)
if [ ! -z "\${DB_HOST}" ]; then
    echo "⏳ Aguardando banco de dados..."
    while ! nc -z \${DB_HOST} \${DB_PORT:-3306}; do
        sleep 1
    done
    echo "✅ Banco de dados conectado!"
fi

# Iniciar aplicação
echo "🎯 Iniciando aplicação na porta \${PORT:-5000}..."
cd /app
exec java \${JAVA_OPTS} -cp "classes:src/main/webapp/WEB-INF/lib/*:/home/biblioteca/.m2/repository/*/*/*/*.jar" com.library.Main
EOF

# Tornar script executável
RUN chmod +x /app/start.sh

# Criar health check script
COPY --chown=biblioteca:biblioteca <<EOF /app/health-check.sh
#!/bin/bash
# Health check para verificar se aplicação está respondendo

PORT=\${PORT:-5000}
HEALTH_URL="http://localhost:\${PORT}/"

# Verificar se aplicação responde
if curl -f -s \${HEALTH_URL} > /dev/null; then
    echo "✅ Aplicação está saudável"
    exit 0
else
    echo "❌ Aplicação não está respondendo"
    exit 1
fi
EOF

RUN chmod +x /app/health-check.sh

# Trocar para usuário não-root
USER biblioteca

# Definir variáveis de ambiente
ENV PORT=5000
ENV JAVA_OPTS=""
ENV TZ=America/Sao_Paulo

# Expor porta
EXPOSE $PORT

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /app/health-check.sh

# Definir volumes para dados persistentes
VOLUME ["/app/logs", "/app/data"]

# Comando padrão
CMD ["/app/start.sh"]

# Adicionar labels adicionais para metadados
LABEL org.opencontainers.image.title="Sistema de Biblioteca"
LABEL org.opencontainers.image.description="Sistema completo de gerenciamento de biblioteca com Java, JSP, JSF e Servlets"
LABEL org.opencontainers.image.author="Gabriel Santos do Nascimento"
LABEL org.opencontainers.image.url="https://github.com/gabriel9908/pacote"
LABEL org.opencontainers.image.documentation="https://github.com/gabriel9908/pacote/blob/main/README.md"
LABEL org.opencontainers.image.source="https://github.com/gabriel9908/pacote"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.created="2024-01-25"
LABEL edu.faculdade.projeto="tcc"
LABEL edu.faculdade.curso="engenharia-software"
LABEL edu.faculdade.aluno="gabriel-santos"
