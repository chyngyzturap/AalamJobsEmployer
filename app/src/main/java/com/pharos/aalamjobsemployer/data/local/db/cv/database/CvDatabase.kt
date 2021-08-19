package com.pharos.aalamjobsemployer.data.local.db.cv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pharos.aalamjobsemployer.data.local.db.cv.dao.*
import com.pharos.aalamjobsemployer.data.local.db.cv.entities.*


@Database(
    version = 12,
    entities = [CompanyInfo::class],
    exportSchema = false

)
abstract class CvDatabase : RoomDatabase() {

    abstract fun getCompanyDao(): CompanyDao

    companion object {
        @Volatile
        private var instance: CvDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CvDatabase::class.java,
            "job_database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}
