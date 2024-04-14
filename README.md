# Levo pra você - mobile
> Aplicativo de entregas ou frete

![image](https://github.com/Levo-pra-voce/mobile/assets/77634037/65c6684e-c90e-40f9-9f21-40a50aaf7dc1)

Projeto responsavel por conversa em tempo real entre entregador e cliente, rastreamento de pedidos e relatorio dos pedidos.

## Pré-requisitos
* [Android studio](https://developer.android.com/studio).
* A [api](https://github.com/Levo-pra-voce/backend) do levo pra você estar online

## Inicialização do app
- Escolha um dispositivo android 
- Aperte o botão de inicialização do app ![image](https://github.com/Levo-pra-voce/mobile/assets/77634037/c43afb25-42c4-4359-84ba-3c2de2cba8df) atravès do android studio

## Configuração para desenvolvimento

- Abrir/Criar o arquivo local.properties
- Adicionar a chave `API_URL`com o valor sendo o endereço http da [api](https://github.com/Levo-pra-voce/backend)
- Adicionar a chave `SOCKET_URL`com o valor sendo o endereço websocket da [api](https://github.com/Levo-pra-voce/backend)
### Exemplo de local.properties
![image](https://github.com/Levo-pra-voce/mobile/assets/77634037/5ec794eb-7897-4d2d-be3f-0c98e18392a5)

## Desenvolvedores

[Luiz Eduardo](https://github.com/luiz-eduardo14)
[Raquel Niehues](https://github.com/ahnaoRaquel)
[Eliezer Silva](https://github.com/Eliezer-TEC)
[Gabriel Vitorino](https://github.com/Tr00vuada)

## Modelo de desenvolvimento 

1. Criar branch com o código da feature (`git checkout -b feature/featureCode`)
2. Commitar suas alterações (`git commit -am 'commit message'`)
3. Fazer push para branch remota (`git push origin feature/featureCode`)
4. Criar pull request da feature para branch `develop`

[levo-pra-voce-api-url]: https://github.com/Levo-pra-voce/backend
