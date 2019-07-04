package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.Report

@Dao
abstract class ReportDao {
    @Query("SELECT * FROM reports ORDER BY `date` ASC")
    abstract fun getAllReports(): List<Report>

    @Query("SELECT * FROM reports WHERE location_id=:locationId ORDER BY `date` ASC")
    abstract fun getSpecificReports(locationId: String): List<Report>

    @Query("SELECT * FROM reports WHERE id=:reportId ORDER BY `date` ASC")
    abstract fun getSpecificReport(reportId: String): Report

    @Query("DELETE FROM reports")
    abstract fun deleteAll()

    @Query("DELETE FROM reports WHERE id=:reportId")
    abstract fun delete(reportId: String)

    @Insert
    abstract
    fun insertReport(report: Report)

    @Update
    abstract
    fun updateReport(report: Report)
}