package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class BillingAux {
    private  BillingClient billingClient;
    private Context context;
    private Activity activity;
    public BillingAux(Context context, Activity activity){
        this.context=context;
        this.activity=activity;
        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, list) -> Log.d("testeo-billing","update");
        billingClient = BillingClient.newBuilder(context).setListener(purchasesUpdatedListener).enablePendingPurchases().build();
    }
    public void startConnection(BillingClientStateListener billingClientStateListener){
        billingClient.startConnection(billingClientStateListener);
    }

    /**
     * Preparar flow para suscripción de 6 meses
     * @param skuDetailsResponseListener listener
     */
    public void prepareFlowSub1(SkuDetailsResponseListener skuDetailsResponseListener){
        List<String> skuList = new ArrayList<>();
        skuList.add("6_months_sub");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),skuDetailsResponseListener);
    }

    /**
     * Preparar flow para suscripción de 12 meses
     * @param skuDetailsResponseListener listener
     */
    public void prepareFlowSub2(SkuDetailsResponseListener skuDetailsResponseListener){
        List<String> skuList = new ArrayList<>();
        skuList.add("12_months_sub");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),skuDetailsResponseListener);
    }

    /**
     * Comenzar flow de compra
     * @param skuDetails sku de suscripción a comprar
     * @return Billing Result
     */
    public BillingResult startBuyFlow(SkuDetails skuDetails){
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        return billingClient.launchBillingFlow(activity,flowParams);
    }

}
