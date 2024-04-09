package com.abdelrani.stockstreams.messages.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Stock {
    private String symbol;
    @JsonProperty("company_name")
    private String companyName;
}
