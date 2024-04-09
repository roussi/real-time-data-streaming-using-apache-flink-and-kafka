package com.abdelrani.stockstreams.operators;


import com.abdelrani.stockstreams.messages.in.Price;
import com.abdelrani.stockstreams.messages.in.Stock;
import com.abdelrani.stockstreams.messages.out.StockUpdate;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class StockPriceJoiner extends RichCoFlatMapFunction<Stock, Price, StockUpdate> {

    /**
     * The state that is maintained by this process function
     */
    private ValueState<Price> priceState;

    @Override
    public void open(Configuration parameters) throws Exception {
        priceState = getRuntimeContext().getState(new ValueStateDescriptor<>("price", Price.class));
    }

    @Override
    public void flatMap1(Stock stock, Collector<StockUpdate> out) throws Exception {
        Price price = priceState.value();
        if (price != null) {
            log.info("Joining stock: {} with price: {}", stock.getSymbol(), price.getPrice());
            out.collect(StockUpdate.builder()
                    .symbol(stock.getSymbol())
                    .price(price.getPrice())
                    .companyName(stock.getCompanyName())
                    .timestamp(price.getTimestamp())
                    .build());
        }
    }

    @Override
    public void flatMap2(Price value, Collector<StockUpdate> out) throws Exception {
        priceState.update(value);
    }
}
