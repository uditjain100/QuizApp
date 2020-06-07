package udit.programmer.co.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import udit.programmer.co.quizapp.Adapter.CategoryAdapter
import udit.programmer.co.quizapp.Common.SplacesItemDecoration
import udit.programmer.co.quizapp.Interface.OnRecyclerViewItemClickListener
import udit.programmer.co.quizapp.Models.Category
import udit.programmer.co.quizapp.Room.AppDatabase

const val DB_NAME = "QuizAsset.db"

class MainActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    val list = db.todoDao().getCategories()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_layout.setHasFixedSize(true)
        rv_layout.layoutManager = GridLayoutManager(this, 2)

        var adapter = CategoryAdapter(list)
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