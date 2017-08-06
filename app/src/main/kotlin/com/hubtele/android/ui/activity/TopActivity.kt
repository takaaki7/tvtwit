package com.hubtele.android.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.hubtele.android.Constants
import com.hubtele.android.R
import com.hubtele.android.ui.fragment.ProgramTableFragment
import com.hubtele.android.ui.fragment.RankingFragment
import com.hubtele.android.ui.fragment.SearchFragment
import com.hubtele.android.ui.helper.InputHelper
import com.hubtele.android.ui.helper.ReviewDialogHelper
import kotlinx.android.synthetic.main.activity_top.*
import kotlinx.android.synthetic.main.activity_top.view.*
import java.util.*
import timber.log.Timber

class TopActivity : BaseActivity() {
    override fun getPageName(): String? = null
    override val contentViewId: Int = R.layout.activity_top
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(myToolbar)
        setupViewPagerAndTabs()
        supportActionBar!!.setDisplayShowTitleEnabled(false);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_info) {
            intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setupViewPagerAndTabs() {
        var pagerAdapter = MyPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(ProgramTableFragment.newInstance(), getString(R.string.tab_title_programs))
        pagerAdapter.addFragment(RankingFragment.newInstance(), getString(R.string.tab_title_ranking))
        pagerAdapter.addFragment(SearchFragment.newInstance(), getString(R.string.tab_title_search))
        viewPager.adapter = pagerAdapter
        showToolbarOnChangePage()
        tabLayout.setupWithViewPager(viewPager)
    }

    fun showToolbarOnChangePage() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                InputHelper.hideKeyboard(viewPager)
                when(position){
                    0 ->sendGA("program_table")
                    1 ->sendGA("ranking")
                    2 ->sendGA("search")
                }
                //                appBarLayout.setExpanded(true)
            }
        })
    }

    class MyPagerAdapter(val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        val fragmentList = ArrayList<Fragment>()
        val fragmentTitleList = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getItem(position: Int): Fragment = fragmentList.get(position)

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence = fragmentTitleList.get(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.Chat.BOARD_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            ReviewDialogHelper().showDialogIfTiming(this, data!!.getLongExtra(Constants.IntentExtraKey.LONG_CHAT_CREATED, 0L))
        }
    }
}