package udit.programmer.co.quizapp.Interface

import udit.programmer.co.quizapp.Models.CurrentQuestion

interface SelectAnswer {
    fun selectedAnswer(): CurrentQuestion
    fun showCorrectAnswer()
    fun disableAnswer()
    fun resetQuestion()
}