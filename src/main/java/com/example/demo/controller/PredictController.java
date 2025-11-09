package com.example.demo.controller;

import com.example.demo.model.InsuranceRequest;
import com.example.demo.model.InsuranceResponse;
import com.example.demo.service.GeminiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PredictController {

    private final GeminiService geminiService;

    public PredictController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping(value = "/predict", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public InsuranceResponse predict(@RequestBody InsuranceRequest req) {
        // Build a clear prompt for the AI model
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an expert medical insurance analyst. Given the following person's data, estimate an annual medical insurance cost (USD) and provide 3 concise suggestions to reduce costs. Respond in plain text with an estimate and suggestions. Data:\n");
        prompt.append("Name: ").append(req.getName()).append("\n");
        prompt.append("Age: ").append(req.getAge()).append("\n");
        prompt.append("Gender: ").append(req.getGender()).append("\n");
        prompt.append("Smoker: ").append(req.getSmoker()).append("\n");
        prompt.append("Location: ").append(req.getLocation()).append("\n");
        prompt.append("Kids: ").append(req.getKids()).append("\n");
        prompt.append("BMI: ").append(req.getBmi()).append("\n");
        prompt.append("Existing conditions: ").append(req.getExistingCondition()).append("\n");
        prompt.append("Provide a short, friendly estimate and 3 actionable suggestions.");

        String aiOutput = geminiService.generateText(prompt.toString());
        return new InsuranceResponse(aiOutput);
    }
}
