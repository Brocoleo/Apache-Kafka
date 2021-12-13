import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Application {
    private static final String VALID_TRANSACTIONS_TOPIC = "valid-transactions";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    public static void main(String[] args) {
        String consumerGroup = "account-manager";

        System.out.println("El consumidor es parte del grupo consumidores" + consumerGroup);

        Consumer<String, String> kafkaConsumer = createKafkaConsumer(BOOTSTRAP_SERVERS, consumerGroup);

        consumeMessages(VALID_TRANSACTIONS_TOPIC, kafkaConsumer);
    }

    public static void consumeMessages(String topic, Consumer<String, String> kafkaConsumer) {
    	Logger logger = LoggerFactory.getLogger(Consumer.class.getName()
    	kafkaConsumer.subscribe(Arrays.asList(topic));  
        try {
	        while (true) {
	            /**
	             * Complete el codigo aca en caso que sea necesario
	             * Verifique si hay nuevas transacciones a leer desde Kafka.
	             * Apruebe las transacciones entrantes
	             */
	        	 ConsumerRecords<String,String> records=kafkaConsumer.poll(Duration.ofMillis(100));  
	             for(ConsumerRecord<String,String> record: records){  
	                 logger.info("Value:" +record.value());    
	             }  
	        }
        } catch(Exeption e) {
        	System.out.println(e);
        } finally {
        	kafkaConsumer.close();
        }
    }

    public static Consumer<String, String> createKafkaConsumer(String bootstrapServers, String consumerGroup) {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new KafkaConsumer<>(properties);
    }

    private static void approveTransaction(Transaction transaction) {
        System.out.println(String.format("Transacción autorizada para el usuario %s, con la cantidad de $%.2f",
                transaction.getUser(), transaction.getAmount()));
    }

}
