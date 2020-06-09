package udit.programmer.co.quizapp.Interface

import udit.programmer.co.quizapp.Models.Category

interface OnRecyclerViewItemClickListener {
    fun onClick(category: Category)
}