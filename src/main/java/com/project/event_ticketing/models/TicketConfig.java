package com.project.event_ticketing.models;

import lombok.Data;

@Data
public class TicketConfig {
    private int maxTicketCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int vendorCount;
    private int customerCount;


}
