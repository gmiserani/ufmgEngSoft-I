## Dependencias

- [Java Developer Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Make

## Comandos make

- `make` ou `make full`: Buildar + Executar
- `make build`: Buildar
- `make run`: Executar
- `make clean`: Limpa os arquivos `.class` gerados no build

## Como rodar

- Na root do repositório use o comando `make` para buildar e executar o programa

## Como utilizar

OBS:

- O sistema já vem inicializado com 2 candidatos a presidente; 3 a deputado federal, sendo dois de MG e um de SP; 3 a governador, com dois de MG e um do ES; 3 ao senado, com 2 de MG e um do ES; 3 a prefeitura, com dois de BH e um de Uberlândia.
- O sistema já vem com os dois gestores (de sessão e de candidaturas)
- O sistema já vem com todos os eleitores possíveis para utilizá-los basta checar o arquivo `voterLoad.txt`

No menu inicial para gerenciar candidatos e eleição siga pela opção 2:

- User: `emp` , Password: `12345` -> Cadastro e remoção de candidatos da eleição
- User: `cert` , Password: `54321` -> Inicialização/finalização da eleição (liberar pra poder votar) e mostrar o resultado ao final da eleição.

Além da senha de usuário é necessário a senha da eleição para completar operações relacionadas a gestão da eleição ou candidatos. Essa senha é a palavra `password`

Para votar também existe um eleitor com o título de eleitor nº 123456789012 que pode votar nos candidatos pré-cadastrados

## Execução teste

Para uma execução teste podemos seguir o seguinte passo:

- Ao iniciar a aplicação selecionar a opção 2 e logar com o user `cert`
- Escolher a opção 1 e inserir a senha da urna (`password`) para iniciar a votação
- Escolher a opção 0 para voltar ao menu inicial
- Escolher votar (opção 1) e inserir o nº `123456789012` do eleitor de teste
- Insira a cidade do eleito
- Aperte 1 para confirmar o usuário
- Inicializa então a votação. Nela pode inserir o número do candidato, "br" para votar branco ou 0 para votar nulo.
- A primeira votação é a do presidente, onde pode escolher qualquer cadidato ('123', por exemplo). Uma vez iserido o voto, basta apertar 1 para confirmar
- A partir de agora, os candidatos dependem do estado.
- A segunda é o primeiro de 2 para deputado federal. Essa votação seguem como a do presidente, com a diferença de que, caso seja inserido o número de um candidato de um estado diferente do estado do eleitor, retornará uma mensagem de erro dizendo que tal candidato não foi encontrado. 
- As votações seguintes seguem a mesma ideia.
- Uma vez que votar para todos os cargos, você será retornado ao inicio, onde poderá realizar uma nova votacao com um novo eleitor ou encerrar a eleição.
- Nesse último caso basta:
- No menu inicial, selecionar a opção 2 e logar com o user `cert`
- Escolher a opção 2 e inserir a senha da urna (`password`) para encerrar a votação
- Escolher a opção 3 e inserir a senha da urna (`password`) para mostrar o resultado final da votação
- Escolher a opção 0 duas vezes para encerrar a aplicação
