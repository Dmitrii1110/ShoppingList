package com.itproger.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import com.itproger.shoppinglist.R
import com.itproger.shoppinglist.databinding.ActivityShopListBinding
import com.itproger.shoppinglist.db.MainViewModel
import com.itproger.shoppinglist.entities.ShopListItem
import com.itproger.shoppinglist.entities.ShopListNameItem

class ShopListActivity : AppCompatActivity() {
    //30.1 Создали активити шоп лист бандинг
    private lateinit var binding: ActivityShopListBinding
    //30.4 Добавиди возможность поиска нужного списка
    private var shopListNameItem: ShopListNameItem? = null
    //32.1 Прячем кнопку сохранить в NewItem до момента пока не появится информация
    private lateinit var saveItem: MenuItem
    //35.3
    private var edItem: EditText? = null

    //30.3 Добавляем кусок кода из шоп лист нейм фрагмент
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //30.2 Инициализируем активити
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init() //36.7
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
        return true
    }

    //35.6
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_item){
            addNewShopItem() //после проверки инициализиуем функцию ниже
        }
        return super.onOptionsItemSelected(item)
    }

    //35.5 Сохраняем данные и передаем их в MainViewModel
    private fun addNewShopItem(){
        if (edItem?.text.toString().isEmpty()) return // если пусто то делаем возврат
        //если не пусто то созраняем
        val item = ShopListItem(
            null,
            edItem?.text.toString(),
            null,
            0,
            shopListNameItem?.id!!,
            0

        )
        mainViewModel.insertShopItem(item)
    }

    //32.3 Вновь показываем кнопку сохранить
    private fun expandActionView():MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()  //32.5 Перерисовка элементов меню после сохранения newItem
                return true
            }

        }
    }

    //30.5 Функция запуска передачи данных
    private fun init(){
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
        binding.tvtest.text = shopListNameItem?.name
    }

    //30.6 Создали константу
    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }
}