package com.project.event_ticketing.service;

import com.project.event_ticketing.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@Slf4j
public class TicketService {

    private TicketPool ticketPool;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<Vendor> vendors = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private TicketConfig config;
    private boolean systemRunning = false;


    public void startSystem(TicketConfig config) {
        if (systemRunning) {
            throw new IllegalStateException("System is already running.");
        }

        this.config = config;
        ticketPool = new TicketPool(config.getMaxTicketCapacity());
        systemRunning = true;

        // Start vendors
        for (int i = 0; i < config.getVendorCount(); i++) {
            Vendor vendor = new Vendor("Vendor-" + i, ticketPool, config.getTicketReleaseRate());
            vendors.add(vendor);
            executor.submit(vendor);
        }

        for (int i = 0; i < config.getCustomerCount(); i++) {
            Customer customer = new Customer("Customer-" + i, ticketPool, config.getCustomerRetrievalRate());
            customers.add(customer);
            executor.submit(customer);
        }
    }

    public void stopSystem() {
        systemRunning = false;
        executor.shutdownNow();
        vendors.clear();
        customers.clear();
    }


    public void addVendor(String vendorId) {
        if (!systemRunning) {
            throw new IllegalStateException("System is not running.");
        }

        if (vendors.size() >= config.getVendorCount()) {
            throw new IllegalStateException("Vendor limit reached. Cannot add more vendors.");
        }

        Vendor vendor = new Vendor(vendorId, ticketPool, config.getTicketReleaseRate());
        vendors.add(vendor);
        executor.submit(vendor);
    }


    public void addCustomer(String customerId) {
        if (!systemRunning) {
            throw new IllegalStateException("System is not running.");
        }

        if (customers.size() >= config.getCustomerCount()) {
            throw new IllegalStateException("Customer limit reached. Cannot add more customers.");
        }

        Customer customer = new Customer(customerId, ticketPool, config.getCustomerRetrievalRate());
        customers.add(customer);
        executor.submit(customer);
    }


    public TicketPoolStatus getTicketPoolStatus() {
        return ticketPool.getStatus();
    }


    public int addTickets(String vendorId, List<String> tickets) {
         vendors.stream()
                .filter(v -> v.getVendorId().equals(vendorId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        int addedCount = 0;
        for (String ticket : tickets) {
            if (ticketPool.size() >= config.getMaxTicketCapacity()) {
                log.info("Maximum ticket capacity reached. Vendor: " + vendorId);
                break;
            }
            try {
                ticketPool.addTicket(ticket);
                addedCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error adding ticket", e);
            }
        }
        return addedCount;
    }


    public List<String> buyTickets(String customerId, int count) {
       customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<String> boughtTickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                String ticket = ticketPool.removeTicket();
                boughtTickets.add(ticket);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return boughtTickets;
    }
}


