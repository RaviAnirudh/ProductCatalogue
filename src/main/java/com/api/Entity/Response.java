package com.api.Entity;

import java.util.List;

public class Response {
    private String responseMessage;
    private int responseCode;
    private String requestId;
    private List<Product> payloads;
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public List<Product> getPayloads() {
        return payloads;
    }
    public Response setPayloads(List<Product> payload) {
        this.payloads = payload;
        return this;
    }
}
