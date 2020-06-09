package udit.programmer.co.quizapp.Common

import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.Models.Question
import udit.programmer.co.quizapp.QuestionFragment

object Common {

    val TOTAL_TIME = 20 * 60 * 1000
    var answer_sheet_list: MutableList<CurrentQuestion> = ArrayList()
    var questionList: MutableList<Question> = ArrayList()
    var selectedCategory: Category? = null
    var fragmentList: MutableList<QuestionFragment> = ArrayList()

    enum class ANSWER_TYPE {
        NO_ANSWER,
        RIGHT_ANSWER,
        WRONG_ANSWER
    }

}