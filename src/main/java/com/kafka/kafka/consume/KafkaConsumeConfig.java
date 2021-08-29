package com.kafka.kafka.consume;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumeConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    /**
     * 보통 earliest, lastest 둘중하나를 가장 많이 사용한다는데 정지 되었다가
     * 다시 시작한다는 가정하에 lastest설정. none도 있다는데 거의 쓰지 않는다고 한다.
     */
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offsetReset;
    /**
     * 1차적으로 1000 개씩 일거오게 햇는데 간격 샛팅을 길개주고 테스트 해봐야할듯.
     * 간격셋팅 정보 찾기...
     */
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;
    /**
     * auto commit은 true, false 테스트를 해봐야하니까 일단 설명은 없고
     * 안 중요하면 auto해도 될듯하고.
     * 중요한 데이터면 Callback등을 통해서 확인하는 과정이나 백업과정이 필요할것으로 생각됨
     */
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;

    /* serializer setting */

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String keyDeSerializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeSerializer;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return getStringStringConsumerFactory();
    }

    /**
     * 프로퍼티를 설정해서 KafkaConsumerFactory 생성
     * @return
     */
    private ConsumerFactory<String, String> getStringStringConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeSerializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeSerializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
