package com.project.event_ticketing.models;

import java.util.UUID;

public class Vendor implements Runnable {
    private final String vendorId;
    private final TicketPool ticketPool;
    private final int releaseRate;

    public Vendor(String vendorId, TicketPool ticketPool, int releaseRate) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = "Ticket-" + UUID.randomUUID();
                ticketPool.addTicket(ticket);
                Thread.sleep(1000 / releaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getVendorId() {
        return vendorId;
    }
}

