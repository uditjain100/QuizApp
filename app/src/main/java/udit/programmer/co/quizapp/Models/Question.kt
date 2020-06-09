package udit.programmer.co.quizapp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Question")
data class Question(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var ID: Int,
    @ColumnInfo(name = "QuestionText")
    var QuestionText: String?,
    @ColumnInfo(name = "QuestionImage")
    var QuestionImage: String?,
    @ColumnInfo(name = "AnswerA")
    var AnswerA: String?,
    @ColumnInfo(name = "AnswerB")
    var AnswerB: String?,
    @ColumnInfo(name = "AnswerC")
    var AnswerC: String?,
    @ColumnInfo(name = "AnswerD")
    var AnswerD: String?,
    @ColumnInfo(name = "CorrectAnswer")
    var CorrectAnswer: String?,
    @ColumnInfo(name = "IsImageQuestion")
    var IsImageQuestion: Boolean?,
    @ColumnInfo(name = "CategoryID")
    var CategoryID: Int?
)