package com.dudas.sportanalytic.dagger

import com.dudas.sportanalytic.ui.BaseActivity
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.data_edit.location.DataEditLocationFragment
import com.dudas.sportanalytic.ui.data_edit.location.new_edit_location.NewEditLocationFragment
import com.dudas.sportanalytic.ui.data_edit.product.DataEditProductCategoriesFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.DataEditProductFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.EditListProductFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_multiple_products.CreateNewMultipleProductsFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_product.CreateNewProductFragment
import com.dudas.sportanalytic.ui.data_edit.product.new_edit_product_categories.NewEditProductCategoriesFragment
import com.dudas.sportanalytic.ui.login.LoginActivity
import com.dudas.sportanalytic.ui.reservations.ReservationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ActivityModule::class, AppModule::class, CommonModule::class, NetworkModule::class))
interface ActivityComponent{
    fun inject(baseFragment: BaseFragment)
    fun inject(activity: LoginActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(dataEditLocationFragment: DataEditLocationFragment)
    fun inject(newEditLocationFragment: NewEditLocationFragment)
    fun inject(dataEditProductCategoriesFragment: DataEditProductCategoriesFragment)
    fun inject(newEditProductCategoriesFragment: NewEditProductCategoriesFragment)
    fun inject(dataEditProductFragment: DataEditProductFragment)
    fun inject(editListProductFragment: EditListProductFragment)
    fun inject(createNewProductFragment: CreateNewProductFragment)
    fun inject(createNewMultipleProductsFragment: CreateNewMultipleProductsFragment)
    fun inject(reservationFragment: ReservationFragment)
}