package com.abdelrani.stockstreams.sources;

import com.abdelrani.stockstreams.configuration.KafkaProperties;
import com.abdelrani.stockstreams.messages.in.Price;
import com.abdelrani.stockstreams.messages.in.Stock;
import com.abdelrani.stockstreams.messages.out.StockUpdate;
import com.abdelrani.stockstreams.serdes.PriceDeserializer;
import com.abdelrani.stockstreams.serdes.StockDeserializer;
import com.abdelrani.stockstreams.serdes.StockUpdateSerializer;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

public class SourceBuilder {

    public static  KafkaSource<Price> priceSource(KafkaProperties kafkaProperties) {
        return KafkaSource.<Price>builder()
                .setBootstrapServers(kafkaProperties.getBootstrapServers())
                .setTopics(kafkaProperties.getTopic())
                .setValueOnlyDeserializer(new PriceDeserializer())
                .setGroupId(kafkaProperties.getGroupId())
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .build();
    }

    public static  KafkaSource<Stock> stockSource(KafkaProperties kafkaProperties) {
        return KafkaSource.<Stock>builder()
                .setBootstrapServers(kafkaProperties.getBootstrapServers())
                .setTopics(kafkaProperties.getTopic())
                .setDeserializer(new StockDeserializer())
                .setGroupId(kafkaProperties.getGroupId())
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setProperty("partition.discovery.interval.ms", "10000") // discover new partitions per 10 seconds
                .build();
    }

    public static KafkaSink<StockUpdate> stockUpdateSink(KafkaProperties kafkaProperties) {
        return KafkaSink.<StockUpdate>builder()
                .setBootstrapServers(kafkaProperties.getBootstrapServers())
                .setRecordSerializer(new StockUpdateSerializer(kafkaProperties.getTopic()))
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE) // at least once delivery
                .build();
    }
}