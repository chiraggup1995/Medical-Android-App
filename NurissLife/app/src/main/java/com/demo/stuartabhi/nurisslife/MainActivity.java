package com.demo.stuartabhi.nurisslife;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.demo.stuartabhi.nurisslife.Fragment.AboutUsFragment;
import com.demo.stuartabhi.nurisslife.Fragment.Carrers;
import com.demo.stuartabhi.nurisslife.Fragment.ContactFragment;
import com.demo.stuartabhi.nurisslife.Fragment.ContactUs;
import com.demo.stuartabhi.nurisslife.Fragment.FAQ;
import com.demo.stuartabhi.nurisslife.Fragment.Homefragment;
import com.demo.stuartabhi.nurisslife.Fragment.ProductsFragment;
import com.demo.stuartabhi.nurisslife.Fragment.ShareFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CharSequence mtitle,mtitle1;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private Fragment fragment=null;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mtitle="Nuriss Life Care";
        mtitle1="Start Here";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendemail();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(null==savedInstanceState) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Homefragment()).commit();
            getSupportActionBar().setTitle(mtitle);
            getSupportActionBar().setSubtitle(mtitle1);

        }

    }

    private void sendemail() {
        Log.i("Send Email", "");
        String[] TO = {"nurisslife4all@gmail.com"};
        String[] CC = {""};
        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.setData(Uri.parse("mailto:"));
        emailintent.setType("text/plain");
        emailintent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailintent.putExtra(Intent.EXTRA_CC, CC);
        emailintent.putExtra(Intent.EXTRA_SUBJECT, "Your Subject:");
        emailintent.putExtra(Intent.EXTRA_TEXT, "Email message");
        try {
            startActivity(Intent.createChooser(emailintent, "Send Email..."));
            finish();
            Log.i("Finished sending mail", "");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
                else{
                    super.onBackPressed();
                }
            }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* public void restoreActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mtitle);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment=new Homefragment();

        } else if (id == R.id.nav_aboutus) {
            fragment=new AboutUsFragment();

        } else if (id == R.id.nav_products) {
            fragment=new ProductsFragment();

        } else if (id == R.id.nav_career) {
            fragment = new Carrers();

        } else if (id == R.id.nav_faq) {
            fragment=new FAQ();

        } else if (id == R.id.nav_share) {
            fragment=new ShareFragment();

        } else if (id == R.id.nav_contact) {
            fragment=new ContactFragment();
        }
        else if (id == R.id.nav_contact1) {
            //mtitle = "Contact";
            //mtitle1 = "Keep In Touch";
            fragment = new ContactUs();
            //getSupportActionBar().setTitle(mtitle);
            //getSupportActionBar().setSubtitle(mtitle1);
        }
        if(fragment!= null)
        {
            fragmentManager=getSupportFragmentManager();
           fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack("null").commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}