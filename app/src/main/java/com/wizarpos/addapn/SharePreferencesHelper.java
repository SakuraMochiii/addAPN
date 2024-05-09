package com.wizarpos.addapn;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesHelper {


    public static final String SP_NAME = "AddAPN";

    private static SharePreferencesHelper mInstance;

    private SharedPreferences sp;

    public static SharePreferencesHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharePreferencesHelper(context);
        }
        return mInstance;
    }

    private SharePreferencesHelper(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

}
