package com.itproger.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.itproger.shoppinglist.R
import com.itproger.shoppinglist.databinding.ListNameItemBinding
import com.itproger.shoppinglist.entities.NoteItem
import com.itproger.shoppinglist.entities.ShoppingListName

//27.1 Создали новый класс (скопировали с NoteAdapter) и заменили NoteAdapter на ShoppingListName
//27.13 Пока убираем интерфейс listener : Listener
class ShopListNameAdapter() : ListAdapter<ShoppingListName, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position)) //27.12 Пока убираем интерфейс listener
    }

    //подключаем созданную форму note list item к Binding
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view) //27.4 Заменили NoteListItemBinding на ListNameItemBinding

        //функция для заполнения формы разметки note list item
        //27.2 Заменили NoteItem на ShoppingListName
        //27.3 Изменяем название переменной note на shopListNameItem
        //27.11 Пока убираем интерфейс listener: Listener
        fun setData(shopListNameItem: ShoppingListName) = with(binding){
            tvListName.text = shopListNameItem.name //27.5 Заменили tvTitle на tvListName, а title заменили на name
            //tvDescription.text = HtmlManager.getFromHtml(shopListNameItem.content).trim() //27.6 Эту сроку мы убираем
            tvTime.text = shopListNameItem.time
            itemView.setOnClickListener{
                //listener.onClickItem(shopListNameItem) 27.7 Эту строку тоже убираем
            }
            imDelete.setOnClickListener{
                //listener.deleteItem(shopListNameItem.id!!) 27.8 Эту строку тоже убираем
            }

        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.list_name_item, parent, false)) //27.13 Заменяем note_list_item на list_name_item
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListName>(){ //27.9 Меняем NoteItem на ShoppingListName
        override fun areItemsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {//27.10 Меняем NoteItem на ShoppingListName
            return oldItem == newItem
        }

    }
    //прописываем интерфейс для кнопки удалить
    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)

    }
}