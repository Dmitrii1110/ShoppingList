package com.itproger.shoppinglist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.itproger.shoppinglist.activities.MainApp
import com.itproger.shoppinglist.databinding.FragmentShopListNamesBinding
import com.itproger.shoppinglist.db.MainViewModel
import com.itproger.shoppinglist.dialogs.NewListDialog
import com.itproger.shoppinglist.entities.ShoppingListName
import com.itproger.shoppinglist.utils.TimeManager


class ShopListNamesFragment : BaseFragment() {
    private lateinit var binding: FragmentShopListNamesBinding


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    //Сюда будем записывать диалог
    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        })


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

    }

    //observer следит за базой данных и передет актуальные данные на сервер
    private fun observer (){
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner, {

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }
}