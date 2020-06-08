package udit.programmer.co.quizapp.MVVM

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Models.Question

class ViewModelClass : ViewModel() {

    val categories = MutableLiveData<MutableList<Category>>()
    val questions = MutableLiveData<MutableList<Question>>()

    fun fetchCategories(context: Context) {
        viewModelScope.launch {
            val list = Repository(context).getCategories()
            list.apply {
                categories.postValue(this.value)
            }
        }
    }

//    fun fetchQuestions(context: Context, categoryId: Int) {
//        viewModelScope.launch {
//            val list = withContext(Dispatchers.IO) {
//                Repository(context).getQuestionsByCategoryId(categoryId)
//            }
//            list.apply {
//                questions.postValue(this.value)
//            }
//        }
//    }

}