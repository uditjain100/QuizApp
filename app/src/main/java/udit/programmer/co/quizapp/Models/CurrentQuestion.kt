package udit.programmer.co.quizapp.Models

import udit.programmer.co.quizapp.Common.Common

data class CurrentQuestion (
    var questionIndex : Int,
    var type : Common.ANSWER_TYPE
)