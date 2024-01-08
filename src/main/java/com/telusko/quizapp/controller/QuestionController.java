package com.telusko.quizapp.controller;

import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.service.QuestionService;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "question")
public class QuestionController {
	@Autowired
	QuestionService questionService;

	@GetMapping(path="allQuestion")
	public ResponseEntity<List<Question>> getAllQuestion() {
			return questionService.getAllQuestions();
	}
	
	@GetMapping(path="category/{category}")
	public ResponseEntity<List<Question>> getQuestionByCategory(@PathVariable String category){
		return questionService.getQuestionsByCategory(category);
	}
	
	@PostMapping(value = "/report")
	private String printReport() throws JRException, ClassNotFoundException {
	    return questionService.dynamicReportBuilder();
	}
}
