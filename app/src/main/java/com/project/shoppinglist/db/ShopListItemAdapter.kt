package com.project.shoppinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ShopLibraryListItemBinding
import com.project.shoppinglist.databinding.ShopListItemBinding
import com.project.shoppinglist.entities.ShopListItem

//27.1 Создали новый класс (скопировали с NoteAdapter) и заменили NoteAdapter на ShoppingListName
//27.13 Пока убираем интерфейс listener : Listener
//28.7 Снова добавляем интерфейс listener : Listener
class ShopListItemAdapter(private val listener: Listener) : ListAdapter<ShopListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

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
    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view){
         //27.4 Заменили NoteListItemBinding на ListNameItemBinding //36.12 Удалили строку

        //функция для заполнения формы разметки note list item
        //27.2 Заменили NoteItem на ShoppingListName
        //27.3 Изменяем название переменной note на shopListNameItem
        //27.11 Пока убираем интерфейс listener: Listener
        //28.8 Снова добавяляем listener : Listener
        fun setItemData(shopListItem: ShopListItem, listener : Listener){
            val binding = ShopListItemBinding.bind(view)
            binding.apply {
                tvName.text = shopListItem.name
                tvInfo.text = shopListItem.itemInfo //37.1
                tvInfo.visibility = infoVisibility(shopListItem)
                chBox.isChecked = shopListItem.itemChecked //39.4
                setPaintFlagAndColor(binding)
                //38.2 запускаем функцию проверки через слушатель нажатий
                chBox.setOnClickListener{
                    listener.onClickItem(shopListItem.copy(itemChecked = chBox.isChecked), CHECK_BOX) //39.3
                }

                imEdit.setOnClickListener {
                    listener.onClickItem(shopListItem, EDIT)
                }
            }

        }

        //33.4
        fun setLibraryData(shopListItem: ShopListItem, listener : Listener){
            val binding = ShopLibraryListItemBinding.bind(view)
            binding.apply {
                tvName.text = shopListItem.name
                imEdit.setOnClickListener {
                    listener.onClickItem(shopListItem, EDIT_LIBRARY_ITEM)
                }
                imDelete.setOnClickListener {
                    listener.onClickItem(shopListItem, DELETE_LIBRARY_ITEM)
                }
            }

        }
        //38.1 Меняем цвет из зачеркиваем тект при нажатии на чек бокс и обратно
        private fun setPaintFlagAndColor(binding: ShopListItemBinding){
            binding.apply {
                if(chBox.isChecked){
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //зачеркиваем
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //зачеркиваем
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                } else {
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG //возврат в первонач значение
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG //возврат в первонач значение
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                }
            }

        }
        //37.2 Делаем проверку пусто ли или есть значения
        private fun infoVisibility(shopListItem: ShopListItem): Int{
            return if(shopListItem.itemInfo.isEmpty()){
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        companion object{
            //33.1 Вносим изменения
            fun createShopItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.shop_list_item, parent, false)) //27.13 Заменяем note_list_item на list_name_item
            }
            fun createLibraryItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.shop_library_list_item, parent, false))
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopListItem>(){ //27.9 Меняем NoteItem на ShoppingListName
        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {//27.10 Меняем NoteItem на ShoppingListName
            return oldItem == newItem
        }

    }
    //прописываем интерфейс для кнопки удалить
    interface Listener{
        fun onClickItem(shopListItem: ShopListItem, state : Int)

    }

    companion object{
        const val EDIT = 0
        const val CHECK_BOX = 1
        //46.4
        const val EDIT_LIBRARY_ITEM = 2
        //46.6
        const val DELETE_LIBRARY_ITEM = 3
    }
}