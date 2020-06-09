package udit.programmer.co.quizapp

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
import udit.programmer.co.quizapp.Common.SpacesItemDecoration
import udit.programmer.co.quizapp.Interface.OnRecyclerViewItemClickListener
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Room.AppDatabase

const val DB_NAME = "QuizAsset.db"

class MainActivity : AppCompatActivity() {

//    val vm by lazy {
//        ViewModelProvider(this).get(ViewModelClass::class.java)
//    }

    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    val category_list = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        c_toolbar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showCategories()
            }
        })
        db.todoDao().getCategories().observe(this, Observer {
            category_list.addAll(it)
        })
        rv_layout.setHasFixedSize(true)
        rv_layout.layoutManager = GridLayoutManager(this, 2)

//        vm.fetchCategories(this)
//        vm.categories.observe(this, Observer {
//            list.addAll(it)
//        })

        val adapter = CategoryAdapter(category_list)
        rv_layout.addItemDecoration(SpacesItemDecoration(4))
        adapter.onRecyclerViewItemClickListener = object : OnRecyclerViewItemClickListener {
            override fun onClick(category: Category) {
                Toast.makeText(
                    this@MainActivity,
                    "${category.name} clicked",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        rv_layout.adapter = adapter
    }

    private fun showCategories() {
        val name_list = arrayOf(
            "Art / Culture", "Geography",
            "Music", "Economy",
            "History", "Nature",
            "Film / TV", "Informatics",
            "Food and drink", "Language",
            "Science", "General",
            "Literature", "Sports",
            "Politics"
        )
        for (i in 0..14) {
            val category = Category(i, name_list[i], "")
            category_list.add(category)
        }
        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertAllCategories(category_list)
            }
            finish()
        }
    }
}