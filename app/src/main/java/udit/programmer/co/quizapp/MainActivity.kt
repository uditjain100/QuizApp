package udit.programmer.co.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import udit.programmer.co.quizapp.Adapter.CategoryAdapter
import udit.programmer.co.quizapp.Common.SplacesItemDecoration
import udit.programmer.co.quizapp.Interface.OnRecyclerViewItemClickListener
import udit.programmer.co.quizapp.MVVM.ViewModelClass
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Room.AppDatabase

const val DB_NAME = "QuizAsset.db"

class MainActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    val vm by lazy {
        ViewModelProvider(this).get(ViewModelClass::class.java)
    }
    val list = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_layout.setHasFixedSize(true)
        rv_layout.layoutManager = GridLayoutManager(this, 2)

        db.todoDao().getCategories().observe(this, Observer {
            list.addAll(it)
        })

//        vm.fetchCategories(this)
//        vm.categories.observe(this, Observer {
//            list.addAll(it)
//        })
        val adapter = CategoryAdapter(list)
        rv_layout.addItemDecoration(SplacesItemDecoration(4))
        adapter.onRecyclerViewItemClickListener = object : OnRecyclerViewItemClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "${list[position].name} clicked",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
