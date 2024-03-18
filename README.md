# formacao-kafka-alura
Formação Alura: Mensageria com Apache Kafka
* Link: https://cursos.alura.com.br/formacao-kafka

## 1. Streams, Clust er e Microsserviços:
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
  * Criado projeto em Java 17 + Maven para seguir exemplos: [ecommerce](ecommerce)
  * Adicionada dependencias no pom do projeto:
    * kafka-clients.
    * slf4j-simple.
  * Desenvolvimento do exemplo sendo feito na classe Main.
  * O Future do send não é um retorno, é uma promessa de retorno. O que o torna assíncrono.
  * Podemos implementar o send com uma função que receba o callback para termos um retorno do que foi feito.
  * Basta rodar a aplicação para publicar as mensagens.
  * Rodar um consumidor para pegar as mensagens publicadas no ECOMMERCE_NEW_ORDER.
* 05 - Criando consumidores em Java:
  * Criaremos um serviço para detecter fraude chamado FraudDetectorService.
  * Realizada a configuração do Consumer.
  * Precisamos definir o GroupId para que o Kafka saiba que esse consumidor faz parte de um grupo de consumidores e que receba todas as mensagens daquele tópico.
  * Se dois serviços tiverem o mesmo grupo, então as mensagens são distribuidas e não saberemos qual serviço vai receber qual mensagem.
* 06 - Produtores x Consumidores:
  * Em um sistema bancário, um usuário inicia o processo de uma transação bancária. Qual abordagem é baseada em produtores e consumidores de mensagens?
    * A requisição é feita por um site ou app cujo servidor envia uma mensagem de pedido de transação bancária.
    * Essa abordagem mistura o processo síncrono e a mensagem.
#### 02 - Paralelizando tarefas em um serviço:
* 02 - Vários consumidores e produtores:
  * Queremos despachar o pedido e também um e-mail, criando um novo produtor.
  * Na classe Main, realizamos o envio de uma mensagem para o tópico ECOMMERCE_SEND_EMAIL.
  * Criamos um novo consumidor para o tópico ECOMMERCE_SEND_EMAIL na EmailService.
  * Criamos também o LogService, que irá ouvir todos os tópicos para logar as mensagens. Ele utilização de uma expressão regular para ouvir todos os tópicos que tenham ECOMMERCE.
  * Quando enviarmos uma mensagem no Email e no Order Service, cada consumidor irá pegar 1 mensagem, mas o LogService pegará ambas. Com isso, criamos um grupo de mensagem para cada tópico e cada um receberá todas as mensagens que forem enviadas.
* 03 - Paralelizando e a importância das keys:
  * Quando tempos grupos de consumo diferentes entre os Consumidores, o Kafka irá entregar a mensagem do tópico para todos os consumidores do grupo.
  * Porém, queremos que o serviço de Fraude, esteja rodando em dois "pods" (nesse caso, a classe foi rodada duas vezes).
  * Dentro de um grupo, o Kafka irá entregar a mensagem para apenas um dos consumidores do grupo, pois não queremos que o código seja executado duas vezes. Porém, nesse caso, como temos apenas 1 partição, apenas o primeiro consumidor irá receber as mensagens, enquanto o segundo não receberá nada.
  * Para reparticionar as mensagens, precisamos criar mais partições. Para isso, teremos que editar o server.properties e adicionar a configuração ```num.partitions=3```. 
  * Descrever os tópicos: ```bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe```.
  * Só que esses que já existem, não serão alterados.
  * Teremos que alterar o tópico para que ele tenha 3 partições: ```bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic ECOMMERCE_NEW_ORDER --partitions 3```.
  * Ao rodarmos novamente a parte dos consumidores, veremos agora que um ficará responsável por 2 partições e o outro por 1, totalizando as 3, rebalanceada.
  * Porém, ainda assim, o Kafka não entregará mensagem para a partição diferente da 0.
  * Por no exemplo estarmos usando a mesma chave, o Kafka irá entregar a mensagem para a mesma partição, pois ele usa um algoritmo de hash para isso.
  * Então, utilizamos um UUID para gerar uma chave aleatória para cada mensagem, dessa forma, o Kafka irá distribuir as mensagens entre as partições.
  * Conseguimos visualizar os grupos de consumidores utilizando o comando: ```bin/kafka-consumer-groups.sh --all-groups --bootstrap-server localhost:9092 --describe```.
  * Dentro de FraudDetectorService, definimos um nome personalizado do Consumidor setando o CLIENT_ID_CONFIG, concatenando o nome da classe + UUID.
  * O Kafka pode decidir rebalancear as partições e isso pode causar um erro de processamento das mensagens que ainda estão sendo consumidas e isso pode ser um problema, que veremos como resolver na proxima aula.
