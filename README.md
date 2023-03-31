# API de Consulta e manutenção de Dados de Taxa de Juros
> Dados relativos a taxas de juros de operações de crédito por instituição financeira do catálogo de dados abertos do sistema financeiro nacional (DASFN) do
Banco Central do Brasil.

A aplicação permite buscar e fazer a manutenção de dados das taxas de juros de operações.

- [Fonte dos dados](https://olinda.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/aplicacao#!/recursos)
- [Documentação](https://olinda.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/documentacao#TaxasJurosMensalPorMes)

## Funcionalidades
Existem 9 endpoints na API para fazer a consulta e a manutenção dos dados:

1. **Consultar por ID**: Buscar especificamente por ID, e assim fazer a visualização de todos os dados da API.
2. **Consultar tudo**: Listar automaticamente todos os dados.
3. **Salvar**: Efetua a criação e salvamento de novos dados para ser inserido no banco de dados! 
4. **Atualizar**: Buscando inicialmente pelo ID, podemos atualizar os dados já existentes, assim, fazendo as modificações que o usuário inserir.
5. **Apagar**: Deletará somente os dados do ID informado.
6. **Consultar taxa de juros mensal por Id**: Ao informar o ID, será retornado especificamente a taxa de juros ao mes da API.
7. **Listar taxa de juros com paginação**: Retorna uma lista paginada com os dados.
8. **Listar taxa de juros por ano e mês**: Filtragem para que seja retornado ao usuário os dados de acordo com o ano e mês que foi passado.
9. **Listar dados por instituição financeira**: Filtragem para que seja retornado ao usuário os dados de acordo com a instituição financeira informada.

## Instalação
Pré-requisitos: 
1. MySQL instalado e configurado adequadamente na maquina local.
2. Uma IDE da linguagem JAVA a sua escolha.
3. Insomnia ou Postman para que suas solicitações HTTP sejam enviadas para uma API para que você possa visualizar as respostas correspondentes. 

Configuração:
1. Clonar o repositório do Git para sua máquina local;
```shell
        https://github.com/carolineoliveiraa/APIBancoCentral.git
```
2. Entrar no application.properties
```shell
       src/main/resources/application.properties
```

3. Colocar o Username e senha do seu Banco de Dados Local nos seguintes campos
 ```shell
        spring.datasource.username="Your usename"
        spring.datasource.password="Your Password"
```

4. Criar um Schema com o nome dados no seu MySQL local

## Informações

**Criadora**: Jéssica Caroline Gonçalves de Oliveira

**Email**: jessicadeveloper@hotmail.com


