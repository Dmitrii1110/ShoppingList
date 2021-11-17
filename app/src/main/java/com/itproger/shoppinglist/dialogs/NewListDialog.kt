package com.itproger.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.itproger.shoppinglist.databinding.NewListDialogBinding

//*Диалоговое окно измения названия заголовка списка покупок
object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String){//29.8 Добавили в скобках name: String
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edNewListName.setText(name) //29.9 Добавлено edNewListName.setText(name) для отображения текущего названия в поле редактирования
            bCreate.setOnClickListener{
                val listName = edNewListName.text.toString()
                if(listName.isNotEmpty()){
                    listener.onClick(listName)

                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null) //убираем стандартный фон и оставляем нашу разметку
        dialog.show()

    }
    interface Listener{
        fun onClick(name: String)
    }
}