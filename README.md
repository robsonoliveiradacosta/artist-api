# artist-api

### Como rodar o Keycloak (com banco de dados H2 configurado com dados iniciais)

```
# acesse a pasta do projeto no terminal/cmd
cd artist-api

# crie uma imagem customizada do keycloak
docker build -f keycloak/Dockerfile -t robsondeveloper/artist-keycloak:1 .

# execute um container docker da imagem customizada do keycloak (ambiente LINUX)
docker run -d -p 8080:8080 -v $(pwd)/keycloak/h2-data/:/opt/jboss/keycloak/standalone/data/ robsondeveloper/artist-keycloak:1

# execute um container docker da imagem customizada do keycloak (ambiente WINDOWS)
docker run -d -p 8080:8080 -v CAMINHO-PASTA-PROJETO/keycloak/h2-data/:/opt/jboss/keycloak/standalone/data/ robsondeveloper/artist-keycloak:1
