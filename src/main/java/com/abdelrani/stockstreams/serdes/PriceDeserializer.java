package com.abdelrani.stockstreams.serdes;

import com.abdelrani.stockstreams.messages.in.Price;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

import static com.abdelrani.stockstreams.configuration.JacksonConfiguration.MAPPER;

@Slf4j
public class PriceDeserializer extends AbstractDeserializationSchema<Price>  {

    @Override
    public Price deserialize(byte[] message) throws IOException {
        Price priceMessage = MAPPER.readValue(message, Price.class);
        log.info("Received price message for stock: {}", priceMessage.getSymbol());
        return priceMessage;
    }

}
