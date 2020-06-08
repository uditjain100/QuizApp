package udit.programmer.co.quizapp.Room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import udit.programmer.co.quizapp.DB_NAME
import udit.programmer.co.quizapp.Models.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
//                    .fallbackToDestructiveMigration().addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        PopulateDBAsyncTask(INSTANCE).execute()
//                    }
//                })
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
//
//private class PopulateDBAsyncTask(var db: AppDatabase?) : AsyncTask<Unit, Unit, Unit>() {
//    val name_list = arrayOf(
//        "Art / Culture", "Geography",
//        "Music", "Economy",
//        "History", "Nature",
//        "Film / TV", "Informatics",
//        "Food and drink", "Language",
//        "Science", "General",
//        "Literature", "Sports",
//        "Politics"
//    )
//
//    override fun doInBackground(vararg params: Unit?) {
//        for (i in 0..14) {
//            var category = Category(i, name_list[i], "")
//            db!!.todoDao().insertCategories(category)
//        }
//        return
//    }
//}