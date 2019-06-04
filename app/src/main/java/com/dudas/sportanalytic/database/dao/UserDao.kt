package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.User

@Dao
abstract class UserDao {
    @Query("SELECT * FROM users")
    abstract fun getAllUsers(): List<User>

    @Query("DELETE FROM users")
    abstract fun deleteAll()

    @Insert
    abstract
    fun insertUser(user: User)

    @Update
    abstract
    fun updateUser(user: User)
}