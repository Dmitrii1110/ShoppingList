package com.itproger.shoppinglist.activities

import android.app.Application
import com.itproger.shoppinglist.db.MainDataBase

class MainApp : Application () {
    val database by lazy { MainDataBase.getDataBase(this)}
}