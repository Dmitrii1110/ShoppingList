package com.itproger.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.itproger.shoppinglist.R
import com.itproger.shoppinglist.databinding.ListNameItemBinding
import com.itproger.shoppinglist.entities.ShopListNameItem
import com.itproger.shoppinglist.entities.ShoppingListItem

//27.1 Создали новый класс (скопировали с NoteAdapter) и заменили NoteAdapter на ShoppingListName
//27.13 Пока убираем интерфейс listener : Listener
//28.7 Снова добавляем интерфейс listener : Listener
class ShopListItemAdapter(private val listener: Listener) : ListAdapter<ShoppingListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) ItemHolder.createShopItem(parent)//передает норальная разметка
             else
                 ItemHolder.createLibraryItem(parent)//создаться другая разметка с элементами из библиотеки
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if(getItem(position).itemType == 0){
            holder.setItemData(getItem(position), listener) //27.12 Пока убираем интерфейс listener //28.10 Вновь добавляем
        } else {
            holder.setLibraryData (getItem(position), listener)
        }
    }

    //33.5
    override fun getItemViewType(position: Int): Int {
        return super.getItem(position).itemType
    }
    //подключаем созданную форму note list item к Binding
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view) //27.4 Заменили NoteListItemBinding на ListNameItemBinding

        //функция для заполнения формы разметки note list item
        //27.2 Заменили NoteItem на ShoppingListName
        //27.3 Изменяем название переменной note на shopListNameItem
        //27.11 Пока убираем интерфейс listener: Listener
        //28.8 Снова добавяляем listener : Listener
        fun setItemData(shopListItem: ShoppingListItem, listener : Listener) = with(binding){

        }

        //33.4
        fun setLibraryData(shopListNameItem: ShoppingListItem, listener : Listener) = with(binding){

        }
        companion object{
            //33.1 Вносим изменения
            fun createShopItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.list_name_item, parent, false)) //27.13 Заменяем note_list_item на list_name_item
            }
            fun createLibraryItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.list_name_item, parent, false))
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListItem>(){ //27.9 Меняем NoteItem на ShoppingListName
        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShoppingListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {//27.10 Меняем NoteItem на ShoppingListName
            return oldItem == newItem
        }

    }
    //прописываем интерфейс для кнопки удалить
    interface Listener{
        fun deleteItem(id: Int)
        fun editItem(shopListNameItem : ShopListNameItem) //29.4 Создаём функцию редактирования заголовка листа покупок
        fun onClickItem(shopListNameItem: ShopListNameItem)

    }
}