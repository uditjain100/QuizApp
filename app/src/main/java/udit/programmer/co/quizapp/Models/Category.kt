package udit.programmer.co.quizapp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var Id: Int,
    @ColumnInfo(name = "Name")
    var Name: String?,
    @ColumnInfo(name = "Image")
    var Image: String?
)