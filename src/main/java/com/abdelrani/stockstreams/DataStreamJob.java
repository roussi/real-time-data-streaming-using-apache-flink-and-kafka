/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abdelrani.stockstreams;

import com.abdelrani.stockstreams.configuration.PropertiesLoader;
import com.abdelrani.stockstreams.messages.in.Price;
import com.abdelrani.stockstreams.messages.in.Stock;
import com.abdelrani.stockstreams.messages.out.StockUpdate;
import com.abdelrani.stockstreams.operators.StockPriceJoiner;
import com.abdelrani.stockstreams.sources.SourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Set;

@Slf4j
public class DataStreamJob {

    private static final Set<String> ALLOWED_STOCKS = Set.of("AAPL", "GOOG", "AMZN", "MSFT", "TSLA");

    public static void main(String[] args) throws Exception {
        // Sets up the execution environment, which is the main entry point to building Flink applications.
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configure the environment
        configureEnvironment(env);

        // loading properties
        PropertiesLoader propertiesLoader = new PropertiesLoader();

        // Creating the execution plan
        DataStreamSource<Stock> stockDataStream = env.fromSource(SourceBuilder.stockSource(propertiesLoader.stockKafkaProperties()),
                WatermarkStrategy.noWatermarks(),
                "stock-source");

        DataStreamSource<Price> priceDataStream = env.fromSource(SourceBuilder.priceSource(propertiesLoader.priceKafkaProperties()),
                WatermarkStrategy.noWatermarks(),
                "price-source");

        // send the result to a kafka topic using KafkaSink
        KafkaSink<StockUpdate> stockUpdateKafkaSink = SourceBuilder.stockUpdateSink(propertiesLoader.stockUpdateKafkaProperties());

        stockDataStream
                .filter(stock -> ALLOWED_STOCKS.contains(stock.getSymbol()))
                .connect(priceDataStream)
                .keyBy(Stock::getSymbol, Price::getSymbol)
                .flatMap(new StockPriceJoiner())
                .name("join-stock-price")
                .map(stockUpdate -> {
                    log.info("stock price update: {}", stockUpdate);
                    return stockUpdate;
                })
                .sinkTo(stockUpdateKafkaSink);


        // Execute program, beginning computation.
        env.execute("StockPriceStreamJob");

    }

    private static void configureEnvironment(StreamExecutionEnvironment env) {
        // automatically recover from failures
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                3, // number of restart attempts
                Time.seconds(10) // delay
        ));
        // disable kryo serialization and use the PojoSerializer (for better efficiency)
        env.getConfig().disableGenericTypes();
        // configuring the task manager
        env.configure(getConfiguration());
    }

    // Configure the task manager
    private static Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.setString("taskmanager.cpu.cores", "4");
        config.setString("taskmanager.memory.task.heap.size", "1024m");
        return config;
    }
}
