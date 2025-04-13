package com.skincare_booking_system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skincare_booking_system.dto.request.AnswerSelectionRequest;
import com.skincare_booking_system.dto.request.ResultRequest;
import com.skincare_booking_system.dto.response.ResultResponse;
import com.skincare_booking_system.entities.Answer;
import com.skincare_booking_system.entities.Result;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.repository.AnswerRepository;
import com.skincare_booking_system.repository.ResultRepository;
import com.skincare_booking_system.repository.UserRepository;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<ResultResponse> getAllResults() {
        return resultRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<ResultResponse> getResultsByUserId(Long userId) {
        return resultRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ResultResponse getResultById(Long id) {
        Result result = resultRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));
        return convertToResponse(result);
    }

    public ResultResponse createResult(ResultRequest resultRequest) {
        User user = userRepository
                .findById(resultRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + resultRequest.getUserId()));

        Answer answer = answerRepository
                .findById(resultRequest.getAnswerId())
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + resultRequest.getAnswerId()));

        Result result = new Result();
        result.setUser(user);
        result.setAnswer(answer);
        result.setCreatedAt(LocalDateTime.now());

        return convertToResponse(resultRepository.save(result));
    }

    public ResultResponse updateResult(Long id, ResultRequest resultRequest) {
        Result result = resultRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));

        User user = userRepository
                .findById(resultRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + resultRequest.getUserId()));

        Answer answer = answerRepository
                .findById(resultRequest.getAnswerId())
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + resultRequest.getAnswerId()));

        result.setUser(user);
        result.setAnswer(answer);

        return convertToResponse(resultRepository.save(result));
    }

    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id)) {
            throw new RuntimeException("Result not found with id: " + id);
        }
        resultRepository.deleteById(id);
    }

    @Transactional
    public void saveAnswerSelections(AnswerSelectionRequest selectionRequest) {
        User user = userRepository
                .findById(selectionRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + selectionRequest.getUserId()));

        for (Long answerId : selectionRequest.getAnswerIds()) {
            Answer answer = answerRepository
                    .findById(answerId)
                    .orElseThrow(() -> new RuntimeException("Answer not found with id: " + answerId));

            Result result = new Result();
            result.setUser(user);
            result.setAnswer(answer);
            result.setCreatedAt(LocalDateTime.now());
            resultRepository.save(result);
        }
    }

    public List<ResultResponse> getUserSummary(Long userId) {
        return resultRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ResultResponse convertToResponse(Result result) {
        ResultResponse response = new ResultResponse();
        response.setId(result.getId());
        response.setUserId(result.getUser().getId());
        response.setAnswerText(result.getAnswer().getText());
        response.setQuestionText(result.getAnswer().getQuestion().getText());
        response.setServiceName(result.getAnswer().getService().getServiceName());
        response.setServiceDescription(result.getAnswer().getService().getDescription());
        response.setCreatedAt(result.getCreatedAt());
        return response;
    }
}
