package com.example.ecommerceapp.core.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS addresses (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                userFullName TEXT NOT NULL,
                details TEXT NOT NULL,
                isSelect INTEGER NOT NULL,
                phone TEXT NOT NULL,
                city TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}