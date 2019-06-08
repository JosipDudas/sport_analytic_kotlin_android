package com.dudas.sportanalytic.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dudas.sportanalytic.App
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.dagger.*
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.login.LoginActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    protected val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent
            .builder()
            .activityModule(ActivityModule(this))
            .appModule(AppModule(application as App))
            .commonModule(CommonModule())
            .networkModule(NetworkModule())
            .build()
    }

    @Inject
    protected lateinit var preferences: MyPreferences
    @Inject
    protected lateinit var connector: SportAnalyticDB
    @Inject
    protected lateinit var eventBus: EventBus
    @Inject
    lateinit var sportAnalyticService: SportAnalyticService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }
            toolbar.title = getToolbarName()
        }
    }

    abstract fun getToolbarName(): String

    fun logout(showLogoutMessage: Boolean) {
        removePreferences()

        deleteDataFromDataBase()

        if(showLogoutMessage) {
            Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_LONG).show()
        }

        finish()
        startActivity(intentFor<LoginActivity>().clearTask().newTask())
    }

    private fun removePreferences() {
        preferences.removeUser()
    }

    private fun deleteDataFromDataBase() {
        connector.userDao().deleteAll()
    }

    override fun onStart() {
        super.onStart()
        //eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        //eventBus.unregister(this)
    }
}