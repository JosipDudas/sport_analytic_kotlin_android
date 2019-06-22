package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.ProductCategories

@Dao
abstract class ProductCategoriesDao {
    @Query("SELECT * FROM product_categories ORDER BY name ASC")
    abstract fun getAllProductCategories(): List<ProductCategories>

    @Query("SELECT * FROM product_categories WHERE location_id=:locationId ORDER BY name ASC")
    abstract fun getProductCategoriesForLocation(locationId: String): List<ProductCategories>

    @Query("SELECT * FROM product_categories WHERE id=:productId ORDER BY name ASC")
    abstract fun getSpecificProductCategories(productId: String): ProductCategories

    @Query("DELETE FROM product_categories")
    abstract fun deleteAll()

    @Query("DELETE FROM product_categories WHERE id=:productId")
    abstract fun delete(productId: String)

    @Insert
    abstract
    fun insertProductCategories(product: ProductCategories)

    @Update
    abstract
    fun updateProductCategories(product: ProductCategories)
}