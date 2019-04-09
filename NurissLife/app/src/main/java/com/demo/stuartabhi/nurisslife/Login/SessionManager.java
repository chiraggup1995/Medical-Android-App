package com.demo.stuartabhi.nurisslife.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by stuartabhi on 6/3/2016.
 */
public class SessionManager {

    private static String TAG=SessionManager.class.getSimpleName();
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="NurissTalklogin";
    private static final String KEY_IS_LOGGEDIN="isLoggedIn";
    public SessionManager(Context context)
    {
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void setLogin(boolean IsLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN,IsLoggedIn);
        editor.commit();
        Log.d(TAG,"User login session modified!");
    }
    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGEDIN,false);
    }
}
