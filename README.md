# Gestão de Loja

Sistema em arquitetura de microsserviços para gestão de loja. Atualmente conta com dois serviços independentes: **authenticator** (autenticação e usuários) e **estoque** (produtos e categorias). Novos microsserviços serão adicionados conforme o projeto evolui — este README é atualizado a cada nova tecnologia incorporada.

## Arquitetura

Cada microsserviço é um projeto Spring Boot independente, com seu próprio banco de dados PostgreSQL, versionado separadamente e comunicando-se de forma stateless via tokens JWT assinados com par de chaves RSA:

- **authenticator**: possui a chave privada e pública. É responsável por cadastrar usuários, autenticá-los e **emitir** os tokens JWT.
- **estoque**: possui apenas a chave pública, usada exclusivamente para **validar** a assinatura dos tokens recebidos, sem depender do authenticator em tempo de execução.

Esse desenho permite escalar, versionar e implantar cada serviço de forma independente.

## Tecnologias utilizadas

### Linguagem e build
- **Java 25** — versão utilizada em ambos os serviços (configurada via toolchain do Gradle).
- **Gradle** (com wrapper `gradlew`) — build e gerenciamento de dependências de cada microsserviço, mantidos como projetos Gradle separados.

### Framework
- **Spring Boot 4** — base de ambos os microsserviços.
  - **Spring Web (MVC)** — exposição das APIs REST (controllers).
  - **Spring Data JPA** — persistência e acesso a dados via repositórios, incluindo o uso de **JPA Specifications** no `estoque` para construir filtros de busca dinâmicos (por nome, categoria, quantidade, faixa de preço, etc.) sem a necessidade de múltiplas queries manuais.
  - **Spring Validation** — validação de dados de entrada nos DTOs de request.
  - **Spring DevTools** — reinício automático em ambiente de desenvolvimento.

### Segurança e autenticação
- **Spring Security** — presente nos dois serviços, com sessão stateless (sem uso de sessão HTTP).
- **OAuth2 Resource Server (Spring Security)** — usado para validar tokens JWT.
  - No **authenticator**, a autenticação de login é feita manualmente (usuário/senha) e, após validada, um JWT é **gerado** com a biblioteca **Nimbus JOSE + JWT**, usando um par de chaves **RSA** (carregadas de arquivos `.pem`) para assinar o token.
  - No **estoque**, não há geração de token: o serviço atua puramente como **Resource Server**, validando a assinatura dos tokens recebidos com a chave pública RSA compartilhada.
- **BCrypt** (via Spring Security Crypto) — hash de senhas dos usuários no authenticator.

### Persistência e banco de dados
- **PostgreSQL** — banco relacional de cada microsserviço (um banco dedicado por serviço).
- **Liquibase** — controle de versionamento e migração do schema do banco de dados de cada serviço, através de changelogs XML.

### Mapeamento de objetos
- **MapStruct** — geração automática de mappers entre entidades de domínio e DTOs (request/response), evitando código repetitivo de conversão manual.
- **Lombok** — redução de boilerplate em entidades, DTOs e serviços (getters/setters, construtores, builders).

### Testes
- **JUnit 5** — framework de testes de ambos os serviços.
- **Testcontainers** — subida de instâncias reais de PostgreSQL em containers Docker durante os testes de integração/repositório, garantindo testes mais fiéis ao ambiente de produção.
- **Database Rider** — gerenciamento de datasets (massa de dados) para testes que envolvem o banco de dados.
- **Spring Security Test** e **Spring Security OAuth2 Resource Server Test** — apoio a testes de endpoints protegidos por autenticação/JWT.
- **JaCoCo** — geração de relatórios de cobertura de testes, com verificação automática de cobertura mínima (97%) durante o build (`check`).

### Infraestrutura e ambiente
- **Docker / Docker Compose** — orquestração dos bancos de dados PostgreSQL de cada microsserviço em containers isolados, cada um em sua própria rede e volume de dados.

### Integração contínua
- **GitHub Actions** — pipeline de CI que, a cada push/pull request na branch `main`, compila e executa os testes de cada microsserviço de forma independente (jobs separados para `estoque` e `authenticator`), incluindo a criação das chaves JWT necessárias para os testes a partir de secrets do repositório.

## Estrutura do projeto

```
backend/
├── authenticator/    # Microsserviço de autenticação e usuários
├── estoque/          # Microsserviço de estoque (produtos e categorias)
└── infraestrutura/   # Docker Compose dos bancos de dados
```

## Microsserviços

### authenticator
Responsável pelo cadastro de usuários e pela emissão de tokens JWT utilizados para autenticação nos demais serviços da plataforma.

### estoque
Responsável pelo gerenciamento de categorias e produtos da loja, com suporte a filtros de busca dinâmicos. Protegido como Resource Server, validando os tokens emitidos pelo authenticator.

---

> Este README será atualizado conforme novos microsserviços e tecnologias forem incorporados ao projeto.
