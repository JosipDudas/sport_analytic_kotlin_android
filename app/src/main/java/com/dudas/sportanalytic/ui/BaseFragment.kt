package com.dudas.sportanalytic.ui

import android.content.Context
import com.dudas.sportanalytic.App
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.dagger.*
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.MyPreferences
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

abstract class BaseFragment : androidx.fragment.app.Fragment() {
    private val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent
            .builder()
            .activityModule(ActivityModule(activity as BaseActivity))
            .appModule(AppModule((activity as BaseActivity).application as App))
            .commonModule(CommonModule())
            .build()
    }

    @Inject
    protected lateinit var preferences: MyPreferences
    @Inject
    protected lateinit var connector: SportAnalyticDB
    @Inject
    protected lateinit var eventBus : EventBus
    @Inject
    lateinit var sportAnalyticService: SportAnalyticService

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context !is BaseActivity){
            throw IllegalStateException( "$javaClass.name must be of type ${BaseActivity::class.simpleName}")
        }
        activityComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        //eventBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        //eventBus.unregister(this)
    }
}