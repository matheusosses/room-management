# 🏨 Room Management API

Uma API RESTful robusta desenvolvida em Java e Spring Boot para a gestão de reservas de salas de reunião. O sistema garante a integridade das marcações, impedindo conflitos de horários e aplicando regras de negócio de domínio rico.

## 🚀 Funcionalidades

* **Gestão de Salas:** Operações de CRUD (Criar, Ler, Atualizar, Detalhar) para salas de reunião.
* **Gestão de Usuários:** CRUD de usuários do sistema.
* **Gestão de Reservas:** * Criação de reservas com validação inteligente de conflito de horários.
  * Proteção contra reservas em datas passadas.
  * Atualização de reservas (total ou parcial) com re-validação de disponibilidade.
  * Cancelamento de reservas (soft delete/alteração de status).
* **Tratamento Global de Erros:** Respostas padronizadas e limpas em JSON (404 Not Found, 400 Bad Request) via `@RestControllerAdvice`.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
* **Persistência:** Spring Data JPA / Hibernate
* **Base de Dados:** MySQL 8.0
* **Migrações/Validações:** Bean Validation (@Valid)
* **Testes:** JUnit 5, Mockito, Spring Boot Test
* **DevOps:** Docker & Docker Compose, Maven

## 📋 Pré-requisitos

Para executar este projeto localmente através de contêineres, precisas apenas de ter instalado:
* Docker Desktop ([https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop))
* Git ([https://git-scm.com/](https://git-scm.com/))

## ⚙️ Como Executar o Projeto

1. Clonar o repositório:

    git clone [https://github.com/teu-usuario/room-management.git](https://github.com/teu-usuario/room-management.git)
    cd room-management

2. Gerar o pacote da aplicação (.jar):
*(Garante que os testes passam e compila o código)*

    ./mvnw clean package

*(No Windows PowerShell, usa .\mvnw clean package)*

3. Subir os contêineres (Base de dados + API):

    docker-compose up -d --build

A API estará disponível e pronta a receber requisições em: http://localhost:8080

## 🛣️ Endpoints Principais da API

### Salas
* POST /salas - Regista uma nova sala.
* GET /salas - Lista todas as salas.
* GET /salas/{id} - Detalha uma sala específica.
* PUT /salas/{id} - Atualiza os dados de uma sala.

### Usuários
* POST /usuarios - Regista um novo usuário.
* GET /usuarios - Lista todos os usuários.
* GET /usuarios/{id} - Detalha um usuário específico.
* PUT /usuarios/{id} - Atualiza os dados de um usuário.

### Reservas
* POST /reservas - Cria uma nova reserva.
* GET /reservas - Lista todas as reservas.
* GET /reservas/{id} - Detalha uma reserva específica.
* PUT /reservas/{id} - Atualiza datas ou sala.
* DELETE /reservas/{id} - Cancela uma reserva.

## 🧪 Como rodar os Testes

O projeto conta com uma suite de testes unitários para Controllers e Services usando Mocks. Para correr os testes isoladamente:

    ./mvnw test

---
Desenvolvido por Matheus Osses
