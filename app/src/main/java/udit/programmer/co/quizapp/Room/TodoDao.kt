package udit.programmer.co.quizapp.Room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Models.Question

@Dao
interface TodoDao {

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<MutableList<Category>>

    @Query("SELECT * FROM category WHERE id In (:categoryId)")
    fun getCategoriesById(categoryId: Int): LiveData<MutableList<Category>>
//
//    @Insert
//    fun insertCategories(category: Category)
//
    @Insert
    fun insertAllCategories(list: MutableList<Category>)

    @Query("SELECT * FROM Question WHERE categoryId = :categoryId ORDER BY RANDOM() LIMIT 30")
    fun getQuestionsByCategoryId(categoryId: Int): LiveData<MutableList<Question>>

//    @Query("SELECT * FROM Question")
//    fun getQuestions(): LiveData<MutableList<Question>>
//
//    @Insert
//    fun insertAllQuestionsByCategories(vararg question: Question, categoryId: Int)

    @Insert
    fun insertAllQuestions(list: MutableList<Question>)

}