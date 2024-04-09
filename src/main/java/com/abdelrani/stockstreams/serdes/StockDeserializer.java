package com.abdelrani.stockstreams.serdes;

import com.abdelrani.stockstreams.messages.in.Stock;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

import static com.abdelrani.stockstreams.configuration.JacksonConfiguration.MAPPER;

@Slf4j
public class StockDeserializer implements KafkaRecordDeserializationSchema<Stock> {

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Stock> out) throws IOException {
        Stock message = MAPPER.readValue(record.value(), Stock.class);
        log.info("Received stock with symbol: {}", message.getSymbol());
        out.collect(message);
    }

    @Override
    public TypeInformation<Stock> getProducedType() {
        return TypeInformation.of(Stock.class);
    }
}
