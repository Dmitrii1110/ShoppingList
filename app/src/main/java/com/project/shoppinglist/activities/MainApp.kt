package com.project.shoppinglist.activities

import android.app.Application
import com.project.shoppinglist.db.MainDataBase

class MainApp : Application () {
    val database by lazy { MainDataBase.getDataBase(this)}
}