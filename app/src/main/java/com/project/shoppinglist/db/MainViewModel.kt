package com.project.shoppinglist.db

import androidx.lifecycle.*
import com.project.shoppinglist.entities.LibraryItem
import com.project.shoppinglist.entities.NoteItem
import com.project.shoppinglist.entities.ShopListItem
import com.project.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//класс для передачи данных в базу данных(используем карутину)
class MainViewModel(database : MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val libraryItems = MutableLiveData<List<LibraryItem>>()

    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> =
        dao.getAllShopListNames().asLiveData()

    //36.4 Показываем все ранее сохраненные в базе элементы в выбранном листе
    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>> {
        return dao.getAllShopListItems(listId).asLiveData()
    }

    //45.3
    fun getAllLibraryItems(name: String)= viewModelScope.launch {
        libraryItems.postValue(dao.getAllLibraryItems(name))
    }


    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListNote(listNameItem)
    }

    //35.2
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if(!isLibraryItemExists(shopListItem.name)) dao.insertLibraryItem(LibraryItem(null, shopListItem.name))
    }
    //39.2
    fun updateListItem(item: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(item)
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
    fun deleteShopList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if (deleteList) dao.deleteShopListName(id)
        dao.deleteShopItemsListId(id)

    }
        //44
        private suspend fun isLibraryItemExists(name: String): Boolean{
            return dao.getAllLibraryItems(name).isNotEmpty()
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