* 04 - Max poll e dando mais chances para auto commit:
  * Esse problema está relacionado ao tempo de commit. O Kafka espera um tempo para fazer o commit das mensagens, e se o tempo for muito alto, ele pode rebalancear as partições e perdermos mensagens.
  * Dentro do consumer.poll(Duration.ofMillis(100)) é um momento que ocorre esse commit. Porém existem outras que veremos futuramente.
  * Dessa forma, precisaremos adicionar uma nova propriedade para acessar o poll mais frequentemente, para que o balanceamento não afete tanto o recebimento das mensagens.
  * Dentro do FraudDetectorService, adicionamos a propriedade MAX_POLL_RECORDS_CONFIG com valor 1, para informar que queremos que o poll seja feito a cada mensagem recebida. Assim, temos chances menores de perdermos mensagens recebidas.
  * A medida que o consumidor for consumindo as mensagens, é feita o commit e em caso de rebalanceamento, não perderemos as mensagens.
* 06 - Qual a importância das chaves na parelelização de tarefas?
  * Ela é peça fundamental para parelelizar o processamento de mensagens em um tópico dentro do mesmo consumer group.
  * A chave é usada para distribuir a mensagem entre as partições existentes e consequentemente entre as instâncias de um serviço dentro de um consumer group.
#### 03 - Criando nossa camada:
* 02 - Extraindo uma camada de consumidor:
  * Criamos uma classe chamada KafkaService para abstrair o consumidor.
  * Todo o código repetido das classes de EmailService e FraudDetectorService, foi extraido para a classe KafkaService.
  * Parte de properties tbm.
* 03 - Extraindo nossa camada de producer:
  * Da mesma forma que extraimos o consumer acima, iremos extrair o service de producer.
  * Criamos a classe KafkaDispatcher para abstrair a parte do Producer e deixar a classes NewOrderMain sem vinculos com o Kafka.
  * Criamos um método send para enviar as mensagens.
  * Também abstraimos a parte de properties.
  * E por ultimo criamos o close para fecharmos o producer, em caso de erro ou sucesso, para não deixar o recurso aberto.
  * Foi feita a mesma configuração no consumer.
* 05 - Our own layer:
  * Qual a vantagem de criar nossa própria camada?
    * Adotar boas práticas como evitar código duplicado.
    * Definir padrões, boas práticas e evitar más práticas, permitindo novos/as devs começar a desenvolver rapidamente código pronto para produção.
