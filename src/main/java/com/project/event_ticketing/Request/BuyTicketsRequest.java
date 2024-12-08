package com.project.event_ticketing.Request;

import lombok.Data;

@Data
public class BuyTicketsRequest {
    private String customerId;
    private int count;


}
