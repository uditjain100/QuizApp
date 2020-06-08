package udit.programmer.co.quizapp.MVVM

import android.content.Context
import udit.programmer.co.quizapp.Room.AppDatabase

class Repository(context: Context) {

    val db by lazy {
        AppDatabase.getDataBase(context)
    }

    fun getCategories() = db.todoDao().getCategories()
    fun getCategoriesById(id: Int) = db.todoDao().getCategoriesById(id)
    fun getQuestionsById(id: Int) = db.todoDao().getQuestionsById(id)
    fun getQuestionsByCategoryId(categoryId: Int) =
        db.todoDao().getQuestionsByCategoryId(categoryId)
}