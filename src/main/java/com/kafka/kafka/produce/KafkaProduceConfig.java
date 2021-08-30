package com.kafka.kafka.produce;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProduceConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keyDeSerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueDeSerializer;

    @Value("${spring.kafka.producer.linger.ms}")
    private String lingerMs;

    @Value("${spring.kafka.producer.enable.idempotence}")
    private String enableIdempotence;

//    @Value("${spring.kafka.producer.max.in.flight.requests.per.connection}")
//    private String maxInFlightRequestsPerConnection;


    /**
        프로듀서 설정 Factory 종류별로 변경하고
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keyDeSerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueDeSerializer);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);
        //configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * 기본 카프카 템플릿은 해당 Factory를 주입받아서 기본 Properties를 설정한다.
     * @return
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 프로듀서보다 Kafkatemplate이  Warping해서  더 편리한 기능을 제공하니까
     * 프로듀서를 사용해도 되지만 Template이 더 좋은 방법이라고 안내하고 있음
     * https://stackoverflow.com/questions/63808139/what-is-the-difference-between-kafka-template-and-kafka-producer
     * 일단 프로듀서도 생성하는 예제는 만들기만 함.
     * @return
     */
    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keyDeSerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueDeSerializer);
        return new KafkaProducer<>(configProps);

    }
}