#### 04 - Serialização customizada:
* 02 - Diretórios do Kafka e do Zookeper:
  * O Kafka armazena os dados em disco, e por padrão, ele armazena em /tmp (mac e linux).
  * Em qualquer sistema operacional, quando armazenamos algo no /tpm, ele pode acabar sendo apagado, pois é um diretório temporário.
  * Podemos então criar duas pastas (pode ser na pasta anterior do Kafka), uma para o Kafka e outra para o Zookeper:
    * mkdir data/kafka
    * mkdir data/zookeeper
  * Ai preciraremos editar os arquivos de configuração do kafka e do zookeeper para apontar para essas pastas:
    * config/server.properties:
      * ```vi config/server.properties```
      * log.dirs=/Users/rodrigo/Developer/kafka/data/kafka
    * config/zookeeper.properties
      * ```vi config/zookeeper.properties```
      * dataDir=/Users/rodrigo/Developer/kafka/data/zookeeper
    * Se quisermos deletar os logs do temp, rodamos o comando: ```rm -rf /tmp/kafka-logs/ /tmp/zookeeper/```
    * Rodamos o zookeeper e o kafka novamente através dos comandos:
      * ```bin/zookeeper-server-start.sh config/zookeeper.properties```
      * ```bin/kafka-server-start.sh config/server.properties```
    * Consegui rodar o zookeeper e kafka agora, dps de alterar a pasta acima.
* 03 - Serialização com GSON:
  * A ideia dessa aula foi implementar o GSON para serializar os objetos que estamos enviando. Tivemos que criar a classe GsonSerializer para receber o tipo generico <T> e serializar o objeto. Dessa forma, conseguimos enviar a String ou a classe Order criada ou qualquer outra classe futuramente criada para realizar esse envio.
  * Na NewOrderMain, foi necessário criar dois dispatcher, um para enviar a String e outro para enviar o objeto Order.
  * Na KafkaDispatcher, foi necessário alterar o atributo de serialização para o genérico.
* 04 - Migrando o log:
  * Migrado o consumidor do LogService e melhorando construtores do KafkaService para receber o pattern de regex.
* 05 - Deserialização customizada:
  * Basicamente foi criada a classe de GsonDeserializer para transformar as mensagens em algum tipo de Classe, junto com a lógica de deserialização implementada em cada um dos consumidores através do construtor do KafkaConsumerMessage, onde cada serviço deverá informar qual o tipo de classe que ele irá receber para ser deserializada.
  * Para representar essas classes genéricas, foi utilizado o tipo T que usa-se em tempo de compilação.
  * Porém, a aplicação continuará dando erro na parte de LogService por ainda não estar preparada para receber um Objeto no lugar da String.
* 06 - Lidando com customizações:
  * Quando queremos utilizar vários subjects num consumidor, que é o caso do LogService, devido à atualmente estar recebendo String e Order, pode ocorrer um problema.
  * Isso não é um cenário que geralmente ocorre, pois um consumidor geralmente é responsável por um tipo de mensagem apenas.
  * Para resolver, foi necessário passar um novo parâmetro de propriedades extras no LogService, onde informamos que ao invés do GsonDeserializer, queremos utilizar o StringDeserializer.
  * Essa configuração é setada através do overrideProperties no setProperties do KafkaConsumerMessage.
#### 05 - Microsserviços e módulos:
* 02 - Microsserviços como módulos em um mono repo:
  * Cada um dos serviços que desenvolvemos, poderia ser um projeto separado, porém, para facilitar o desenvolvimento, podemos colocar todos os serviços dentro de um mesmo projeto, e cada um deles ser um módulo.
  * Temos algumas formas de separar esses escopos, como criando novos projetos, ou separando esse projeto em módulos, utilizando o maven ou gradle.
  * Realizamos a criação de um novo módulo para cada recurso, ficando: common-kafka, service-order, service-email, service-fraud-detector, service-log e movemos suas respectivas classes para dentro dos mesmos.
  * As classes que ficaram em comum foram movidas para o common-kafka.
  * Depois dentro de cada modulo, realizamos a importação dos módulos necessários para utilizar a aplicação.
  * Outra dica é mover utilizando o refactor do IntelliJ, para que ele já atualize as importações corretamente.
