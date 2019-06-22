package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.Product

@Dao
abstract class ProductDao {
    @Query("SELECT * FROM products")
    abstract fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products WHERE categorie_id=:categorieId")
    abstract fun getSpecificProducts(categorieId: String): List<Product>

    @Query("SELECT * FROM products WHERE id=:productId")
    abstract fun getSpecificProduct(productId: String): Product

    @Query("DELETE FROM products")
    abstract fun deleteAll()

    @Query("DELETE FROM products WHERE id=:productId")
    abstract fun delete(productId: String)

    @Insert
    abstract
    fun insertProduct(product: Product)

    @Update
    abstract
    fun updateProduct(product: Product)
}