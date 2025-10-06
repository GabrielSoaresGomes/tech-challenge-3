# 🏥 Tech Challenge 03 — Medical Scheduling Platform

## 📖 Visão Geral

O Tech Challenge 03 é um sistema distribuído baseado em microserviços desenvolvido com Java 21 e Spring Boot, que gerencia o fluxo completo de agendamentos médicos — desde autenticação e criação/cancelamento até notificações e histórico de alterações.

A arquitetura utiliza Spring Cloud Gateway, RabbitMQ, MailHog e GraphQL, com persistência em bancos PostgreSQL e execução orquestrada via Docker Compose.

## 🧩 Arquitetura dos Serviços

### 🌐 Gateway Service

Porta: 8080

Imagem: tc3/gateway-service:local

Depende dos serviços:

auth-service

scheduling-service

Responsável por:

Roteamento e balanceamento de requisições (via Spring Cloud Gateway WebFlux)

Controle de autenticação e passagem do token JWT entre microserviços

### 🔐 Auth Service

Porta: 8081 (interna)

Banco: tc3-auth-postgres

Porta: 5433 (exposta)

Database: tech-challenge-03-auth

Responsável por:

Autenticação e autorização via Spring Security + JWT

Gerenciamento de usuários e perfis

### 🩺 Scheduling Service

Porta: 2003

Banco: tc3-scheduling-postgres

Porta: 5434

Database: tech-challenge-03-scheduling

Depende de:

rabbitmq (fila de mensagens)

auth-service (validação de token e usuários)

history-service (para registrar histórico via GraphQL)

Responsável por:

Criação e cancelamento de agendamentos médicos

Registro de mensagens no RabbitMQ para envio de notificações

Comunicação com o History Service para registro das alterações

### 📨 Notification Service

Imagem: tc3/notification-service:local

Depende de:

scheduling-service

mailhog (SMTP fake)

rabbitmq

Responsável por:

Consumir mensagens do RabbitMQ

Enviar e-mails de criação/cancelamento de consultas para os pacientes

Usa o MailHog como servidor SMTP local

### 🕒 History Service

Porta: 2002

Banco: tc3-history-postgres

Porta: 5542

Database: history_service

Responsável por:

Registrar e consultar históricos de alterações de agendamentos

Expor e consumir dados via GraphQL

### 🐇 RabbitMQ

Porta de aplicação: 5672

Painel de administração: http://localhost:15672

Usuário/Senha: user / 123456

Responsável pela mensageria entre o scheduling-service e o notification-service

### 📬 MailHog

SMTP fake server

Porta SMTP: 1025

Painel Web: http://localhost:8025

Usado para visualizar os e-mails enviados pelo notification-service durante o desenvolvimento

## ⚙️ Tecnologias Utilizadas
Categoria	Tecnologia
Linguagem	Java 21
Framework	Spring Boot 3.x
Gateway	Spring Cloud Gateway (WebFlux)
Segurança	Spring Security + JWT
Mensageria	RabbitMQ
Comunicação	GraphQL
E-mail	MailHog (SMTP fake)
Banco de Dados	PostgreSQL
Contêinerização	Docker e Docker Compose

## 🧰 Pré-requisitos

Docker

Docker Compose

(Opcional) Java 21+
 caso queira rodar os serviços localmente

## 🚀 Subindo o ambiente completo

Execute o comando: docker-compose up --build;

Acesse o serviço de autenticação e crie um usuário com a role doctor e patient;

Faça login com o doctor e faça um agendamento;

## 🔁 Fluxo de Funcionamento

O usuário autentica-se via Auth Service e recebe um token JWT.

O Gateway Service valida o token e encaminha a requisição para o Scheduling Service.

O Scheduling Service cria/cancela um agendamento e:

Envia mensagem ao RabbitMQ.

Registra o histórico no History Service via GraphQL.

O Notification Service consome a mensagem e envia o e-mail via MailHog.

O desenvolvedor pode visualizar o e-mail simulado em:
👉 http://localhost:8025
