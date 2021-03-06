package com.project.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.shoppinglist.entities.NoteItem
import com.project.shoppinglist.entities.ShopListNameItem
import com.project.shoppinglist.entities.ShopListItem
import kotlinx.coroutines.flow.Flow

@Dao

interface Dao {
    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>
    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    //36.1 Считываем из базы все элементы, индификатор которых совпадает с id списка
    @Query ("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    //28.1 ниже в одной функции создаём возможность удаления item
    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)
    //35.1
    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)
    @Insert
    suspend fun insertShopListNote(nameItem: ShopListNameItem)
    @Update
    suspend fun updateNote(note: NoteItem)
    //39.1
    @Update
    suspend fun updateListItem(item: ShopListItem)
    //29.1 Создаём функцию редактирования через кнопку названия списка
    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)
}