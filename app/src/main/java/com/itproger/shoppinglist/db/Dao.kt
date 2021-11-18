package com.itproger.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.itproger.shoppinglist.entities.NoteItem
import com.itproger.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

@Dao

interface Dao {
    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>
    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>
    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    //28.1 ниже в одной функции создаём возможность удаления item
    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)
    @Insert
    suspend fun insertShopListNote(nameItem: ShopListNameItem)
    @Update
    suspend fun updateNote(note: NoteItem)
    //29.1 Создаём функцию редактирования через кнопку названия списка
    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)
}