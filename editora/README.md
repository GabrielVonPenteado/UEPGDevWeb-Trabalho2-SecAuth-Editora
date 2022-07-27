# Descrição da API

## Configurações iniciais

### XML
No arquivo pom.xml temos as configurações do projeto, onde estão a conexão com o apache, as dependências e os plug-ins.

### Aplication Properties
No arquivo aplication.properties temos as configurações de acesso ao banco de dados, onde temos a url de acesso, username, senha e a linguagem que conversará com o banco. Também temos uma correção de bug e uma configuração que, ao se modificar uma classe na aplicação, essa classe se altere no banco também.

## Classes de Modelo

- Artigo: 


## Classes Repositório

Nelas temos todos os métodos paara buscar e recuperarmos dados das tabelas(classes) dentro do banco de dados.

- ArtigoRepository:


## Classes Controle

Nelas que temos os métodos GET, POST, DELETE, PUT etc onde podemos utilizar os métodos dos repositórios criados.

- ArtigoController:


- HomeController:


## Classes Segurança

- CognitoOidcLogoutSuccesHandler:


- SecurituConfiguration:


## Templates

- index.html



## Testes

Para os testes foi usado a IDE pgAdmin para verificarmos o que está acontecendo no banco. Ao rodarmos a aplicação no Maven, as tabelas já foram criadas automaticamente no banco secauth criado, com suas colunas e chaves primárias e estrangeiras devidamente atribuidas.
