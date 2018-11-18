package com.greatgaming;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessagingHandler extends DataHandler{
    private List<MessagingHandler> buddies = new ArrayList<MessagingHandler>();
    private Queue<String> messageQueue = new LinkedList<String>();
    public static final String CUSTOM_SEPARATOR = "ROTAPAPES";
    public static final String HEARTBEAT_STRING = "HEARTBEAT";

    public String handleData(String data) {
        System.out.println("received: " + data);

        if (!data.equals(HEARTBEAT_STRING)) {
            for (MessagingHandler handler : this.buddies) {
                handler.addMessage(data);
            }
        }
        this.addMessage(data);
        StringBuilder builder = new StringBuilder();
        while(messageQueue.peek() != null) {
            builder.append(messageQueue.poll());
            builder.append(CUSTOM_SEPARATOR);
        }
        return builder.toString();
    }

    public void addMessage(String message) {
        messageQueue.add(message);
    }

    public void addParticipant(MessagingHandler handler) {
        buddies.add(handler);
    }
}
