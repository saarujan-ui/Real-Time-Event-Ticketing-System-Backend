package com.project.event_ticketing.controller;

import com.project.event_ticketing.Request.AddTicketsRequest;
import com.project.event_ticketing.Request.BuyTicketsRequest;
import com.project.event_ticketing.Request.CustomerRequest;
import com.project.event_ticketing.Request.VendorRequest;
import com.project.event_ticketing.models.TicketConfig;
import com.project.event_ticketing.models.TicketPoolStatus;
import com.project.event_ticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticketing")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;


    @PostMapping("/start")
    public ResponseEntity<String> startSystem(@RequestBody TicketConfig config) {
        ticketService.startSystem(config);
        return ResponseEntity.ok("Ticketing system started successfully.");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        ticketService.stopSystem();
        return ResponseEntity.ok("Ticketing system stopped successfully.");
    }

    @PostMapping("/vendors")
    public ResponseEntity<String> addVendor(@RequestBody VendorRequest vendorRequest) {
        ticketService.addVendor(vendorRequest.getVendorId());
        return ResponseEntity.ok("Vendor added successfully.");
    }

    @PostMapping("/customers")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerRequest customerRequest) {
        ticketService.addCustomer(customerRequest.getCustomerId());
        return ResponseEntity.ok("Customer added successfully.");
    }

    @GetMapping("/status")
    public ResponseEntity<TicketPoolStatus> getTicketPoolStatus() {
        return ResponseEntity.ok(ticketService.getTicketPoolStatus());
    }

    @PostMapping("/vendors/add-tickets")
    public ResponseEntity<String> addTickets(@RequestBody AddTicketsRequest request) {
        int addedCount = ticketService.addTickets(request.getVendorId(), request.getTickets());
        if (addedCount < request.getTickets().size()) {
            return ResponseEntity.ok("Added only " + addedCount + " tickets due to threshold limit.");
        }
        return ResponseEntity.ok("All tickets added successfully.");
    }

    @PostMapping("/customers/buy-tickets")
    public ResponseEntity<String> buyTickets(@RequestBody BuyTicketsRequest request) {
        List<String> boughtTickets = ticketService.buyTickets(request.getCustomerId(), request.getCount());
        if (boughtTickets.size() < request.getCount()) {
            return ResponseEntity.ok("Bought only " + boughtTickets.size() + " tickets due to limited availability.");
        }
        return ResponseEntity.ok("All requested tickets bought successfully.");
    }


}

