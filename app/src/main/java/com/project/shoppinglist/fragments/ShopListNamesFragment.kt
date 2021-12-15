package com.project.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.shoppinglist.activities.MainApp
import com.project.shoppinglist.activities.ShopListActivity
import com.project.shoppinglist.databinding.FragmentShopListNamesBinding
import com.project.shoppinglist.db.MainViewModel
import com.project.shoppinglist.db.ShopListNameAdapter
import com.project.shoppinglist.dialogs.DeleteDialog
import com.project.shoppinglist.dialogs.NewListDialog
import com.project.shoppinglist.entities.ShopListNameItem
import com.project.shoppinglist.utils.TimeManager

//28.11 Добавляем в класс ShopListNameAdapter.Listener //28.12 Добавялем методы
class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter //27.14 подготавливаем переменную, чтобы инициализировать позже ShopListAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    //Сюда будем записывать диалог
    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){
                val shopListName = ShopListNameItem(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "") //29.10 Добавлено "" - пустое поле так как идёт создание нового листа списка покупок


    }

    //тут адаптер посредник сам следит за циклом активити
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRCView()
        observer()
    }

    private  fun initRCView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)//27.15 Передали контекст
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)//27.16 Инициализируем наш адаптер //28.14 Передаём контекст this@ShopListNamesFragment
        rcView.adapter = adapter//27.17 Поключаем наш адаптер к rcView
    }

    //observer следит за базой данных и передет актуальные данные на сервер
    private fun observer (){
        mainViewModel.allShopListNamesItem.observe(viewLifecycleOwner, {
            //27.18 Берём наш адаптер, который будет обновлять наш submitList
            adapter.submitList(it)

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    //*28.13 Прописываем нажатие на кнопку Удалить Список
    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopList(id, true)
            }

        })

    }

    //29.6 Вставлена копия куска кода onClickNew
    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){

                mainViewModel.updateListName(shopListNameItem.copy(name = name)) //29.7 Изменили на updateListName, вставляем в скобках .copy(name = name)
            }
        }, shopListNameItem.name) //29.11 Добавлено shopListName.name - мы тем самым передаём текущее название в диалоговом окне редактирования заголовка
    }

    //29.5 Изменили у существующей функции название переменной и название класса
    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        //30.7 Создаём интент
    val i = Intent(activity, ShopListActivity::class.java).apply {
        putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
    }
        startActivity(i) //30.8

    }
}