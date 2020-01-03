package com.example.booker;

public class Request {
    String bookTitle,requesterName,requestId,requesterId,bookId,timeStamp,coverLink;
    boolean status;

    public Request() {
    }

    public Request(String bookTitle, String requesterName, String requestId, String requesterId, String bookId, String timeStamp,String coverLink, boolean status) {
        this.bookTitle = bookTitle;
        this.requesterName = requesterName;
        this.requestId = requestId;
        this.requesterId = requesterId;
        this.bookId = bookId;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
