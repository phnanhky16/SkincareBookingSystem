package com.skincare_booking_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.AnswerRequest;
import com.skincare_booking_system.dto.response.AnswerResponse;
import com.skincare_booking_system.entities.Answer;
import com.skincare_booking_system.entities.Question;
import com.skincare_booking_system.entities.Services;
import com.skincare_booking_system.repository.AnswerRepository;
import com.skincare_booking_system.repository.QuestionRepository;
import com.skincare_booking_system.repository.ServicesRepository;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    public List<AnswerResponse> getAllAnswers() {
        return answerRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public AnswerResponse getAnswerById(Long id) {
        Answer answer = answerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + id));
        return convertToResponse(answer);
    }

    public AnswerResponse createAnswer(AnswerRequest answerRequest) {
        Question question = questionRepository
                .findById(answerRequest.getQuestionId())
                .orElseThrow(
                        () -> new RuntimeException("Question not found with id: " + answerRequest.getQuestionId()));

        Services service = servicesRepository
                .findById(answerRequest.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + answerRequest.getServiceId()));

        Answer answer = new Answer();
        answer.setText(answerRequest.getText());
        answer.setQuestion(question);
        answer.setService(service);

        return convertToResponse(answerRepository.save(answer));
    }

    public AnswerResponse updateAnswer(Long id, AnswerRequest answerRequest) {
        Answer answer = answerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + id));

        Question question = questionRepository
                .findById(answerRequest.getQuestionId())
                .orElseThrow(
                        () -> new RuntimeException("Question not found with id: " + answerRequest.getQuestionId()));

        Services service = servicesRepository
                .findById(answerRequest.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + answerRequest.getServiceId()));

        answer.setText(answerRequest.getText());
        answer.setQuestion(question);
        answer.setService(service);

        return convertToResponse(answerRepository.save(answer));
    }

    public void deleteAnswer(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new RuntimeException("Answer not found with id: " + id);
        }
        answerRepository.deleteById(id);
    }

    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestion_Id(questionId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AnswerResponse> getAnswersByServiceId(Long serviceId) {
        return answerRepository.findByServiceId(serviceId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AnswerResponse convertToResponse(Answer answer) {
        AnswerResponse response = new AnswerResponse();
        response.setId(answer.getId());
        response.setAnswerText(answer.getText());
        response.setQuestionText(answer.getQuestion().getText());
        response.setServiceName(answer.getService().getServiceName());
        response.setServiceDescription(answer.getService().getDescription());
        return response;
    }
}
