package udit.programmer.co.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import udit.programmer.co.quizapp.Adapter.CategoryAdapter
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Common.SpacesItemDecoration
import udit.programmer.co.quizapp.Interface.OnRecyclerViewItemClickListener
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Models.Question
import udit.programmer.co.quizapp.Room.AppDatabase

const val DB_NAME = "QuizAsset.db"
const val KEY = "CEASED_METEOR"

class MainActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    private val category_list = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db.todoDao().getCategories().observe(this, Observer {
            category_list.addAll(it)
        })

        rv_layout.setHasFixedSize(true)
        rv_layout.layoutManager = GridLayoutManager(this, 2)

        val adapter = CategoryAdapter(category_list)
        rv_layout.addItemDecoration(SpacesItemDecoration(4))
        adapter.onRecyclerViewItemClickListener = object : OnRecyclerViewItemClickListener {
            override fun onClick(category: Category) {
                Common.selectedCategory = category
                startActivity(Intent(this@MainActivity, QuestionsActivity::class.java))
            }
        }
        rv_layout.adapter = adapter
    }

}