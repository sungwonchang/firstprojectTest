package com.kafka.kafka.controller;

import com.kafka.kafka.produce.KafkaProduce;
import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("sample")
public class SampleController {

    @Value("${sample.topic.news}")
    private String topicNews;

    @Value("${sample.topic.music}")
    private String topicMusic;

    @Value("${sample.message-key.news}")
    private String messageKeyNews;

    @Value("${sample.message-key.music}")
    private String messageKeyMusic;

    final String success = "published success";

    private final com.kafka.kafka.produce.KafkaProduce KafkaProduce;


    @Autowired
    public SampleController(com.kafka.kafka.produce.KafkaProduce kafkaProduce) {
        KafkaProduce = kafkaProduce;
    }


    @GetMapping("/send/news/{msg}")
    String sendNews(@PathVariable String msg) {
        KafkaProduce.sendMessage(topicNews, messageKeyNews, msg);
        return success;
    }

    @GetMapping("/send/music/{msg}")
    String sendMusic(@PathVariable String msg) {
        KafkaProduce.sendMessage(topicMusic, messageKeyMusic, msg);
        return success;
    }
}
