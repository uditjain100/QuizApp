package udit.programmer.co.quizapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.View
import android.view.ViewParent
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.rectangle_text_view.*
import udit.programmer.co.quizapp.Adapter.GridAnswerAdapter
import udit.programmer.co.quizapp.Adapter.MyFragmentAdapter
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.Room.AppDatabase
import java.time.ZoneOffset
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
    lateinit var txt_wrong_answer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        generateQuestions()
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

        val position = view_pager.currentItem
        val questionFragment = Common.fragmentList[position]

        val question_state = questionFragment.selectedAnswer()
        Common.answer_sheet_list[position] = question_state
        adapter.notifyDataSetChanged()

        countCorrectAnswer()

        right_answer.text =
            "${Common.right_answer_count} / ${Common.questionList.size}"
        txt_wrong_answer.text = "${Common.wrong_answer_count}"

        if (question_state.type != Common.ANSWER_TYPE.NO_ANSWER) {
            questionFragment.showCorrectAnswer()
            questionFragment.disableAnswer()
        }

    }

    private fun countCorrectAnswer() {

        Common.right_answer_count = 0
        Common.wrong_answer_count = 0

        for (item in Common.answer_sheet_list) {
            if (Common.answer_sheet_list == Common.ANSWER_TYPE.RIGHT_ANSWER) {
                Common.right_answer_count++
            } else if (Common.answer_sheet_list == Common.ANSWER_TYPE.WRONG_ANSWER) {
                Common.wrong_answer_count++
            }
        }


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

                fragmentAdapter =
                    MyFragmentAdapter(supportFragmentManager, Common.questionList.size)
                view_pager.offscreenPageLimit = Common.questionList.size
                view_pager.adapter = fragmentAdapter

                sliding_tabs.setupWithViewPager(view_pager)

                view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                    val SCROLLING_RIGHT = 0
                    val SCROLLING_LEFT = 1
                    val SCROLLING_UBDETERMINED = 2

                    var currentScrollDirection = SCROLLING_UBDETERMINED

                    private val isScrollDirectionUndetermined: Boolean
                        get() = currentScrollDirection == SCROLLING_UBDETERMINED
                    private val isScrollDirectionRight: Boolean
                        get() = currentScrollDirection == SCROLLING_RIGHT
                    private val isScrollDirectionLeft: Boolean
                        get() = currentScrollDirection == SCROLLING_LEFT

                    private fun setScrollingDirection(positionOffset: Float) {
                        if (1 - positionOffset >= 0.5)
                            this.currentScrollDirection = SCROLLING_RIGHT
                        else if (1 - positionOffset <= 0.5)
                            this.currentScrollDirection = SCROLLING_LEFT
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        if (state == ViewPager.SCROLL_STATE_IDLE)
                            this.currentScrollDirection = SCROLLING_UBDETERMINED
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        if (isScrollDirectionUndetermined) setScrollingDirection(positionOffset)
                    }

                    override fun onPageSelected(p0: Int) {
                        var questionFragment: QuestionFragment
                        var position = 0
                        if (p0 > 0) {
                            if (isScrollDirectionRight) {
                                questionFragment = Common.fragmentList[p0 - 1]
                                position = p0 - 1
                            } else if (isScrollDirectionLeft) {
                                questionFragment = Common.fragmentList[p0 + 1]
                                position = p0 + 1
                            } else {
                                questionFragment = Common.fragmentList[p0]
                            }
                        } else {
                            questionFragment = Common.fragmentList[0]
                            position = 0
                        }
                        if (Common.answer_sheet_list[position].type == Common.ANSWER_TYPE.NO_ANSWER) {
                            val question_state = questionFragment.selectedAnswer()
                            Common.answer_sheet_list[position] = question_state
                            adapter.notifyDataSetChanged()

                            countCorrectAnswer()

                            right_answer.text =
                                "${Common.right_answer_count} / ${Common.questionList.size}"
                            txt_wrong_answer.text = "${Common.wrong_answer_count}"

                            if (question_state.type != Common.ANSWER_TYPE.NO_ANSWER) {
                                questionFragment.showCorrectAnswer()
                                questionFragment.disableAnswer()
                            }
                        }
                    }
                })
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu!!.findItem(R.id.menu_wrong_answer)
        val layout = item.actionView as RelativeLayout
        txt_wrong_answer = layout.findViewById(R.id.menu_wrong_answer) as TextView
        txt_wrong_answer.text = 0.toString()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.quiz, menu)
        return true
    }

}