package com.dudas.sportanalytic.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.ui.BaseFragment

class MainFragment: BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, null)
    }
}