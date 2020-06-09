package udit.programmer.co.quizapp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "questiontxt")
    var questiontxt: String?,
    @ColumnInfo(name = "questionImage")
    var questionImage: String?,
    @ColumnInfo(name = "answerA")
    var answerA: String?,
    @ColumnInfo(name = "answerB")
    var answerB: String?,
    @ColumnInfo(name = "answerC")
    var answerC: String?,
    @ColumnInfo(name = "answerD")
    var answerD: String?,
    @ColumnInfo(name = "correctAnswer")
    var correctAnswer: String?,
    @ColumnInfo(name = "isImageQuestion")
    var isImageQuestion: Boolean,
    @ColumnInfo(name = "categoryId")
    var categoryId: Int
)