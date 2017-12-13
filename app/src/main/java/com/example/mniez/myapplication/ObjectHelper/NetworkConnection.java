package com.example.mniez.myapplication.ObjectHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by mniez on 13.12.2017.
 */

public class NetworkConnection {

    Context mKontekst;

    public NetworkConnection(Context mKontekst) {
        this.mKontekst = mKontekst;
    }

    public boolean isNetworkConnection() {

        boolean isConnection = false;
        ConnectivityManager manager = (ConnectivityManager) mKontekst.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            isConnection = true;
        }
        else {
            isConnection = false;
        }
        return isConnection;
    }
}
