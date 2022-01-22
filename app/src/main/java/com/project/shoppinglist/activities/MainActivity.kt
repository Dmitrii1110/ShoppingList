package com.project.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.project.shoppinglist.R
import com.project.shoppinglist.databinding.ActivityMainBinding
import com.project.shoppinglist.dialogs.NewListDialog
import com.project.shoppinglist.fragments.FragmentManager
import com.project.shoppinglist.fragments.NoteFragment
import com.project.shoppinglist.fragments.ShopListNamesFragment
import com.project.shoppinglist.settings.SettingsActivity
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus

import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.project.shoppinglist.billing.BillingManager


class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private lateinit var defPref: SharedPreferences
    private var currentMenuItemId = R.id.shop_list
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null
    private var adShowCounter = 0
    private var adShowCounterMax = 3
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPref.getString("theme_key", "blue").toString()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //MobileAds.initialize(this) {}
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
        if(!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false))loadInterAd()
    }

    //Ниже 5 функций подключения и показа рекламы

    //если успешно загрузилось объявление
    private fun loadInterAd(){
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object : InterstitialAdLoadCallback(){


                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }

                //если загрузилось не успешно
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                }
        })
    }

    //фунуция показа объявления
    private fun showInterAd(adListener: AdListener){
        if(iAd != null && adShowCounter > adShowCounterMax){
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

                //как только пользователь закроет объявление его перебросит на нужную активити
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd() //загружаем следующее объявление
                    adListener.onFinish() //
                }

                //если произошла какая то ошибка
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                //запускается когда объявление было полностью показано
                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            iAd?.show(this) //запускаем объявление
        } else {
            adShowCounter++
            adListener.onFinish()
        }

    }


    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

                        }

                    })
                }
                R.id.notes -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(NoteFragment.newInstance(),this@MainActivity)
                        }

                    })
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key", "blue") != currentTheme) recreate()
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
        override fun onClick(name: String) {
            Log.d("MyLog", "name: $name")
        }

    interface AdListener{
        fun onFinish()
    }

}