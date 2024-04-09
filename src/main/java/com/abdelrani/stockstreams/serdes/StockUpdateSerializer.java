package com.abdelrani.stockstreams.serdes;

import com.abdelrani.stockstreams.messages.out.StockUpdate;
import lombok.SneakyThrows;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

import static com.abdelrani.stockstreams.configuration.JacksonConfiguration.MAPPER;

public class StockUpdateSerializer implements KafkaRecordSerializationSchema<StockUpdate> {

    private final String topic;

    public StockUpdateSerializer(String topic) {
        this.topic = topic;
    }

    @SneakyThrows
    @Nullable
    @Override
    public ProducerRecord<byte[], byte[]> serialize(StockUpdate element, KafkaSinkContext context, Long timestamp) {
        byte[] key = element.getSymbol().getBytes();
        byte[] result = MAPPER.writeValueAsBytes(element);
        return new ProducerRecord<>(topic, key, result);
    }
}
