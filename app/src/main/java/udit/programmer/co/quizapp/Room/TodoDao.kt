package udit.programmer.co.quizapp.Room

import androidx.room.Dao
import androidx.room.Query
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Models.Question

@Dao
interface TodoDao {
    @Query("SELECT * FROM Category")
    fun getCategories(): MutableList<Category>

    @Query("SELECT * FROM Question WHERE categoryId ORDER BY RANDOM() LIMIT 30")
    fun getQuestionsByCategory(categoryId: Int): MutableList<Question>
}   