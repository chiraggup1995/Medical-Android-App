package com.demo.stuartabhi.nurisslife.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.stuartabhi.nurisslife.Login.AppConfig;
import com.demo.stuartabhi.nurisslife.Login.AppController;
import com.demo.stuartabhi.nurisslife.Login.MainHome;
import com.demo.stuartabhi.nurisslife.Login.SQLiteHandler;
import com.demo.stuartabhi.nurisslife.Login.SessionManager;
import com.demo.stuartabhi.nurisslife.Login.SignUp;
import com.demo.stuartabhi.nurisslife.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment  {

    private static final String TAG = "ShareFragment";
    private static final int REQUEST_SIGNUP = 0;
    private TextInputEditText inputemail,inputpass;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Button btnLogin;
    private SQLiteHandler db;
    private TextView tv1;
    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActionBar().setTitle("Nuriss Talk");
        getActionBar().setSubtitle("Report Here(M.R)");

        ScrollView sv=(ScrollView)inflater.inflate(R.layout.fragment_share,container,false);
        inputemail=(TextInputEditText)sv.findViewById(R.id.input_email);
        inputpass=(TextInputEditText)sv.findViewById(R.id.input_password);
        tv1=(TextView)sv.findViewById(R.id.link_signup);
        btnLogin=(Button)sv.findViewById(R.id.btn_login);

        //Progress Dialog
        pDialog=new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        //Sqlite database handler
        db= new SQLiteHandler(getActivity());
        //Session Manager
        session=new SessionManager(getActivity());
        //Check if user is already logged in or not
        if(session.isLoggedIn())
        {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame,new MainHome()).addToBackStack(null).commit();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=inputemail.getText().toString().trim();
                final String password=inputpass.getText().toString().trim();
                //checking email pattern
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.matches(emailPattern))
                {
                    Toast.makeText(getContext(),"valid email address",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }

//////////////////////////////////////////////////////////////////

                if(!email.isEmpty()&& !password.isEmpty() )
                {
                    checkLogin(email,password);

                }
                else
                {
                    Toast.makeText(getContext(),"Please enter the credentials!",Toast.LENGTH_LONG).show();
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent int1=new Intent(getActivity(), SignUp.class);
                startActivity(int1);
                getActivity().finish();*/
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame,new SignUp()).addToBackStack(null).commit();
               /* FragmentTransaction ft = fm.beginTransaction();
                SignUp llf = new SignUp();
                ft.replace(R.id.page_login, llf);
                ft.commit();*/
            }
        });

        return sv;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }

    private void checkLogin(final String email, final String password) {
        String tag_string_req="req_login";
        pDialog.setMessage("Logging in");
        showDialog();
        StringRequest strReq=new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response:" + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Check for a error node in jSON
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        //Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        //inserting rows in users table
                        db.addUser(name, email, uid, created_at);
                        //launch Home activity
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.content_frame,new MainHome()).commit();
                    } else {
                        //Error in login
                        String error_msg = jObj.getString("error message");
                        Toast.makeText(getContext(), error_msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "JSon error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
    if(pDialog.isShowing())
        pDialog.dismiss();
    }
}