package com.project.event_ticketing.models;

public class Customer implements Runnable {
    private final String customerId;
    private final TicketPool ticketPool;
    private final int retrievalRate;

    public Customer(String customerId, TicketPool ticketPool, int retrievalRate) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = ticketPool.removeTicket();
                System.out.println(customerId + " purchased " + ticket);
                Thread.sleep(1000 / retrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getCustomerId() {
        return customerId;
    }
}

