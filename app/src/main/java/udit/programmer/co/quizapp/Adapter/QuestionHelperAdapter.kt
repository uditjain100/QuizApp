package udit.programmer.co.quizapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_question_list_helper_item.view.*
import udit.programmer.co.quizapp.Common.Common
import udit.programmer.co.quizapp.Interface.OnHelperRecyclerViewClickListener
import udit.programmer.co.quizapp.Interface.OnRecyclerViewItemClickListener
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.R

class QuestionHelperAdapter(var answerSheetList: MutableList<CurrentQuestion>) :
    RecyclerView.Adapter<QuestionHelperViewHolder>() {

    var onHelperRecyclerViewClickListener: OnHelperRecyclerViewClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHelperViewHolder {
        return QuestionHelperViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_question_list_helper_item, parent, false)
        )
    }

    override fun getItemCount(): Int = answerSheetList.size

    override fun onBindViewHolder(holder: QuestionHelperViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onHelperRecyclerViewClickListener?.onClick(position)
        }
        holder.itemView.text_question_num.text = (position + 1).toString()
        if (answerSheetList[position].type == Common.ANSWER_TYPE.RIGHT_ANSWER) {
            holder.itemView.layout_wrapper.setBackgroundResource(R.drawable.grid_right_answer_layout)
        } else if (answerSheetList[position].type == Common.ANSWER_TYPE.WRONG_ANSWER) {
            holder.itemView.layout_wrapper.setBackgroundResource(R.drawable.grid_wrong_answer_layout)
        } else {
            holder.itemView.layout_wrapper.setBackgroundResource(R.drawable.grid_item_no_answer)
        }
    }

}