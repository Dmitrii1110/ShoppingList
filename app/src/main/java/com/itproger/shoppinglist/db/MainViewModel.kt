package com.itproger.shoppinglist.db

import androidx.lifecycle.*
import com.itproger.shoppinglist.entities.NoteItem
import com.itproger.shoppinglist.entities.ShoppingListName
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//класс для передачи данных в базу данных(используем карутину)
class MainViewModel(database : MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShoppingListNames: LiveData<List<ShoppingListName>> = dao.getAllShopListNames().asLiveData()
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.insertShopListNote(listName)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    //функция удаления записей через карутину
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
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