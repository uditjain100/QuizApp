package udit.programmer.co.quizapp.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_layout.view.*
import udit.programmer.co.quizapp.Models.Category

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(category: Category) {
        itemView.category_tv.text = category.name.toString()
    }
}