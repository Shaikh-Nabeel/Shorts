package com.nabeel130.shorts.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "private_note_table")
class PrivateNote(@ColumnInfo(name = "pText") var pText: String) {
    @PrimaryKey(autoGenerate = true) var pId = 0
}