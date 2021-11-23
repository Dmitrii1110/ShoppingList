package com.itproger.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.itproger.shoppinglist.R
import com.itproger.shoppinglist.databinding.ActivityShopListBinding
import com.itproger.shoppinglist.db.MainViewModel
import com.itproger.shoppinglist.entities.ShopListNameItem

class ShopListActivity : AppCompatActivity() {
    //30.1 Создали активити шоп лист бандинг
    private lateinit var binding: ActivityShopListBinding
    //30.4 Добавиди возможность поиска нужного списка
    private var shopListNameItem: ShopListNameItem? = null

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
        return true
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