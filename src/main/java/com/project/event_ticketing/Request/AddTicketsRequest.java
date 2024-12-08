package com.project.event_ticketing.Request;

import lombok.Data;

import java.util.List;

@Data
public class AddTicketsRequest {
    private String vendorId;
    private List<String> tickets;

}