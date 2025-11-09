package com.example.demo.model;

public class PredictResponse {
    private String output;

    public PredictResponse() {}

    public PredictResponse(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
