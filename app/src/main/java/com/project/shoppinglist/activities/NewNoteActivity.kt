package com.project.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ActivityNewNoteBinding
import com.project.shoppinglist.entities.NoteItem
import com.project.shoppinglist.fragments.NoteFragment
import com.project.shoppinglist.utils.HtmlManager
import com.project.shoppinglist.utils.MyTouchListener
import com.project.shoppinglist.utils.TimeManager
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var defPref: SharedPreferences
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        actionBarSettings()
        init()
        setTextSize()
        getNote()
        onClickColorPicker()
        actionMenuCallback()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    //слушатель нажатий на выбор цвета в Колор Пикер
    private fun onClickColorPicker() = with(binding){
        imRed.setOnClickListener{
            setColorForSelectedText(R.color.picker_red)
        }
        imGreen.setOnClickListener{
            setColorForSelectedText(R.color.picker_green)
        }
        imBlue.setOnClickListener{
            setColorForSelectedText(R.color.picker_blue)
        }
        imBlack.setOnClickListener{
            setColorForSelectedText(R.color.picker_black)
        }
        imYellow.setOnClickListener{
            setColorForSelectedText(R.color.picker_yellow)
        }
        imOrange.setOnClickListener{
            setColorForSelectedText(R.color.picker_orange)
        }

    }

    private fun getNote(){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }
    }

    //передаём данные Note при повторном открытии Note файла
    private fun fillNote() = with(binding){
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nw_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //в открытом окне Ноте активити мы делаем активные кнопки в тул баре
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_save){
                setMainResult()
        } else if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.id_bold) {
            setBoldForSelectedText()
        } else if (item.itemId == R.id.id_color) {
            if (binding.colorPicker.isShown){
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //прописываем функцию запуска и проверки функции Bold
    private fun setBoldForSelectedText() = with(binding){
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if(styles.isNotEmpty()){
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    //прописываем функцию запуска и проверки функции Изменения цвета текста
    private fun setColorForSelectedText(colorId: Int) = with(binding){
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if(styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])
        edDescription.text.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setMainResult(){
        var editState = "new"
        val tempNote: NoteItem? = if (note == null){
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote() : NoteItem? = with(binding){
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )
    }

    private fun createNewNote(): NoteItem{
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
            TimeManager.getCurrentTime(),
            ""
        )
    }


    private fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    //функция где мы прописываем запуск анимации для Колор Пикер
    private fun openColorPicker(){
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    //функция где мы прописываем закрытие анимации для Колор Пикер
    private fun closeColorPicker(){
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        //прописываем ниже слушатель нажатий для закрытия анимации
        openAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        binding.colorPicker.startAnimation(openAnim)
    }

    //убираем всплывающее меню при выделении теста
    private fun actionMenuCallback(){
        val actionCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }

    //52.1 Для выбора и изменения размера текста
    private fun setTextSize() = with(binding){
        Log.d("MyLog", "size: ${pref?.getString("title_size_key", "16")}")
        edTitle.setTextSize(pref?.getString("title_size_key", "16"))
        edDescription.setTextSize(pref?.getString("content_size_key", "14"))

    }

    private fun EditText.setTextSize(size: String?){
        if(size != null) this.textSize = size.toFloat()
    }

private fun getSelectedTheme(): Int {
       return when {

           (defPref.getString("theme_key", "blue") == "blue") -> {
               R.style.Theme_ShoppingListBlue
           }
            (defPref.getString("theme_key", "red") == "red") -> {
                R.style.Theme_ShoppingListRed
            }
           (defPref.getString("theme_key", "green") == "green") -> {
               R.style.Theme_ShoppingListGreen
            }
            (defPref.getString("theme_key", "yellow") == "yellow") -> {
                R.style.Theme_ShoppingListYellow
            }
           else -> {
               R.style.Theme_ShoppingListBlue
           }
       }
    }
}