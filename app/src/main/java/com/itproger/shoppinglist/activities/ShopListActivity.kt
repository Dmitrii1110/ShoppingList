package com.itproger.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.itproger.shoppinglist.R
import com.itproger.shoppinglist.databinding.ActivityShopListBinding
import com.itproger.shoppinglist.db.MainViewModel
import com.itproger.shoppinglist.entities.ShoppingListName

class ShopListActivity : AppCompatActivity() {
    //30.1 Создали активити шоп лист бандинг
    private lateinit var binding: ActivityShopListBinding
    //30.4 Добавиди возможность поиска нужного списка
    private var shopListName: ShoppingListName? = null

    //30.3 Добавляем кусок кода из шоп лист нейм фрагмент
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //30.2 Инициализируем активити
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //30.5 Функция запуска передачи данных
    private fun init(){
        shopListName = intent.getSerializableExtra(SHOP_LIST_NAME) as ShoppingListName
    }

    //30.6 Создали константу
    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }
}