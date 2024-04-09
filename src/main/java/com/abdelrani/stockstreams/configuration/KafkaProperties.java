package com.abdelrani.stockstreams.configuration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaProperties {
    private String bootstrapServers;
    private String topic;
    private String groupId;
    private String clientId;
    private String autoOffsetReset;
}
