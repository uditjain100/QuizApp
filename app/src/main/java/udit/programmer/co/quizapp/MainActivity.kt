package udit.programmer.co.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

const val DB_NAME = "QuizAsset.db"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}