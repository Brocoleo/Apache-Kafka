
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Application {
    private static final String SUSPICIOUS_TRANSACTIONS_TOPIC = "suspicious-transactions";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    public static void main(String[] args) {
        String consumerGroup = "user-notification-service";

        System.out.println("Consumidores es parte del grupo " + consumerGroup);

        Consumer<String, Transaction> kafkaConsumer = createKafkaConsumer(BOOTSTRAP_SERVERS, consumerGroup);

        consumeMessages(SUSPICIOUS_TRANSACTIONS_TOPIC, kafkaConsumer);
    }

    public static void consumeMessages(String topic, Consumer<String, Transaction> kafkaConsumer) {
        /**
         * Complete el código aquí para suscribirse al tema(topic) correcto.
         * Ejecute un loop y lea las transacciones entrantes
         * Para cada nueva transacción, envíe una notificación al usuario
         */
    }

    public static Consumer<String, Transaction> createKafkaConsumer(String bootstrapServers, String consumerGroup) {
        Properties properties = new Properties();

        /**
         * Complete el código aquí para configurar el resto de los parámetros del cliente de Kafka
         */
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        return new KafkaConsumer<>(properties);
    }

    private static void sendUserNotification(Transaction transaction) {
        System.out.println(
                String.format("Enviando al usuario %s una notificacion acerca de una transaccion sospechosa de $%.2f en su cuenta " +
                                "procedente de %s",
                        transaction.getUser(),
                        transaction.getAmount(),
                        transaction.getTransactionLocation()));
    }
}
