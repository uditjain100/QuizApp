package udit.programmer.co.quizapp.Models

import androidx.room.Entity

@Entity
data class Question(
    var id: Int,
    var questiontxt: String?,
    var questionImage: String?,
    var answerA: String?,
    var answerB: String?,
    var answerC: String?,
    var answerD: String?,
    var correctAnswer: String?,
    var isImageQuestion: Boolean,
    var categoryId: Int
)