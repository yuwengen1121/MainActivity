package com.xy.tickets.db;


/**
 * The type of ticket.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum TicketType {

	GROUPBUY(0, "团购"),
	MOVIE(1, "电影"),
	FLIGHT(2, "飞机"),
    HOTEL(3, "酒店"),
    TRAIN(4, "火车");

	private TicketType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private int value = 0;
    private String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public int value() {
        return this.value;
    }
}
