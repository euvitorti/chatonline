# ChatOnline 👩‍💻

## Visão Geral

**ChatOnline** é uma aplicação de chat em tempo real desenvolvida utilizando Java e WebSocket para o backend. HTML, CSS, JavaScript para o frontend. A aplicação permite a comunicação instantânea entre usuários, oferecendo uma experiência de chat interativa.

## Funcionalidades

- **Chat em Tempo Real**: Comunicação instantânea entre usuários conectados.
- **Autenticação**: Sistema de login para gerenciamento seguro de sessões.
- **Envio de Mensagens e Arquivos**: Suporte planejado para envio de mensagens de texto e arquivos.
- **Histórico de Mensagens**: Armazenamento e visualização de mensagens anteriores (funcionalidade futura).
- **Reações com Emoji**: Adição de emojis para reações nas mensagens (funcionalidade futura).
- **Criptografia com WSS**: WebSocket seguro para proteção das comunicações (funcionalidade futura).

## Tecnologias Utilizadas

### Backend

- **Java**: Linguagem de programação principal.
- **WebSocket**: Protocolo para comunicação em tempo real.
- **Spring Boot**: Framework para construção e configuração da aplicação.

### Frontend

- **HTML**: Estrutura básica da aplicação.
- **CSS**: Estilização da interface.
- **JavaScript**: Funcionalidades interativas.

## Estrutura do Banco de Dados

As tabelas do banco de dados necessárias para a aplicação são as seguintes:

```sql
-- Criação da tabela users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela messages
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender
        FOREIGN KEY (sender_id) 
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Criação da tabela media
CREATE TABLE media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message_id BIGINT,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_message
        FOREIGN KEY (message_id)
        REFERENCES messages(id)
        ON DELETE SET NULL
);
```

## Como Funciona o Backend
O backend da aplicação utiliza Java e Spring Boot para gerenciar as comunicações em tempo real através do protocolo WebSocket. Aqui está um resumo das principais funcionalidades do backend:
- Autenticação de Usuários: Utiliza Spring Security para autenticar os usuários. O sistema de autenticação garante que apenas usuários registrados possam acessar as funcionalidades do chat.
- Gerenciamento de Conexões: O backend gerencia as conexões WebSocket, permitindo que múltiplos usuários se conectem e troquem mensagens em tempo real.
- Processamento de Mensagens: As mensagens enviadas pelos usuários são processadas e retransmitidas para todos os usuários conectados. Isso garante que todos recebam as mensagens instantaneamente.
- Segurança: As comunicações são protegidas utilizando WebSocket seguro (WSS), garantindo que as mensagens trocadas entre os usuários permaneçam privadas.

## Como Executar a Aplicação
Pré-requisitos
- Java 21 ou superior
- Maven
- MySQL ou outro banco de dados de sua preferência, caso seja outro, atualize a dependência no pom.xml.
- **Frontend**: O frontend da aplicação deve estar disponível. Você pode encontrar o repositório do frontend em [link do repositório do frontend](https://github.com/euvitorti/chatFront).
- Clone o repositório:
```
git clone https://github.com/euvitorti/chatonline.git
cd chatonline
````

Configure a aplicação:

- Abra src/main/resources/application.properties.
- Atualize a configuração do banco de dados com suas credenciais:
```
spring.datasource.url=jdbc:mysql://localhost/seu_banco_de_dados
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

Execute a aplicação e acesse a aplicação com o front. A aplicação estará rodando em http://localhost:8080.

## Documentação da API
A documentação da API está disponível via Swagger UI. Você pode acessá-la em:
```
http://localhost:8080/swagger-ui.html
```
### Notas
- Sinta-se à vontade para fazer mais ajustes, adicionar informações ou personalizar detalhes conforme necessário. Se precisar de mais alguma coisa, estou à disposição!