* 03 - Binários dos microsserviços:
  * Não tem problema compartilharmos a classe Order entre os serviços. Porém, o problema seria se algum dos serviços precisasse de uma propriedade nova do Order, estariamos forçando que os demais módulos também utilizassem essa, teriamos que atualizar todos os módulos.
  * Isso cria uma certa dependência entre os módulos, pois toda vez que quisessemos lançar uma versão nova, teriamos que esperar a correção do Order ser feita.
  * Como a Order é uma classe de modelo simples, não teria problema duplicarmos elas entre os módulos.
  * Realizados testes, subindo o Zookeeper e o Kafka, o mesmo funcionou corretamente produzindo e consumindo através dos tópicos.
  * Agora iremos empacotar o projeto.
  * Iremos no Maven (canto direito) > ecommerce (root) > maven package.
  * Será gerado o jar de cada um desses módulos.
* 05 - Bibliotecas comuns:
  * Qual a vantagem de extrair bibliotecas comuns?
    * Evitar duplicação de código.
    * Múltiplos projetos se beneficiam da mesma base de código.
    * Isso permite que devs foquem nos requirimentos únicos de seu projeto.

### Kafka: Fast delegate, evolução e cluster de brokers:
* Link: https://cursos.alura.com.br/course/kafka-cluster-de-microservicos

#### 01 - Novos produtores e novos consumidores:
* 02 - Produtores consumidores e o eager de patterns:
  * No [FraudDetectorService.java](ecommerce%2Fservice-fraud-detector%2Fsrc%2Fmain%2Fjava%2Fbr%2Fcom%2Fgabrieldragone%2FFraudDetectorService.java) iremos simular a situação onde alguns pedidos irão ocorrer com sucesso e outros não.
  * A validação que iremos inserir é se o value da order for maior que 4500, iremos simular que o pedido foi fraudulento.
  * Uma observação, se estivessemos utilizando a classe de modelo java normal (sem ser o record) é que agora teriamos duas classes Order, ambas com os mesmos atributos, porém uma dela semos métodos de getters e setters.
  * Além de consumir as mensagens, agora no FraudDetector iremos enviar mensagens de sucesso e de erro.
  * Dai temos duas opções, ou paramos o serviço, jogando a exception pra cima nos métodos, ou utilizamos o try/catch e tratamos a mensagem de erro.
  * Quando adicionamos novos tópicos, para o [LogService.java](ecommerce%2Fservice-log%2Fsrc%2Fmain%2Fjava%2Fbr%2Fcom%2Fgabrieldragone%2FLogService.java) receber as mensagens, devido ao patterns, se surgir um novo tópico, ele não irá ouvir automaticamente, sendo necessária a reinicialização do mesmo para que ele possa ouvir o novo tópico.
* 03 - Um serviço que acessa bancos externos:
  * Criada classe responsável pela criação de usuarios [CreateUserService.java](ecommerce%2Fservice-users%2Fsrc%2Fmain%2Fjava%2Fbr%2Fcom%2Fgabrieldragone%2FCreateUserService.java).
  * Utilizando SQLite, pois o intuito do curso não é focar em banco de dados, mas sim em Kafka.
  * Preparada a conexão e criação de arquivo para representar o banco de dados que será salvo dentro de ecommerce/target/users_database.db.
  * Feita a lógica de criação de usuário e validação de usuário existente.
  * Também foi realizada a alteração das exceptions lançadas no Consumer, deixando as mesmas mais genéricas. Porém, isso não é o ideal.
  * Paramos no problema entre schemas que vão sendo levados durante a evolução dos serviços.
#### 02 - Evoluindo um serviço:
* 02 - Evoluindo serviços e schemas:
  * Precisamos pensar se os processos que foram iniciados em paralelo foram ou não executados e se a ordem dessa execução não quebre nenhum processo do sistema.
  * Os sistemas devem estar preparados para caso um dado ainda não esteja pronto devido à algum sistema que não tenha sido executado ainda.
  * Dentro do domain Order do service-order, adicionamos o email e enviamos a mensagem para os tópicos.
  * Os serviços que tem a Order sem o email implementado, vão basicamente ignorar o campo novo e trabalhar apenas com os campos informado em sua versão da Order.
  * E os que precisam, como é o caso do service-users, irão implementar o campo e fazer o que quiser com o dado novo.
