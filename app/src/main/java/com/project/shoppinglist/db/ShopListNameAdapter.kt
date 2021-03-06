package com.project.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ListNameItemBinding
import com.project.shoppinglist.entities.ShopListNameItem

//27.1 Создали новый класс (скопировали с NoteAdapter) и заменили NoteAdapter на ShoppingListName
//27.13 Пока убираем интерфейс listener : Listener
//28.7 Снова добавляем интерфейс listener : Listener
class ShopListNameAdapter(private val listener: Listener) : ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener) //27.12 Пока убираем интерфейс listener //28.10 Вновь добавляем
    }

    //подключаем созданную форму note list item к Binding
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view) //27.4 Заменили NoteListItemBinding на ListNameItemBinding

        //функция для заполнения формы разметки note list item
        //27.2 Заменили NoteItem на ShoppingListName
        //27.3 Изменяем название переменной note на shopListNameItem
        //27.11 Пока убираем интерфейс listener: Listener
        //28.8 Снова добавяляем listener : Listener
        fun setData(shopListNameItem: ShopListNameItem, listener : Listener) = with(binding){
            tvListName.text = shopListNameItem.name //27.5 Заменили tvTitle на tvListName, а title заменили на name
            //tvDescription.text = HtmlManager.getFromHtml(shopListNameItem.content).trim() //27.6 Эту сроку мы убираем
            tvTime.text = shopListNameItem.time
            itemView.setOnClickListener{
                listener.onClickItem(shopListNameItem) //27.7 Эту строку тоже убираем //30.8 снова добавили
            }
            //*При нажатии на эту кнопку сработает интерфейс
            imDelete.setOnClickListener{
                listener.deleteItem(shopListNameItem.id!!) //27.8 Эту строку тоже убираем //28.9 И вновь добавили
            }

            //29.3 Прописываем редактирование названия списка через слушатель нажатий и убрали индификатор
            imEdit.setOnClickListener{
                listener.editItem(shopListNameItem)
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

    class ItemComparator : DiffUtil.ItemCallback<ShopListNameItem>(){ //27.9 Меняем NoteItem на ShoppingListName
        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {//27.10 Меняем NoteItem на ShoppingListName
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