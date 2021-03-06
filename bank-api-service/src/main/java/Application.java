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
            Transaction transaction = incomingTransactionsReader.next();    
            /*
             * Se envía el topico, y el objeto transaction en cuestión, donde 
             * ProduceRecord se encargará de serializar el objeto con la propieti definida: serialize 
             *
             */
            if(transaction.getTransactionLocation().equals(userResidenceDatabase.getUserResidence(transaction.getUser()))){
                //topic valid-transactions
                kafkaProducer.send(new ProducerRecord<String, Transaction>("valid-transactions", transaction) );
            }
            else{
                //topic suspicious-transactions
            	kafkaProducer.send(new ProducerRecord<String, Transaction>("suspicious-transactions", transaction) );
            }
        }
    }

    public static Producer<String, Transaction> createKafkaProducer(String bootstrapServers) {
    	/*
    	 El Value_Serial... se pone el nombre de la clase que serializará
    	 El key, es el por default 
    	 
    	 */
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "banking-api-service");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  Transaction.TransactionSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

}
