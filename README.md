# formacao-kafka-alura
Formação Alura: Mensageria com Apache Kafka
* Link: https://cursos.alura.com.br/formacao-kafka

### 1. Streams, Cluster e Microsserviços:
* #### O que é Kafka? #HipsterPontoTube - Youtube:
  * https://www.youtube.com/watch?v=G6Tcy7hNdA8
  * Sistema normalmente chamado de Pub/Sub (Publisher/Subscriber), onde um temos pessoas produzindo eventos e outras consumindo os mesmos.
  * Dessa forma não temos um acoplamento entre projetos, além de isolar e deixar o processo assíncrono.
  * Podemos fazer com que todos os consumidores inscritos em determinado tópico recebam a mesma mensagem enviada ou também podemos separar por partições, utilizando um algoritmo de hash em cima de uma chave que disponibilizamos, dessa forma, tudo daquele usuário/sistema específico cai na mesma partição para o mesmo consumir.
  * Mas também podemos deixar separado em tópicos separados.
  * Podemos configurar o Kafka para quando disparamos uma mensagem termos a certeza que o broker a recebeu/armazenou numa partição. Tbm podemos querer saber se essas mensagens foram replicadas em outras partições.
