package com.abdelrani.stockstreams.configuration;

import java.util.Map;

public class PropertiesLoader {
    private static final Map<String, String> ENVS = System.getenv();

    public KafkaProperties priceKafkaProperties() {
        return KafkaProperties.builder()
                .bootstrapServers(ENVS.get("PRICE_KAFKA_BOOTSTRAP_SERVERS"))
                .topic(ENVS.get("PRICE_KAFKA_TOPIC"))
                .groupId(ENVS.get("PRICE_KAFKA_GROUP_ID"))
                .clientId(ENVS.get("PRICE_KAFKA_CLIENT_ID"))
                .autoOffsetReset(ENVS.get("PRICE_KAFKA_AUTO_OFFSET_RESET"))
                .build();
    }

    public KafkaProperties stockKafkaProperties() {
        return KafkaProperties.builder()
                .bootstrapServers(ENVS.get("STOCK_KAFKA_BOOTSTRAP_SERVERS"))
                .topic(ENVS.get("STOCK_KAFKA_TOPIC"))
                .groupId(ENVS.get("STOCK_KAFKA_GROUP_ID"))
                .clientId(ENVS.get("STOCK_KAFKA_CLIENT_ID"))
                .autoOffsetReset(ENVS.get("STOCK_KAFKA_AUTO_OFFSET_RESET"))
                .build();
    }

    public KafkaProperties stockUpdateKafkaProperties() {
        return KafkaProperties.builder()
                .bootstrapServers(ENVS.get("STOCK_UPDATE_KAFKA_BOOTSTRAP_SERVERS"))
                .topic(ENVS.get("STOCK_UPDATE_KAFKA_TOPIC"))
                .autoOffsetReset(ENVS.get("STOCK_UPDATE_KAFKA_AUTO_OFFSET_RESET"))
                .build();
    }
}
