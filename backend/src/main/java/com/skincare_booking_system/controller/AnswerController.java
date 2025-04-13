package com.skincare_booking_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.AnswerRequest;
import com.skincare_booking_system.dto.response.AnswerResponse;
import com.skincare_booking_system.service.AnswerService;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @PostMapping
    public ResponseEntity<AnswerResponse> createAnswer(@RequestBody AnswerRequest answerRequest) {
        return ResponseEntity.ok(answerService.createAnswer(answerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(
            @PathVariable Long id, @RequestBody AnswerRequest answerRequest) {
        return ResponseEntity.ok(answerService.updateAnswer(id, answerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(answerService.getAnswersByServiceId(serviceId));
    }
}
