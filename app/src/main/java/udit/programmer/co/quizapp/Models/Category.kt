package udit.programmer.co.quizapp.Models

import androidx.room.Entity

@Entity
data class Category(
    var id: Int,
    var name: String?,
    var image: String?
)