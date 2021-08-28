package com.kafka.kafka.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigration {
//    @Bean
//    public KafkaAdmin admin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        return new KafkaAdmin(configs);
//    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("thing1")
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("thing2")
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name("thing3")
                .assignReplicas(0, Arrays.asList(0, 1))
                .assignReplicas(1, Arrays.asList(1, 2))
                .assignReplicas(2, Arrays.asList(2, 0))
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    /**버전 2.6부터 .partitions() 및/또는 replicas()를 생략할 수 있으며 브로커 기본값이 해당 속성에 적용됩니다.
     * 이 기능을 지원하려면 브로커 버전이 2.4.0 이상이어야 합니다(KIP-464 참조).*/

    @Bean
    public NewTopic topic4() {
        return TopicBuilder.name("defaultBoth")
                .build();
    }

    @Bean
    public NewTopic topic5() {
        return TopicBuilder.name("defaultPart")
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic6() {
        return TopicBuilder.name("defaultRepl")
                .partitions(3)
                .build();
    }

    /**
     * 버전 2.7부터 단일 KafkaAdmin.NewTopics 빈 정의에서 여러 NewTopic 을 선언할 수 있습니다
     */
    @Bean
    public KafkaAdmin.NewTopics topics456() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("defaultBoth_new")
                        .build(),
                TopicBuilder.name("defaultPart_new")
                        .replicas(1)
                        .build(),
                TopicBuilder.name("defaultRepl_new")
                        .partitions(3)
                        .build());
    }

}
