# 🔏 Safe Notes — Spring Security OAuth2

Projeto desenvolvido na formação **Build & Run**, com foco em autenticação e segurança de APIs utilizando **Spring Security OAuth2** como Resource Server. A aplicação emite tokens JWT assinados com chaves públicas e privadas, permitindo controle seguro de acesso às anotações dos usuários.

---

## 📦 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security OAuth2
- JWT (JSON Web Tokens)
- Redis (blocklist de tokens)
- Docker & Docker Compose
- Gradle

---

## 🚀 Funcionalidades

- ✅ Emissão e validação de JWTs com assinatura RSA
- ✅ Armazenamento seguro de Refresh Tokens
- ✅ Fluxo completo de login, logout e renovação de tokens
- ✅ RBAC (Role-Based Access Control) para controle de permissões
- ✅ Proteção de endpoints com autenticação via token
- ✅ Encerramento de sessões individuais e globais
- ✅ Blocklist com Redis para tokens inválidos

---

## ▶️ Como Executar o Projeto

```bash
# 1. Suba os containers necessários
cd docker
docker-compose up -d

# 2. Volte para a raiz e inicie a aplicação
cd ..
./gradlew clean bootRun

```
### Obs: Na pasta raiz do projeto existe uma pasta chamada bruno que contém todos endpoints utilizados.

