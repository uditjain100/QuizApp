package udit.programmer.co.quizapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import udit.programmer.co.quizapp.QuestionFragment
import java.lang.StringBuilder

class MyFragmentAdapter(fm: FragmentManager, var fragmentList: MutableList<QuestionFragment>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = fragmentList.elementAt(position)

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? =
        StringBuilder("Question ").append(position + 1).toString()

}