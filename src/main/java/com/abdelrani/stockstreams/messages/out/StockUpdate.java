package com.abdelrani.stockstreams.messages.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdate {
    private String symbol;
    private String companyName;
    private String timestamp;
    private Double price;
}
