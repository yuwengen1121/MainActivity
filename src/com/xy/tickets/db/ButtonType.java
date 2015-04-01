package com.xy.tickets.db;


/**
 * The type of ticket.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum ButtonType {

	MAP(0, "查看地图"),
	PHONEB(1, "联系商家"),
	PHONEH(2, "联系酒店"),
	FLIGHTSTATUS(3, "航班动态"),
    TAXI(4, "滴滴打车");

	private ButtonType(int value, String displayName) {
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
