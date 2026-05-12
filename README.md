# To-Do List — Backend

**Aplicacao em producao:** https://to-do-list-frontend-ecru-seven.vercel.app

**Repositorio do frontend:** https://github.com/gadelha2005/to-do-list-frontend

**Documentação:** https://to-do-list-backend-production-cef3.up.railway.app/swagger-ui.html

---

API REST para gerenciamento de tarefas com autenticacao JWT. O usuario se cadastra, faz login e pode criar, listar, editar e excluir suas proprias tarefas.

## Tecnologias

- Java 21
- Spring Boot 3.4.5
- Spring Security com JWT (jjwt 0.12.6)
- Spring Data JPA + Hibernate
- MySQL 8
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito

## Funcionalidades

- Cadastro e login com token JWT
- CRUD completo de tarefas por usuario autenticado
- Filtragem de tarefas por status
- Campos: titulo, descricao, status (PENDING, IN_PROGRESS, DONE), prioridade (LOW, MEDIUM, HIGH) e prazo
- Validacao de dados com Jakarta Validation
- Tratamento global de excecoes
- Testes unitarios na camada de servico

## Endpoints principais

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | /auth/register | Cadastro de usuario |
| POST | /auth/login | Login e retorno do token |
| GET | /tasks | Listar tarefas (filtro opcional por status) |
| POST | /tasks | Criar tarefa |
| GET | /tasks/{id} | Buscar tarefa por ID |
| PATCH | /tasks/{id} | Atualizar tarefa parcialmente |
| DELETE | /tasks/{id} | Excluir tarefa |

Documentacao completa disponivel em `https://to-do-list-backend-production-cef3.up.railway.app/swagger-ui.html` com autenticacao Bearer.

## Como rodar localmente

**Pre-requisitos:** Java 21, Maven, MySQL 8

1. Clone o repositorio
2. Crie um banco de dados MySQL chamado `to_do_list`
3. Configure as credenciais em `src/main/resources/application.yml`
4. Execute:

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Testes

```bash
./mvnw test
```

Os testes usam H2 em memoria e nao dependem de banco externo.

## Deploy

Backend hospedado no Railway com MySQL provisionado na mesma plataforma. As credenciais de producao sao injetadas via variaveis de ambiente.
