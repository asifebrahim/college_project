package com.example.demo.model;

public class PredictRequest {
    private String input;

    public PredictRequest() {}

    public PredictRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
