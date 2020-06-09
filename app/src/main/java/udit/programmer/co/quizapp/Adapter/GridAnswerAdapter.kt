package udit.programmer.co.quizapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import udit.programmer.co.quizapp.Models.CurrentQuestion
import udit.programmer.co.quizapp.R

class GridAnswerAdapter(internal var answerList: MutableList<CurrentQuestion>) :
    RecyclerView.Adapter<GridAnswerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAnswerViewHolder {
        return GridAnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_answer_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = answerList.size

    override fun onBindViewHolder(holder: GridAnswerViewHolder, position: Int) {
        holder.bind(answerList[position])
    }
}