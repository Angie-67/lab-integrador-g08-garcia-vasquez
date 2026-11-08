package com.tecsup.catalogo_pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE items ADD COLUMN abilitiesJson TEXT NOT NULL DEFAULT '[]'"
                )
                db.execSQL("ALTER TABLE items ADD COLUMN dexNumber INTEGER")
            }
        }
    }
}
