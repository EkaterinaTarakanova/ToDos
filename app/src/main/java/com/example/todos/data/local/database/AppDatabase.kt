package com.example.todos.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todos.data.local.database.dao.TodoDao
import com.example.todos.data.local.database.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE todos_items_new (
                        uid TEXT PRIMARY KEY NOT NULL,
                        text TEXT NOT NULL,
                        importance TEXT NOT NULL,
                        color TEXT,
                        deadline INTEGER NOT NULL,
                        is_done INTEGER NOT NULL,
                        created_at INTEGER,
                        changed_at INTEGER,
                        is_synced INTEGER NOT NULL DEFAULT 1
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO todos_items_new 
                    (uid, text, importance, color, deadline, is_done, created_at, changed_at, is_synced)
                    SELECT 
                        uid, 
                        text, 
                        importance, 
                        color, 
                        deadline, 
                        is_done, 
                        created_at, 
                        changed_at,
                        1 
                    FROM todos_items
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE todos_items")

                db.execSQL("ALTER TABLE todos_items_new RENAME TO todos_items")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE todos_items_new_v3 (
                        uid TEXT PRIMARY KEY NOT NULL,
                        text TEXT NOT NULL,
                        importance TEXT NOT NULL,
                        color TEXT,
                        deadline INTEGER NOT NULL,
                        is_done INTEGER NOT NULL,
                        created_at INTEGER,
                        changed_at INTEGER,
                        sync_status TEXT NOT NULL DEFAULT 'synced'
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    INSERT INTO todos_items_new_v3 
                    (uid, text, importance, color, deadline, is_done, created_at, changed_at, sync_status)
                    SELECT 
                        uid, 
                        text, 
                        importance, 
                        color, 
                        deadline, 
                        is_done, 
                        created_at, 
                        changed_at,
                        CASE 
                            WHEN is_synced = 1 THEN 'synced'
                            ELSE 'pending'
                        END
                    FROM todos_items
                    """.trimIndent()
                )

                database.execSQL("DROP TABLE todos_items")
                database.execSQL("ALTER TABLE todos_items_new_v3 RENAME TO todos_items")
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE todos_items_new_v4 (
                        uid TEXT PRIMARY KEY NOT NULL,
                        text TEXT NOT NULL,
                        importance TEXT NOT NULL,
                        task_color TEXT,  
                        deadline INTEGER NOT NULL,
                        is_done INTEGER NOT NULL,
                        created_at INTEGER,
                        changed_at INTEGER
                        -- sync_status 
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    INSERT INTO todos_items_new_v4 
                    (uid, text, importance, task_color, deadline, is_done, created_at, changed_at)
                    SELECT 
                        uid, text, importance, color, deadline, is_done, created_at, changed_at
                    FROM todos_items
                    """.trimIndent()
                )

                database.execSQL("DROP TABLE todos_items")
                database.execSQL("ALTER TABLE todos_items_new_v4 RENAME TO todos_items")
            }
        }


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todos_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}