package com.kafka.kafka.produce;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Slf4j
@Component
public class KafkaProduce {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProduce(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String messageKey, String message) {

        Message<String> messageBuilder = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.MESSAGE_KEY, messageKey)
                .build();

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(messageBuilder);


        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(
                        "Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset()
                                + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    /**
     * 해당의 경우 사비동기 호출에서 사실상 try {} get()에서 blocking 이 발생히기때문에 정말 느려도되거나
     * 신뢰성기반으로 데이터가 많이 없는경우라면 모를까 비추천하지만.. 왠만하면 사용하지 않는것이...
     * @param topicName
     * @param messageKey
     * @param message
     */
    public void sendMessageSyncException(String topicName, String messageKey, String message) {
        Message<String> messageBuilder = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.MESSAGE_KEY, messageKey)
                .build();

        ListenableFuture<SendResult<String, String>> f = kafkaTemplate.send(messageBuilder);

        try {
            SendResult<String, String> meta = f.get();
        }
        catch (Exception ex){

        }
    }

    /**
     * 기본예제와 같은 방법이며 CallBack 메소드를 Add하는 방법으로 해당 방법을 blocking을 발생시키지 않습니다.
     * @param topicName
     * @param messageKey
     * @param message
     */
    public void sendMessageASyncException(String topicName, String messageKey, String message) {

        Message<String> messageBuilder = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.MESSAGE_KEY, messageKey)
                .build();

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(messageBuilder);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(
                        "Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset()
                                + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}