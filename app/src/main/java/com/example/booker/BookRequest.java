package com.example.booker;

public class BookRequest {
    private String requestId,requesterId,bookId;
    private long timeStamp;
    boolean status;

    public BookRequest() {
    }

    public BookRequest(String requestId, String requesterId, String bookId, long timeStamp, boolean status) {
        this.requestId = requestId;
        this.requesterId = requesterId;
        this.bookId = bookId;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
