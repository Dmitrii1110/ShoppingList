package com.project.shoppinglist.billing

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

class BillingManager(val activity: AppCompatActivity) {
    private var bClient: BillingClient? = null

    //Функция отключения рекламы
    private fun setUpBillingClient(){
        bClient = BillingClient.newBuilder(activity)
            .setListener(getPurchaseListener()).
            enablePendingPurchases()
            .build()
    }

    //Устанавлвиваем слушатель на покупку отключения рекламы
    private fun getPurchaseListener(): PurchasesUpdatedListener{
        return PurchasesUpdatedListener {
                bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {

                }
            }
        }
    }

    //Покупка на всегда (до тех пор пока пользователь не прекратит пользоваться приложением)
    private fun nonConsumableItem(purchase: Purchase){
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
            if (!purchase.isAcknowledged){
                val acParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                bClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK){

                    }


                }
            }
        }
    }
}