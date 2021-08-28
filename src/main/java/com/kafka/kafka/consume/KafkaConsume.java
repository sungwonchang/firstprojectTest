package com.kafka.kafka.consume;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsume {

    @KafkaListener(topics = "${sample.topic.news}"
            , containerFactory = "kafkaListenerContainerFactory"
            , groupId = "${sample.group.news}")
    public void listenNewsTopic(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) throws Exception {
        log.info("========> News MessageKey: {}", messageKey);
        log.info("========> News Message: {}", message);
    }


    @KafkaListener(topics = "${sample.topic.music}"
            , containerFactory = "kafkaListenerContainerFactory"
            , groupId = "${sample.group.music}")
    public void listenMusicTopic(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) throws Exception {
        log.info("========> Music MessageKey: {}", messageKey);
        log.info("========> Music Message: {}", message);
    }
}
