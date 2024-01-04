# formacao-kafka-alura
Formação Alura: Mensageria com Apache Kafka
* Link: https://cursos.alura.com.br/formacao-kafka

## 1. Streams, Cluster e Microsserviços:
### O que é Kafka? #HipsterPontoTube - Youtube:
* [![](https://markdown-videos.vercel.app/youtube/G6Tcy7hNdA8)](https://youtu.be/G6Tcy7hNdA8)
* Sistema normalmente chamado de Pub/Sub (Publisher/Subscriber), onde um temos pessoas produzindo eventos e outras consumindo os mesmos.
* Dessa forma não temos um acoplamento entre projetos, além de isolar e deixar o processo assíncrono.
* Podemos fazer com que todos os consumidores inscritos em determinado tópico recebam a mesma mensagem enviada 
* Ou também podemos separar por partições, utilizando um algoritmo de hash em cima de uma chave que disponibilizamos, dessa forma, tudo daquele usuário/sistema específico cai na mesma partição para o mesmo consumir, e mensagens de outras partições, caem para outros consumidores.
* Também podemos deixar esses processos distintos em tópicos separados.
* Apesar do paralelismo, em alguns casos conseguimos fazer com que o Kafka execute essas mensagens respeitando uma ordem. Por exemplo, dentro de um determinado processo de cliente, preciso que as mensagens sejam executadas na ordem que foram enviadas para não causar nenhum problema de sequencia.
* Podemos configurar o Kafka para quando disparamos uma mensagem termos a certeza que o broker a recebeu/armazenou numa partição. Tbm podemos querer saber se essas mensagens foram replicadas em outras partições.
* O Kafka entra quando temos vários serviços que precisam se comunicar de forma paralela, assíncrona e distribuída.

### Começando com Kafka - Hipsters Ponto Talks #12:
* [![](https://markdown-videos.vercel.app/youtube/aaCqcX30pzc)](https://youtu.be/aaCqcX30pzc)
* Exemplo comunicação síncrona/assíncrona:
  * Antigamente tínhamos que resolver tudo por ligação. Para isso, ambas as pessoas precisavam estar disponiveis no momento da ligação. Isso é o que chamamos de comunicação assíncrona.
  * Hoje com a invenção dos apps de comunicação, nós enviamos e recebemos as mensagens, que ficam armazenadas por algum intermediador (whatsapp, face, telegram, etc) e respondemos apenas quando conseguirmos ou estivermos disponiveis. Isso é o que chamamos de comunicação assíncrona.
* Quando temos um processo que não demora para ser processado e retornado, algo mais simples, não precisamos ter essa despesa a mais de incluirmos uma Mensageria no meio. Porém, quando temos um processo mais complexo, que demora mais para ser processado, ou que não podemos ter a certeza que o mesmo será processado, precisamos de uma Mensageria.
* O Kafka também pode ser usado para extração de dados. Conseguimos usar o Kafka para fazer essa migração.
* É também uma plataforma de streaming. Conseguimos fazer processamento de muitos dados em tempo real.
* O Zookeeper serve para gerenciar as instancias do Kafka (coordenação de vários kafkas). Geralmente os dois são utilizados juntos. Então antes de startarmos o Kafka, precisamos startar o Zookeeper.
* O Kafka usa um protocolo chamado TCP, então pra ele, recebe bytes, podendo ser texto, avro, etc.
* Diferente de outras mensagerias, ele também consegue manter os logs (dentro de arquivos) de mensagens por um tempo determinado, podendo reprocessar dados antigos, desde que estejam dentro do tempo de retenção.
* Conseguimos rever as coisas na ordem, devido ao offset, que é um número sequencial que o Kafka atribui para cada mensagem que ele recebe.
* Quando estamos trabalhando com pods escalados, o Kafka irá entregar a mesma mensagem para cada um dos pods, e isso pode causar um problema de duplicação. Para resolvermos isso, utilizamos o tal do Consumer Groups, que é um grupo de consumidores que irão consumir as mensagens de um tópico. Dessa forma, o Kafka irá entregar a mensagem para apenas um dos consumidores do grupo (precisamos definir o nome do grupo no consumidor). Porém, existe um problema, no exemplo de dois pods consumindo as mensagens, sempre apenas o primeiro pod irá receber as mensagens, dependendo da quantidade de partições, que no caso do exemplo acima seria 1. Mas a vantagem desse seria a disponibilidade, pois se o primeiro pod cair, o segundo irá continuar consumindo as mensagens que chegarem.
* No cenário no qual queremos que tenham mais pods up e que esses pods recebam as mensagens de forma intercalada, precisaremos de mais partições.
* Quando queremos que todas as mensagens ficam na mesma partição, utilizamos uma key.


### Kafka: Produtores, Consumidores e streams:
* Link: https://cursos.alura.com.br/course/kafka-introducao-a-streams-em-microservicos

#### 01 - Produtores e consumidores:
* 02 - Mensageria e Kafka:
  * Processamentos sequenciais (sincrono nessa caso é sem mensageria) dependem muito do processamento e sucesso do serviço anterior. 
    * Faz usuário perder tempo esperando. 
    * Gera muito interdependência entre sistemas, pois os sistemas irão precisar conhecer os outros sistemas.
  * Processamentos paralelos (assincrono com mensageria) podemos retornar que o processo está sendo processado e que iremos retornar e atualizar a situação do mesmo conforme os serviços forem sendo executados. 
    * Não demora pra dar o retorno ao usuário.
    * Sistemas não precisam se conhecer, precisam apenas publicar uma mensagem num broker/tópico e quem estiver interessado nessa mensagem, irá consumir.
    * Cada sistema fica responsável por seu próprio processamento.
    * Se um sistema cair, os outros continuam funcionando.
  * Cross Cutting Concerns: São preocupações que temos em todos os serviços, como por exemplo, log, monitoramento, etc. O Kafka nos ajuda a resolver isso.
  * Se algum sistema cair, usando Kafka, não tem problema, pois as mensagens ficam armazenadas por um tempo determinado, e quando o sistema voltar, ele irá consumir as mensagens que ficaram armazenadas.
  * Conseguimos executar também sequencialmente, informando uma Key para que aqueles processamentos ocorram na sequencia.
* 03 - Instalando o Kafka localmente:
  * https://kafka.apache.org/downloads
  * Baixada a versão já construida através do Binary Downloads 3.6.1 Scala 2.13.
  * Comando para descompactar via terminal MAC:```tar zfx kafka_2.13-3.6.1.tgz```
  * Abrir pasta e rodar comando pra rodar o Kafka: ```bin/kafka-server-start.sh config/server.properties```
  * O Kafka é o gerenciador das mensagens. O local onde armazenamos as mesmas se chama Zookeeper.
  * Então iremos precisar executar na ordem:
    * ```bin/zookeeper-server-start.sh config/zookeeper.properties```
    * ```bin/kafka-server-start.sh config/server.properties```
  * Comandos (todos executados na pasta do Kafka):
    * Ver tópicos: ```bin/kafka-topics.sh --list --bootstrap-server localhost:9092```
    * Criar tópico: ```bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic NOME_DO_TOPICO```
    * Criar produtor: ```bin/kafka-console-producer.sh --broker-list localhost:9092 --topic NOME_DO_TOPICO```
      * O terminal vai ficar travado, entao cada mensagem que for enviada, irá aparecer no terminal.
    * Criar consumidor para pegar apenas novas mensagens: ```bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_DO_TOPICO```
      * O terminal irá exibir as mensagens enviadas do outro terminal.
    * Criar consumidor para pegar mensagens desde o inicio: ```bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_DO_TOPICO --from-beginning```
      * O terminal irá exibir as mensagens enviadas do outro terminal.
* 04 - Criando produtores em Java:
  * 

