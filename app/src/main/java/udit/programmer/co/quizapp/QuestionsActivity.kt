package udit.programmer.co.quizapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.rectangle_text_view.*
import udit.programmer.co.quizapp.Adapter.GridAnswerAdapter
import udit.programmer.co.quizapp.Adapter.MyFragmentAdapter
import udit.programmer.co.quizapp.Adapter.QuestionHelperAdapter
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Common.SpacesItemDecoration
import udit.programmer.co.quizapp.Interface.OnHelperRecyclerViewClickListener
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
    lateinit var helper_adapter: QuestionHelperAdapter
    internal var goToQuestion: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action!!.toString() == Common.KEY_GO_TO_QUESTION) {
                val question = intent.getIntExtra(Common.KEY_GO_TO_QUESTION, -1)
                if (question != -1) view_pager.currentItem = question
                drawer_layout.closeDrawer(Gravity.LEFT)
            }
        }

    }
    var isAnswerModeView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(goToQuestion, IntentFilter(Common.KEY_GO_TO_QUESTION))

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        answer_sheet.setHasFixedSize(true)
        answer_sheet.layoutManager = GridLayoutManager(this, 3)
        answer_sheet.addItemDecoration(SpacesItemDecoration(2))

        btn_done.setOnClickListener {
            if (!isAnswerModeView) {
                MaterialStyledDialog.Builder(this)
                    .setTitle("Finish")
                    .setDescription("Do you really want to finish?")
                    .setIcon(R.drawable.ic_baseline_mood_24)
                    .setNegativeText("No")
                    .onNegative {
                        finish()
                    }.setPositiveText("Yes")
                    .onPositive {
                        finishQuiz()
                        drawer_layout.closeDrawer(Gravity.LEFT)
                    }.show()
            } else {
                finishQuiz()
            }
        }

        generateQuestions()

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(goToQuestion)
        if (countDownTimer != null)
            countDownTimer!!.cancel()
        if (Common.fragmentList != null)
            Common.fragmentList.clear()
        if (Common.answer_sheet_list != null)
            Common.answer_sheet_list.clear()
        super.onDestroy()
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

                generateFragmentList()

                fragmentAdapter =
                    MyFragmentAdapter(supportFragmentManager, Common.fragmentList)
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
                right_answer.text =
                    "${Common.right_answer_count} / ${Common.questionList.size}"
                helper_adapter = QuestionHelperAdapter(Common.answer_sheet_list)
                helper_adapter.onHelperRecyclerViewClickListener =
                    object : OnHelperRecyclerViewClickListener {
                        override fun onClick(position: Int) {
                            LocalBroadcastManager.getInstance(this@QuestionsActivity)
                                .sendBroadcast(
                                    Intent(Common.KEY_GO_TO_QUESTION).putExtra(
                                        Common.KEY_GO_TO_QUESTION,
                                        position
                                    )
                                )
                        }
                    }
                answer_sheet.adapter = helper_adapter
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_done -> {
                finishQuiz()
            }
        }
        return true
    }

}