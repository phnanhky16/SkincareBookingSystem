package com.skincare_booking_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.AnswerSelectionRequest;
import com.skincare_booking_system.dto.request.ResultRequest;
import com.skincare_booking_system.dto.response.ResultResponse;
import com.skincare_booking_system.service.ResultService;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<ResultResponse>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResults());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResultResponse>> getResultsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(resultService.getResultsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createResult(@RequestBody ResultRequest resultRequest) {
        return ResponseEntity.ok(resultService.createResult(resultRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateResult(
            @PathVariable Long id, @RequestBody ResultRequest resultRequest) {
        return ResponseEntity.ok(resultService.updateResult(id, resultRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-selections")
    public ResponseEntity<Void> saveAnswerSelections(@RequestBody AnswerSelectionRequest selectionRequest) {
        try {
            resultService.saveAnswerSelections(selectionRequest);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<List<ResultResponse>> getUserSummary(@PathVariable Long userId) {
        try {
            List<ResultResponse> summary = resultService.getUserSummary(userId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
