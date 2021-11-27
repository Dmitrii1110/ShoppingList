package com.itproger.shoppinglist.db

import androidx.lifecycle.*
import com.itproger.shoppinglist.entities.NoteItem
import com.itproger.shoppinglist.entities.ShopListItem
import com.itproger.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//класс для передачи данных в базу данных(используем карутину)
class MainViewModel(database : MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListNote(listNameItem)
    }

    //35.2
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    //29.2 Добавялем функцию редактирования названия списка покупок через кнопку
    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListNameItem)
    }

    //функция удаления записей через карутину
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    //28.2 Создаём функцию для делите shopListName
    fun deleteShopListName(id: Int) = viewModelScope.launch {
        dao.deleteShopListName(id)
    }

    //создаем класс посредник для соединения с базой данных
    class MainViewModelFactory(val database: MainDataBase) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }

    }
}