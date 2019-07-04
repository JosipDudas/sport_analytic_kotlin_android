package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.ReportItem

@Dao
abstract class ReportItemDao {
    @Query("SELECT * FROM report_items ORDER BY product_id ASC")
    abstract fun getAllReportItems(): List<ReportItem>

    @Query("SELECT * FROM report_items WHERE report_id=:reportId ORDER BY product_id ASC")
    abstract fun getSpecificReportItems(reportId: String): List<ReportItem>

    @Query("SELECT * FROM report_items WHERE id=:reportItemId ORDER BY product_id ASC")
    abstract fun getSpecificReportItem(reportItemId: String): ReportItem

    @Query("DELETE FROM report_items")
    abstract fun deleteAll()

    @Query("DELETE FROM report_items WHERE id=:reportId")
    abstract fun delete(reportId: String)

    @Insert
    abstract
    fun insertReportItem(reportItem: ReportItem)

    @Update
    abstract
    fun updateReportItem(reportItem: ReportItem)
}