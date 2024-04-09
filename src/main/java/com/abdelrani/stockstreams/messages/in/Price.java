package com.abdelrani.stockstreams.messages.in;

import lombok.Data;

@Data
public class Price {
    private String symbol;
    private String timestamp;
    private Double price;
}