* 03 - Escolhendo o id adequado:
  * A maneira que estavamos tratando a id do usuário não era a melhor, pois a todo momento estávamos gerando um novo id para o usuário. O que deveria ser gerado são pedidos diferentes e um usuário poderia ter 1 ou mais pedidos.
  * Dai ao invés de usarmos o id do usuário, utilizamos o email do usuário para identificar o mesmo. Para isso, tivemos que arrancar o id do usuário do Order.
  * No service-users, precisamos retirar o id tbm e adicionar no momento da criação do usuário apenas se o mesmo ainda não existir. Assim estamos atribuindo corretamente a id do usuário para o serviço que é realmente responsável por isso.
  * O serviço do fraud detector também não irá precisar mais da id e o agrupamento será feito por email.
  * Agora se rodarmos a aplicação, na parte do user service, o mesmo será criado apenas uma vez, enquanto nos demais o processamento continuará normal.
#### 03 - Servidor HTTP:
* 02 - Usando um servidor http como ponto de entrada:
  * A ideia é fazer com que o gatilho para a geração das mensagens seja feita de forma manual através de algum acionamento http.
  * Então foi criado um novo módulo [service-http-ecommerce](ecommerce%2Fservice-http-ecommerce) que utilizará a biblioteca http jetty servlet que dentro tem a jetty core.
  * Com isso, implementamos o [HttpEcommerceService.java](ecommerce%2Fservice-http-ecommerce%2Fsrc%2Fmain%2Fjava%2Fbr%2Fcom%2Fgabrieldragone%2FHttpEcommerceService.java) que é responsável por subir um servidor na porta 8080 e o [NewOrderServlet.java](ecommerce%2Fservice-http-ecommerce%2Fsrc%2Fmain%2Fjava%2Fbr%2Fcom%2Fgabrieldragone%2FNewOrderServlet.java) que é como se fosse um controller que irá receber as requisições e enviar as mensagens para o Kafka.
  * Além disso, fizemos de forma dinâmica, para receber o email e valor via parametros do request.
* 03 - Fast delegate:
  * Quanto mais código incluirmos dentro do método do servlet, maior a change de ocorrer uma falha e perdermos a compra ou a requisição da pessoa usuária final.
  * O método de fast delegate é utilizado para que possamos fazer a chamada do método de forma assíncrona, para que o servidor não fique travado esperando a resposta do Kafka.
  * É basicamente delegar de maneira rápida a execução do método para que o servidor possa continuar atendendo outras requisições.
  * Quando o processo é finalizado, podemos de diversas formas avisar o usuário que o processo foi finalizado.
  * A ideia do ponto de entrada é ter o mínimo de código possível, mínimo de processamento, para deixar que a mensagem faça tudo por nós.
  * Poderiamos nesse exemplo, fazer com que a criação do pedido e envio de e-mail fossem feitos em serviços separados.
#### 04 - Cluster de brokers:
* 02 - Single point of failure do broker:
  * Por mais que levantemos diversas instancias dos services, fazendo com que eles deixem de ser um ponto de falha, o Kafka ainda é um ponto de falha, pois ele é único.
  * Se ele cair, todos esses serviços com diversas instâncias não irão conseguir estabelecer uma conexão junto ao Kafka.
  * Se tentarmos enviar uma mensagem, devido ao producer.send.get() ter o comportamento de esperar, ele vai aguardar até que o Kafka volte, e isso pode ser um problema, pq irá travar o sistema até que o Kafka volte.
  * A partir do momento que o Kafka voltar, os serviços que estão esperando para enviar as mensagens, irão enviar todas de uma vez.
  * Ainda em relação aos serviços, como eles trabalham com tópicos que tem 3 partições, se um serviço cair, o outro assume as partições do serviço que caiu. Quando os serviços sobem, é rebalanceado as partições.
