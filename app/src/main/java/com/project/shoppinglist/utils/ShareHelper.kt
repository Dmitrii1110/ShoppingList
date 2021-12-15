package com.project.shoppinglist.utils

import android.content.Intent
import com.project.shoppinglist.entities.ShopListItem
import java.lang.StringBuilder

object ShareHelper {

    //42.1 Для возможности поделиться списком через месенджеры и тд
    fun shareShopList(shopList: List<ShopListItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, listName))
        }
        return intent
    }

    //42.2 Для проверки нашего списка
    private fun makeShareText(shopList: List<ShopListItem>, listName: String): String{
        val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        var counter = 0
        shopList.forEach {
            sBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            sBuilder.append("\n")

        }
        return sBuilder.toString()
    }
}