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
                showQuestions()
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
                Common.selectedCategory = category
                startActivity(Intent(this@MainActivity, QuestionsActivity::class.java))
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

    val questions_list = mutableListOf<Question>()

    private fun showQuestions() {
        val q_list = arrayOf(
            "Where is it",
            "What is the capital of Turkey?",
            "What is the national animal of China?",
            "Which Turkish city has the name of a cartoon character?",
            "What is the noisiest city in the world?",
            "What is the name of the desert area in Mexico?",
            "What is a very cold part of Russia?",
            "How many continents are there?",
            "On which Italian island is Palermo?",
            "How many time zones are there in the world?",
            "Which is the largest desert on earth?",
            "Which river is flowing through Rome?",
            "Which country did once have the name Rhodesia?",
            "What island, which belonged to Denmark, was independent in 1944?",
            "What is the largest state of the United States?",
            "On which continent can you visit Sierra Leone?",
            "Which European river does flow through six different countries?",
            "What is the longest river in Europe?",
            "Through which capital does the Liffey river flow?",
            "What is the second largest country in Europe after Russia?",
            "What is the capital of the American state Hawaii?",
            "What do the Japanese people call their own country?",
            "In which country is Krakow located?",
            "What is the largest city in Canada?",
            "What is the capital city of Australia?",
            "Which two seas are joined by the Suez Canal?",
            "Where is it ?",
            "Where is it ?",
            "Where is it ?",
            "Where is it ?",
            "What color are often the domes of churches in Russia?",
            "In which Spanish city did the Joan Miro museum open in 1975?",
            "Which Italian artist painted the Birth of Venus?",
            "Who was the original author of Dracula?",
            "In which city is the famous Manneken Pis fountain?",
            "Who is the inventor of photography?",
            "What is the first book of the Old Testament?",
            "Who did the Mona Lisa paint?",
            "In which English town did Adolf Hitler study art?",
            "Which famous French engineer designed two bridges for the city of Porto?",
            "In which city can you see Michelangelos David?",
            "What is called the Jewish candlestick with special religious meaning?",
            "What is the Kabbalah?",
            "Who painted the ceiling of the Sistine Chapel?",
            "In which country was the famous painter El Greco born?",
            "In which city is the composer Frederic Chopin buried?",
            "Who did paint the famous painting Guernica?",
            "In which city did Romeo and Julia live?",
            "This is multi choice question",
            "This is multi choice question 2",
            "This is multi choice question 3",
            "This is multi choice question 4",
            "This is multi choice question 5",
            "This is multi choice question 6"
        )
        val i_list = arrayOf(
            "https://cdn-web.sidlee.com/-/media/sidlee/cities/paris/sidlee-cities-pre-par-01_798x895.jpg?mw=1420&hash=0DA87CBA5A6626B67D73CB08579BDFD6E46CB2E6",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "https://www.newyorkpass.com/images/rebrand/prices_01.jpg",
            "https://cdn.crtkl.com/wp-content/uploads/sites/1/2015/07/hongkong-office.jpg",
            "http://www.abc.net.au/news/image/6887602-3x2-700x467.jpg",
            "https://dynaimage.cdn.cnn.com/cnn/q_auto,w_900,c_fill,g_auto,h_506,ar_16:9/http%3A%2F%2Fcdn.cnn.com%2Fcnnnext%2Fdam%2Fassets%2F150806121501-sg-50---lead-image.jpg",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        val i_bool_list = arrayOf(
            true,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            true, true, true, true,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false
        )
        val c_ans_list = arrayOf(
            "A", "B", "C", "D", "B",
            "A", "B", "B", "C", "C",
            "A", "D", "C", "C", "B",
            "A", "A", "D", "B", "A",
            "D", "A", "B", "A", "D",
            "C", "A", "B", "A", "D",
            "A", "B", "C", "D", "A",
            "A", "A", "B", "C", "D",
            "A", "B", "C", "D", "A",
            "B", "C", "D", "C,D", "A,D",
            "B,D", "A,C", "A,B", "B,C"
        )
        val c_list = arrayOf(
            2, 2, 2, 2, 2,
            2, 2, 2, 2, 2,
            2, 2, 2, 2, 2,
            2, 2, 2, 2, 2,
            2, 2, 2, 2, 2,
            2, 2, 2, 2, 2,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 3, 3,
            3, 3, 3, 3
        )
        val A_list = arrayOf(
            "A. Paris",
            "A. Hanoi",
            "A. Tiger",
            "A. Superman",
            "A. Bombay",
            "A. Sonora",
            "A. Nortwest",
            "A. Four",
            "A. Crystalmere",
            "A. One",
            "A. Sahara",
            "A. Han River",
            "A. USA",
            "A. Philipines",
            "A. Newyork",
            "A. Africa",
            "A. Donau river",
            "A. Tiber",
            "A. Donau river",
            "A. France",
            "A. USA",
            "A. Nippon",
            "A. Africa",
            "A. Toronto",
            "A. Africa",
            "A. Africa",
            "A. Newyork",
            "A. Sahara",
            "A. Sydney",
            "A. Sydney",
            "A. Gold colored",
            "A. Gold colored",
            "A. Gold colored",
            "A. Gold colored",
            "A. Brussels",
            "A. Daguerre",
            "A. Genesis",
            "A. Genesis",
            "A. Genesis",
            "A. Genesis",
            "A. Florence",
            "A. Florence",
            "A. Florence",
            "A. Florence",
            "A. Greece",
            "A. Greece",
            "A. Greece",
            "A. Greece",
            "A. Wrong Answer",
            "A. Right Answer",
            "A. Wrong Answer",
            "A. Right Answer",
            "A. Right Answer",
            "A. Wrong Answer"
        )
        for (i in 0..14) {
            val question = Question(
                i,
                q_list[i],
                i_list[i],
                A_list[i],
                A_list[i],
                A_list[i],
                A_list[i],
                c_ans_list[i],
                i_bool_list[i],
                c_list[i]
            )
            questions_list.add(question)
        }
        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertAllQuestions(questions_list)
            }
            finish()
        }
    }

}