* 03 - Replicação em cluster:
  * Iremos configurar um segundo servidor de Kafka para que ele possa ser um backup do primeiro, simulando dessa forma um escalonamento.
  * Para isso, precisamos criar um novo arquivo de configuração para o segundo servidor, copiando o server.properties e renomeando para server2.properties:
  * ```cp config/server.properties config/server2.properties```
  * Editamos o arquivo para que ele aponte para uma porta diferente, no caso, a 9093:
  * ```code config/server2.properties```
  * Alteramos o broker.id para 2, pois o primeiro já é 0 e decidimos que pra um melhor entendimento e identificação iremos pular o 1.
  * Alteraremos tbm o log.dirs para que ele aponte para uma pasta diferente, para que não haja conflito com o primeiro servidor:
    * log.dirs=/Users/{meuUser}/Documents/Projects/Pessoal/data/kafka2
  * E por fim, alteramos o listeners para que ele escute na porta 9093:
    * listeners=PLAINTEXT://:9093
  * Como é um exemplo local, não tem problema definirmos portas diferentes, porém, em produção, as portas serão as mesmas, porém, os ips serão diferentes e o direcionamento será feito provavelmente por um load balancer.
  * Para levantar o server 2, basta seguir os atalhos do final da pagina.
  * Utilizando todos os serviços ups e gerando as mensagens, podemos derrubar o server 1 pra ver como vai ser o comportamento do server 2. Mas, por enquanto, esse comportamento não será o esperado, pois os serviços não conseguirão se conectar com o broker do node 0 e o funcionamento desses serviços só irão voltar após subirmos novamente o server 1.
  * Esse comportamento ocorre pois todas as partições estão no leader do server 1.
  * Precisaremos alterar a configuração do tópico para que ele tenha 3 partições e 2 replicas:
    * ```bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic ECOMMERCE_NEW_ORDER --partitions 3 --replication-factor 2```
    * Porém, esse comando vai dar problema, pois o tópico já existe e não podemos alterar o número de partições de um tópico que já existe.
  * Dessa forma, teremos que alterar a configuração dos dois kafkas, adicionando o replication-factor=2 dentro do arquivo de configuração.
  * Então precisaremos reiniciar a configuração do Kafka do zero, removendo os arquivos de log do data:
    * ```rm -rf data/kafka/*```
    * ```rm -rf data/zookeeper/*```
  * Agora basta iniciar o zookeeper, kafka1 e kafka2.
  * Se olharmos os tópicos, veremos que agora temos 3 partições e 2 replicas e essas partições estão nos dois kafkas.
  * Agora, se derrubarmos o kafka1, o kafka2 irá assumir as partições e os serviços irão continuar funcionando.
  * E quando subirmos novamente o kafka1, os serviços irão rebalancear as partições, porém existe um ponto de falha referente ao consumer offsets, devido ao offset ser salvo no kafka, e se o kafka cair, o offset é perdido, por causa do ReplicationFactor 1.
* 03.01 - Ajustes nos projetos:
  * Como o service-http-ecommerce não estava funcionando com a biblioteca de Servlet, resolvi criar um novo projeto usando Kotlin e Spring Boot para fazer a mesma coisa e substituir o mesmo.
  * Além disso, no common-kafka, precisei adicionar a dependencia do Spring para que eu conseguisse importar o projeto no Spring sem dar erro de bean type.
  * Também consegui configurar, pelo menos via Windows, a parte dos serviços via docker-compose.
  * Configurado para subir dois kafkas, e dessa forma, conseguir usar o replication factor = 2.
