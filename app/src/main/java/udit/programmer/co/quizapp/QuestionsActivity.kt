package udit.programmer.co.quizapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.content_main.*
import udit.programmer.co.quizapp.Adapter.GridAnswerAdapter
import udit.programmer.co.quizapp.Adapter.MyFragmentAdapter
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.Room.AppDatabase
import java.util.concurrent.TimeUnit

class QuestionsActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    lateinit var countDownTimer: CountDownTimer
    var time_play = Common.TOTAL_TIME
    lateinit var adapter: GridAnswerAdapter
    lateinit var fragmentAdapter: MyFragmentAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration
    var fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        generateQuestions()
    }

    private fun generateFragmentList() {
        for (i in Common.questionList.indices) {
            val bundle = Bundle()
            bundle.putInt("index", i)
            val fragment = QuestionFragment()
            fragment.arguments = bundle
            Common.fragmentList.add(fragment)
        }
    }

    private fun generateItems() {
        for (i in Common.questionList.indices)
            Common.answer_sheet_list.add(CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER))
    }

    private fun countTimer() {
        countDownTimer = object : CountDownTimer(Common.TOTAL_TIME.toLong(), 1000) {

            override fun onFinish() {
                finishQuiz()
            }

            override fun onTick(interval: Long) {
                txt_timer.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(interval),
                    TimeUnit.MILLISECONDS.toSeconds(interval) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(interval)
                    )
                )
                time_play -= 1000
            }

        }
    }

    private fun finishQuiz() {

    }

    private fun generateQuestions() {
        db.todoDao().getQuestionsByCategoryId(Common.selectedCategory!!.Id).observe(this, Observer {
            Common.questionList.addAll(it)
            if (Common.questionList.size == 0) {
                MaterialStyledDialog.Builder(this)
                    .setTitle("Ooops...")
                    .setDescription("We don't have any questions in this ${Common.selectedCategory!!.Name} category")
                    .setIcon(R.drawable.ic_launcher_foreground)
                    .setPositiveText("OK")
                    .onPositive {
                        finish()
                    }.show()
            } else if (Common.questionList.size > 0) {
                txt_timer.visibility = View.VISIBLE
                right_answer.visibility = View.VISIBLE

                countTimer()

                generateItems()

                grid_answer_rv_layout.setHasFixedSize(true)
                if (Common.questionList.size > 0) {
                    grid_answer_rv_layout.layoutManager = GridLayoutManager(
                        this,
                        if (Common.questionList.size > 5) Common.questionList.size / 2 else Common.questionList.size
                    )
                }
                adapter = GridAnswerAdapter(Common.answer_sheet_list)
                grid_answer_rv_layout.adapter = adapter

                generateFragmentList()
                fragmentAdapter = MyFragmentAdapter(fragmentManager, Common.fragmentList)
                view_pager.offscreenPageLimit = Common.questionList.size
                view_pager.adapter = fragmentAdapter
                sliding_tabs.setupWithViewPager(view_pager)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.quiz, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}