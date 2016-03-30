package me.ashif.postman;

/**
 * Created by almukthar on 27/3/16.
 */
public class IncomingData {

    private String incomingData;
    private String senderID;
    private String channel;
    private String status;

    public IncomingData() {
    }
    public String getIncomingData() {
        return incomingData;
    }

    public void setIncomingData(String incomingData) {
        this.incomingData = incomingData;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
