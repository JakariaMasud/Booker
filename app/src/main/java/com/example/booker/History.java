package com.example.booker;

public class History {
    String historyMsg,historyType;

    public History() {
    }

    public History(String historyMsg, String historyType) {
        this.historyMsg = historyMsg;
        this.historyType = historyType;
    }

    public String getHistoryMsg() {
        return historyMsg;
    }

    public void setHistoryMsg(String historyMsg) {
        this.historyMsg = historyMsg;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }
}
