package com.project.event_ticketing.models;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int capacity;

    public TicketPool(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addTicket(String ticket) throws InterruptedException {
        while (tickets.size() >= capacity) {
            wait();
        }
        tickets.add(ticket);
        notifyAll();
    }

    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait();
        }
        String ticket = tickets.poll();
        notifyAll();
        return ticket;
    }

    public TicketPoolStatus getStatus() {
        return new TicketPoolStatus(tickets.size());
    }
    public int size(){
        return tickets.size();
    }
}
