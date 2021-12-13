package com.project.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.project.shoppinglist.databinding.DeleteDialogBinding

object DeleteDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context)) //28.3 Меняем NewListDialogBinding на DeleteDialogBinding
        builder.setView(binding.root)
        binding.apply {
            bDelete.setOnClickListener{//28.4 Поменяли кнопку bCreate на bDelete
                //28.5 Удаляем ниже функции они нам больше не нужны
                //val listName = edNewListName.text.toString()
                //if(listName.isNotEmpty()){
                listener.onClick() //28.6 В себе он больше ничего принимать не будет
                dialog?.dismiss()
            }

            //28.7 Добавляем кнопку Cancel только без слушателя нажатий
            bCancel.setOnClickListener{
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null) //убираем стандартный фон и оставляем нашу разметку
        dialog.show()

    }
    interface Listener{
        fun onClick() //28.7 В себе он больше ничего принимать не будет
    }
}