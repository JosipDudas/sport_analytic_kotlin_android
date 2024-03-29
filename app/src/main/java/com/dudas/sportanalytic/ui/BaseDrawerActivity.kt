package com.dudas.sportanalytic.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.ui.data_edit.DataEditMainFragment
import com.dudas.sportanalytic.ui.location.LocationFragment
import com.dudas.sportanalytic.ui.main.MainFragment
import com.dudas.sportanalytic.ui.reports.ReportFragment
import com.dudas.sportanalytic.ui.reservations.ReservationFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.toast

abstract class BaseDrawerActivity : BaseActivity(), BaseDrawerActivityViewModel.CallBack {

    private lateinit var viewModel: BaseDrawerActivityViewModel

    companion object {
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = BaseDrawerActivityViewModel(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.main_content, MainFragment.newInstance()).commit()
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupNavMenu(drawer_layout.nav_menu)
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
        displayLoggedinUser(preferences.getUser() != null)
    }

    private fun setupNavMenu(nav_menu: NavigationView) {
        nav_menu.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_data_edit -> {
                    if (preferences.getUser()!!.position==USER && preferences.getLocation() == null) {
                        toast(R.string.location_message)
                    } else {
                        clearFragmentBackStack()
                        openFragment(DataEditMainFragment.newInstance())
                    }
                }
                R.id.nav_reports -> {
                    if (preferences.getLocation() == null) {
                        toast(R.string.location_message)
                    } else {
                        clearFragmentBackStack()
                        openFragment(ReportFragment.newInstance())
                    }
                }
                R.id.nav_location -> {
                    clearFragmentBackStack()
                    openFragment(LocationFragment.newInstance())
                }
                R.id.nav_login -> {
                    //startActivity<LoginActivity>()
                }
                R.id.nav_logout -> {
                    logout(true)
                    displayLoggedinUser(false)
                }
                R.id.nav_reservations -> {
                    if (preferences.getLocation() == null) {
                        toast(R.string.location_message)
                    } else {
                        clearFragmentBackStack()
                        openFragment(ReservationFragment.newInstance())
                    }
                }
                else -> drawer_layout.closeDrawers()
            }
            true
        }
    }

    private fun displayLoggedinUser(isUserLogedIn: Boolean) {
        nav_menu.menu.findItem(R.id.nav_login).isVisible = !isUserLogedIn
        nav_menu.menu.findItem(R.id.nav_logout).isVisible = isUserLogedIn
    }

    override fun onBackPressed() {
        viewModel.backDrawerChecker(drawer_layout.isDrawerOpen(GravityCompat.START))
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun closeApp() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFragment(fragmentToOpen: Fragment) {
        supportFragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, fragmentToOpen)
            .addToBackStack(null)
            .commit()
    }

    private fun clearFragmentBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}