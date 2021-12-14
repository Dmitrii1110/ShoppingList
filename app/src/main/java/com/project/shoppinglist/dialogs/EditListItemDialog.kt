package com.project.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.EditListItemDialogBinding
import com.project.shoppinglist.databinding.NewListDialogBinding
import com.project.shoppinglist.entities.ShopListItem

//*Диалоговое окно измения названия заголовка списка покупок
object EditListItemDialog {
    fun showDialog(context: Context, item: ShopListItem, listener: Listener){//29.8 Добавили в скобках name: String
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            bUpdate.setOnClickListener{
                if(edName.text.toString().isNotEmpty()){
                    val itemInfo = if(edInfo.text.toString().isEmpty()) null else edInfo.text.toString()
                    listener.onClick(item.copy(name = edName.text.toString(), itemInfo = edInfo.text.toString()))

                }
                dialog?.dismiss()
            }

            }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null) //убираем стандартный фон и оставляем нашу разметку
        dialog.show()

    }
    interface Listener{
        fun onClick(item: ShopListItem)
    }
}