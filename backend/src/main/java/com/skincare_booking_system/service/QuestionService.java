package com.skincare_booking_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.QuestionRequest;
import com.skincare_booking_system.dto.response.QuestionResponse;
import com.skincare_booking_system.entities.Question;
import com.skincare_booking_system.repository.QuestionRepository;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return convertToResponse(question);
    }

    public QuestionResponse createQuestion(QuestionRequest questionRequest) {
        Question question = new Question();
        question.setText(questionRequest.getText());
        return convertToResponse(questionRepository.save(question));
    }

    public QuestionResponse updateQuestion(Long id, QuestionRequest questionRequest) {
        Question question = questionRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        question.setText(questionRequest.getText());
        return convertToResponse(questionRepository.save(question));
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    private QuestionResponse convertToResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setText(question.getText());
        return response;
    }
}
