package com.telusko.quizapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telusko.quizapp.model.Quiz;

public interface QuizDao extends JpaRepository<Quiz, Integer>{
	
}
