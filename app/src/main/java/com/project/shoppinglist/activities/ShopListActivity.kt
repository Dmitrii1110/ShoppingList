package com.project.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ActivityShopListBinding
import com.project.shoppinglist.db.MainViewModel
import com.project.shoppinglist.db.ShopListItemAdapter
import com.project.shoppinglist.entities.ShopListItem
import com.project.shoppinglist.entities.ShopListNameItem

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
    }

    //30.6 Создали константу
    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }


    override fun onClickItem(shopListItem: ShopListItem) {
        mainViewModel.updateListItem(shopListItem)

    }
}