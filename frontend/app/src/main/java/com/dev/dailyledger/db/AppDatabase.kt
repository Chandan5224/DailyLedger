package com.example.bluefield.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dev.dailyledger.db.UserDao
import com.dev.dailyledger.model.*

@Database(
    entities = [User::class],
    version = 8
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "dailyledger_db"
            )
                .build()
    }
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Adding a new Boolean column 'taskOnHold' to the 'FormData' table
        db.execSQL("ALTER TABLE FormData ADD COLUMN taskOnHold INTEGER NOT NULL DEFAULT 0")
    }
}

// Define the migration from version 4 to version 5
val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Adding a new Integer column 'isOnHoldSuccess' to the 'FormData' table
        db.execSQL("ALTER TABLE FormData ADD COLUMN isOnHoldSuccess INTEGER NOT NULL DEFAULT 0")
    }
}