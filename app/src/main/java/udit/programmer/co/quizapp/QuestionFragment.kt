package udit.programmer.co.quizapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_question.*
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Interface.SelectAnswer
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.Models.Question
import java.lang.Exception
import java.lang.StringBuilder

class QuestionFragment : Fragment(), SelectAnswer {
    var question: Question? = null
    var questionIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_question, container, false)
        questionIndex = arguments!!.getInt("index", -1)
        question = Common.questionList[questionIndex]
        if (question != null) {
            if (question!!.IsImageQuestion!!) {
                Picasso.get().load(question!!.QuestionImage)
                    .into(image_question, object : Callback {
                        override fun onSuccess() {
                            progress_bar.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            image_question.setImageResource(R.drawable.ic_baseline_error_outline_24)
                        }

                    })
            } else {
                image_layout.visibility = View.GONE
                txt_question_text.text = question!!.QuestionText
                ckb_A.text = question!!.AnswerA
                ckb_B.text = question!!.AnswerB
                ckb_C.text = question!!.AnswerC
                ckb_D.text = question!!.AnswerD
                ckb_A.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Common.selected_values.add(ckb_A.text.toString())
                    } else {
                        Common.selected_values.remove(ckb_A.text.toString())
                    }
                }
                ckb_B.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Common.selected_values.add(ckb_B.text.toString())
                    } else {
                        Common.selected_values.remove(ckb_B.text.toString())
                    }
                }
                ckb_C.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Common.selected_values.add(ckb_C.text.toString())
                    } else {
                        Common.selected_values.remove(ckb_C.text.toString())
                    }
                }
                ckb_D.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Common.selected_values.add(ckb_D.text.toString())
                    } else {
                        Common.selected_values.remove(ckb_D.text.toString())
                    }
                }
            }
        }
        return itemView
    }

    override fun selectedAnswer(): CurrentQuestion {
        Common.selected_values.distinct()
        if (Common.answer_sheet_list[questionIndex].type == Common.ANSWER_TYPE.NO_ANSWER) {
            val currentQuestion = CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER)
            val result = StringBuilder()
            if (Common.selected_values.size > 1) {
                var arrayAnswer = Common.selected_values.toTypedArray()
                for (i in arrayAnswer!!.indices) {
                    if (i < arrayAnswer!!.size - 1) {
                        result.append(StringBuilder(arrayAnswer!![i] as String).substring(0, 1))
                            .append(",")
                    } else {
                        result.append(arrayAnswer!![i] as String).substring(0, 1)
                    }
                }
            } else if (Common.selected_values.size == 1) {
                val arrayAnswer = Common.selected_values.toTypedArray()
                result.append(arrayAnswer!![0] as String).substring(0, 1)
            }

            if (question != null) {
                if (!TextUtils.isEmpty(result)) {
                    if (result.toString() == question!!.CorrectAnswer) {
                        currentQuestion.type = Common.ANSWER_TYPE.RIGHT_ANSWER
                    } else {
                        currentQuestion.type = Common.ANSWER_TYPE.WRONG_ANSWER
                    }
                } else {
                    currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER
                }
            } else {
                Toast.makeText(activity, "Cannot get Question", Toast.LENGTH_LONG).show()
                currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER
            }
            Common.selected_values.clear()
            return currentQuestion
        } else {
            return Common.answer_sheet_list[questionIndex]
        }
    }

    override fun showCorrectAnswer() {
        val correctAnswer =
            question!!.CorrectAnswer!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        for (answer in correctAnswer) {
            when {
                answer == "A" -> {
                    ckb_A.setTypeface(null, Typeface.BOLD)
                    ckb_A.setTextColor(Color.RED)
                }
                answer == "B" -> {
                    ckb_B.setTypeface(null, Typeface.BOLD)
                    ckb_B.setTextColor(Color.RED)
                }
                answer == "C" -> {
                    ckb_C.setTypeface(null, Typeface.BOLD)
                    ckb_C.setTextColor(Color.RED)
                }
                answer == "D " -> {
                    ckb_D.setTypeface(null, Typeface.BOLD)
                    ckb_D.setTextColor(Color.RED)
                }
            }
        }

    }

    override fun disableAnswer() {
        ckb_A.isEnabled = false
        ckb_B.isEnabled = false
        ckb_C.isEnabled = false
        ckb_D.isEnabled = false
    }

    override fun resetQuestion() {
        ckb_A.isEnabled = true
        ckb_B.isEnabled = true
        ckb_C.isEnabled = true
        ckb_D.isEnabled = true

        ckb_A.isChecked = false
        ckb_B.isChecked = false
        ckb_C.isChecked = false
        ckb_D.isChecked = false

        ckb_A.setTypeface(null, Typeface.NORMAL)
        ckb_A.setTextColor(Color.BLACK)
        ckb_B.setTypeface(null, Typeface.NORMAL)
        ckb_B.setTextColor(Color.BLACK)
        ckb_C.setTypeface(null, Typeface.NORMAL)
        ckb_C.setTextColor(Color.BLACK)
        ckb_D.setTypeface(null, Typeface.NORMAL)
        ckb_D.setTextColor(Color.BLACK)

        Common.selected_values.clear()
    }
}