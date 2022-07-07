# SRH
## Uso do Docker compose
Primeiramente, certifique-se de ter o `docker` e o `docker compose` instalados no sistema
```shell
$ docker version
$ docker compose version
```


Na raiz do projeto:
1. Crie um arquivo `.env`
2. Copie o conteúdo do arquivo `.env-template` para o arquivo `.env`
3. Altere os valores das variáveis de acordo com a necessidade
   - Valor de `APP_LOCAL_*_PATH` devem ser paths existentes na máquina do usuário
   - Valor de `NEXUS_USER` e `NEXUS_PASS` são username e senha do usuário do AD
4. Execute o comando `docker compose up --build`
   - Logo após o build, os logs da aplicação ficarão visíveis no terminal
5. Acesse no navegador o endereço `localhost:APP_LOCAL_PORT`, subtituindo a variável pelo valor definido no arquivo `.env`
6. Para encerrar o container, pressione `Ctrl + C`

## Variáveis de ambiente no Compose
Para passar variáveis de ambiente no build da imagem, deve-se tê-las declaradas nos arquivos:
- `.env`, no formato `VARIABLE=value`
- `.Dockerfile-compose`, no formato `ARG VARIABLE` e depois `ENV VARIABLE=$VARIABLE`
- `docker-compose.yml`, no campo `args`, no formato `'VARIABLE=${VARIABLE}'`

Para passar variáveis de ambiente para o container criado pelo `docker compose`, por sua vez:
- `.env`, no formato `VARIABLE=value`
- `docker-compose.yml`, usa-se `env_file: .env`

Quando o usuário alterar, remover ou adicionar variáveis, deve-se ficar atento para os campos e arquivos citados, caso contrário, há grande possibilidade de ocorrerem erros na execução da aplicação.

## Possíveis erros
Porta
- Ao subir o container, pode haver um conflito se a porta definida em `APP_LOCAL_PORT` já estiver sendo usada na máquina local. Para conferir isso, execute o comando
    ```shell
    $ sudo netstat -plant | grep porta
    ```
- Se a porta já estiver sendo usada por alguma aplicação, altere para outra e tente novamente.

DNS
- Se o usuário estiver usando a VPN para se conectar à rede do TCE, podem ocorrer erros na atualização dos pacotes no build do Dockerfile, sendo exibida a mensagem `Couldn't resolve host 'packages.tce.ce.gov.br'`
- Para contornar o problema, reinicie o service do docker com o comando abaixo e execute o `docker compose` novamente
    ```shell
    $ sudo systemctl restart docker
    ```
