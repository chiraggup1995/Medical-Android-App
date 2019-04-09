package com.demo.stuartabhi.nurisslife.Login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.stuartabhi.nurisslife.Fragment.Homefragment;
import com.demo.stuartabhi.nurisslife.Fragment.ShareFragment;
import com.demo.stuartabhi.nurisslife.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {
    private static final String TAG= SignUp.class.getSimpleName();
    private Button btnRegister;
    private TextView btnLinkTologin;
    private TextInputEditText inputFullName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    public SignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActionBar().setTitle("Sign Up");
        getActionBar().setSubtitle("Report Here(M.R)");
        ScrollView sv1=(ScrollView) inflater.inflate(R.layout.fragment_sign_up,container,false);
        inputFullName=(TextInputEditText)sv1.findViewById(R.id.nameregister);
        inputEmail=(TextInputEditText)sv1.findViewById(R.id.emailregister);
        inputPassword=(TextInputEditText)sv1.findViewById(R.id.passwordregister);
        btnRegister=(Button)sv1.findViewById(R.id.btn_register);
        btnLinkTologin=(TextView)sv1.findViewById(R.id.btnLinktoLogin);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        session=new SessionManager(getActivity());
        db=new SQLiteHandler(getActivity());
        if(session.isLoggedIn())
        {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame,new MainHome()).addToBackStack(null).commit();
        }
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=inputFullName.getText().toString().trim();
                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString().trim();
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
                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty())
                {
                    registerUser(name,email,password);
                }
                else
                {
                    Toast.makeText(getContext(),"Please enter your details!",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLinkTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame,new ShareFragment()).addToBackStack(null).commit();
            }
        });
        return sv1;
    }
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String name, final String email, final String password) {
        //TAG used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register response:" + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);
                        Toast.makeText(getContext(), "User Successfully registered", Toast.LENGTH_LONG).show();
                        //Lauching Login Page
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.content_frame,new ShareFragment()).addToBackStack(null).commit();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if(!pDialog.isShowing())
        pDialog.show();
    }

    private void hideDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }
}
