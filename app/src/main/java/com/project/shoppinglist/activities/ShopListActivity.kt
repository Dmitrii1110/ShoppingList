package com.project.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ActivityShopListBinding
import com.project.shoppinglist.db.MainViewModel
import com.project.shoppinglist.db.ShopListItemAdapter
import com.project.shoppinglist.dialogs.EditListItemDialog
import com.project.shoppinglist.entities.LibraryItem
import com.project.shoppinglist.entities.ShopListItem
import com.project.shoppinglist.entities.ShopListNameItem
import com.project.shoppinglist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    //30.1 Создали активити шоп лист бандинг
    private lateinit var binding: ActivityShopListBinding
    //30.4 Добавиди возможность поиска нужного списка
    private var shopListNameItem: ShopListNameItem? = null
    //32.1 Прячем кнопку сохранить в NewItem до момента пока не появится информация
    private lateinit var saveItem: MenuItem
    //35.3
    private var edItem: EditText? = null
    //36.9
    private var adapter: ShopListItemAdapter? = null
    //43.1
    private lateinit var textWatcher: TextWatcher


    //30.3 Добавляем кусок кода из шоп лист нейм фрагмент
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //30.2 Инициализируем активити
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init() //30.7
        listItemObserver() //36.7
        initRcView() //36.11
    }

    //31.1 Добавляем меню из созданной разметки
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        //32.2 Пряем кнопку сохранить в NewItem
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item) //32.4 Слушатель показа кнопки
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText //35.4 Сохраняем введенный текст
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher()
        return true
    }

    //43.2 Будет возвращать уже инициализированный textWatcher
    private fun textWatcher(): TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("MyLog", "On Text Changed: $s")
                mainViewModel.getAllLibraryItems("%${s}%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    //35.6
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewShopItem(edItem?.text.toString()) //после проверки инициализиуем функцию ниже
            }
            R.id.delete_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(
                    ShareHelper.shareShopList(adapter?.currentList!!, shopListNameItem?.name!!),
                    "Share by"
                ))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //35.5 Сохраняем данные и передаем их в MainViewModel
    private fun addNewShopItem(name: String){
        if (name.isEmpty()) return // если пусто то делаем возврат
        //если не пусто то созвращаем
        val item = ShopListItem(
            null,
            name,
            "",
            false,
            shopListNameItem?.id!!,
            0

        )
        edItem?.setText("")//чтобы после сохрания элемента окно ввода снова было пустым
        mainViewModel.insertShopItem(item) //Сохраняем данные
    }

    //36.6 Показываем полученные элементы списка на нашем активити
    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this,{
            adapter?.submitList(it)
            //37.3 Делаем проверку
            binding.tvEmpty.visibility = if (it.isEmpty()){
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    //45
    private fun libraryItemObserver(){
        mainViewModel.libraryItems.observe(this, {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach {item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)

            }
            adapter?.submitList(tempShopList)
            //46.7 Проверка подсказок
            binding.tvEmpty.visibility = if (it.isEmpty()){
                View.VISIBLE
            } else {
                View.GONE
            }

        })
    }

    //36.8 Для иницаилизации RecycleView
    private fun initRcView()= with(binding){
        adapter = ShopListItemAdapter(this@ShopListActivity)
       rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter
    }

    //32.3 Вновь показываем кнопку сохранить
    private fun expandActionView():MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).removeObservers(this@ShopListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                listItemObserver()
                invalidateOptionsMenu()  //32.5 Перерисовка элементов меню после сохранения newItem
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }

        }
    }

    //30.5 Функция запуска передачи данных
    private fun init(){
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    //30.6 Создали константу
    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }


    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when(state){
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem) // Чек бокс
            ShopListItemAdapter.EDIT -> editListItem(shopListItem) //редактирование
            ShopListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(shopListItem)
            ShopListItemAdapter.ADD -> addNewShopItem(shopListItem.name) //47.3
            ShopListItemAdapter.DELETE_LIBRARY_ITEM ->{
            mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        }


    }
    private fun editListItem(item: ShopListItem){
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

        })

    }

    private fun editLibraryItem(item: ShopListItem){
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") //Для отображения изменения подсказки
            }

        })

    }

    //48.2 Создаём счетчик элементов списка
    private fun saveItemCount(){
        var checkedItemCounter = 0
        adapter?.currentList?.forEach{
            if(it.itemChecked) checkedItemCounter++

        }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedItemCounter
        )
        mainViewModel.updateListName(tempShopListNameItem!!)
    }

    //48.1  Завершение подсчета элементов
    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }
}