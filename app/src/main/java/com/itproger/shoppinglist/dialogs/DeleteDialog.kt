package com.itproger.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.itproger.shoppinglist.databinding.NewListDialogBinding

object DeleteDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
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