* 04 - Cluster de 5 brokers e explorando líderes e réplicas:
  * Iremos alterar os arquivos de configuração para aumentar:
    * ```code config/server.properties```
    * offsets.topic.replication.factor=3.
    * transaction.state.log.replication.factor=3.
    * default.replication.factor=3.
  * Deletamos o server2, mas iremos criar mais 4:
  * ```rm config/server2.properties```
  * Movemos o arquivo server.properties para server1.properties:
    * ```mv config/server.properties config/server1.properties```
  * Criamos mais 3 brokers:
    * ```cp config/server1.properties config/server2.properties```
    * ```cp config/server1.properties config/server3.properties```
    * ```cp config/server1.properties config/server4.properties```
  * Para cada um desses brokers, iremos editar o arquivo e setar as configurações nos atributos:
    * ```code config/server{numeroBroker}.properties```
      * broker.id={numeroBroker}
      * log.dirs=/Users/{meuUser}/Documents/Projects/Pessoal/data/kafka{numeroBroker}
      * listeners=PLAINTEXT://:909{numeroBroker} Lembrando que nesse caso precisamos fazer isso pois estamos rodando os brokers na mesma máquina, mas em produção, os ips serão diferentes.
    * Remover os arquivos de data, pois eles serão criados sozinhos.
  * A ideia aqui é subir os 4 kafkas, produzir as mensagens, verificar o consumo, verificar os detalhes dos tópicos e verificar o comportamento dos líderes e réplicas.
  * Quando derrubamos um broker, o Kafka irá rebalancear as partições, e o líder irá mudar de broker, fazendo com que não fiquemos sem o serviço. Antes do broker cair ele enviará as mensagens para outros líderes assumirem as informações do mesmo.
  * Dessa forma, não teremos mais um ponto de falha e sim diversos pontos de falha, o que é muito melhor.
  * Tudo é feito de forma automáticas, sem precisar de intervenção humana.
* 05 - Acks e reliability:
  * O que acontece quando uma mensagem chega no lider, mas antes de replicar para as replicas, o lider cair? Dessa forma, quando uma replica assumir o papel de liderança, a mensagem que foi enviada para o lider e não foi replicada, será perdida. O mesmo acontece quando as replicas caem.
  * Dentro da configuração do KafkaProducer, temos a propriedade ProducerConfig.ACKS_CONFIG que define o comportamento do produtor, esse que pode ter os seguintes comportamentos:
    * acks=0: O produtor não espera nenhuma confirmação do broker, ele apenas envia a mensagem e não espera nada. É o mais rápido, porém, é o menos seguro, pois ele não espera nenhuma confirmação
    * acks=1: O produtor espera a confirmação do broker líder, mas não espera a confirmação das replicas.
    * acks=all: O produtor espera a confirmação do broker líder e das replicas. Se apenas uma replica estiver up, ele irá esperar a confirmação da mesma. Agora, se tivermos alguma caida, ele irá esperar até que todas estejam up. É o mais seguro, porém, é o mais lento, pois ele espera a confirmação de todas as replicas.
  * ISR: In Sync Replica, é o conjunto de replicas que estão sincronizadas com o líder. Se uma replica cair, ela sai do ISR, e se ela voltar, ela entra no ISR novamente.
  * O reliability é a confiabilidade, e o acks=all é o mais confiável, porém, é o mais lento.


Atalhos:
* Iniciar o Zookeeper:
``` sh Zookeper
cd ../kafka_2.13-3.6.1
bin/zookeeper-server-start.sh config/zookeeper.properties
```

* Iniciar o Kafka1 :
``` sh Kafka
cd ../kafka_2.13-3.6.1
bin/kafka-server-start.sh config/server1.properties
```

* Iniciar o Kafka 2:
``` sh Kafka 2
cd ../kafka_2.13-3.6.1
bin/kafka-server-start.sh config/server2.properties
```

* Iniciar o Kafka 3:
``` sh Kafka 2
cd ../kafka_2.13-3.6.1
bin/kafka-server-start.sh config/server3.properties
```

* Iniciar o Kafka 4:
``` sh Kafka 2
cd ../kafka_2.13-3.6.1
bin/kafka-server-start.sh config/server4.properties
```