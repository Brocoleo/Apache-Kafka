import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Servicio de API Bancaria
 */
public class Application {
    private static final String SUSPICIOUS_TRANSACTIONS_TOPIC = "suspicious-transactions";
    private static final String VALID_TRANSACTIONS_TOPIC = "valid-transactions";

    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    public static void main(String[] args) {
        Producer<String, Transaction> kafkaProducer = createKafkaProducer(BOOTSTRAP_SERVERS);

        try {
            processTransactions(new IncomingTransactionsReader(), new UserResidenceDatabase(), kafkaProducer);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.flush();
            kafkaProducer.close();
        }
    }

    public static void processTransactions(IncomingTransactionsReader incomingTransactionsReader,
                                           UserResidenceDatabase userResidenceDatabase,
                                           Producer<String, Transaction> kafkaProducer) throws ExecutionException, InterruptedException {

        while (incomingTransactionsReader.hasNext()) {
            /**
             * Complete el código en caso que sea necesario.
             * Envie la transacción al tema(topic) correcto, según el origen de la transaccion y los datos de residencia del usuario
             */
        	System.out.println("Hola hola mundo");

            Transaction transaction = incomingTransactionsReader.next();
            ProducerRecord<String, String> record = new ProducerRecord<>("valid-transactions", "hola hola mundito");
            kafkaProducer.send(record);
            /*
            if(transaction.getUser().equals(userResidenceDatabase.getUserResidence())){
                //topic valid-transactions
                ProducerRecord<String, String> record = new ProducerRecord<>("valid-transactions", transaction.toString());
                kafkaProducer.send(record);

            }
            else{
                //topic suspicious-transactions
                ProducerRecord<String, String> record = new ProducerRecord<>("suspicious-transactions", transaction.toString());
                kafkaProducer.send(record);
            }
            */
            
        }
    }

    public static Producer<String, Transaction> createKafkaProducer(String bootstrapServers) {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "banking-api-service");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(properties);
    }

}
