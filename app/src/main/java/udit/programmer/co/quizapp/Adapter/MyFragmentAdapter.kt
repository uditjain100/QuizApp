package udit.programmer.co.quizapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import udit.programmer.co.quizapp.Models.Question
import udit.programmer.co.quizapp.QuestionFragment
import java.lang.StringBuilder

class MyFragmentAdapter(fm: FragmentManager, var size: Int) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = QuestionFragment().newInstance(position)

    override fun getCount(): Int = size

    override fun getPageTitle(position: Int): CharSequence? =
        StringBuilder("Question ").append(position + 1).toString()

}