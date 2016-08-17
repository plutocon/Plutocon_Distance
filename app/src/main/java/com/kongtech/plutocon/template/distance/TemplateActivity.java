package com.kongtech.plutocon.template.distance;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

public class TemplateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setItemMenu(navigationView.getMenu().findItem(R.id.group_sub));
        this.replaceFragment(0);
    }

    public void replaceFragment(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, TemplateFragment.newInstance(this));
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_sdk) {
            Uri uri = Uri.parse("https://github.com/plutocon/Plutocon_SDK/");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        } else if (id == R.id.nav_datasheet) {
            Uri uri = Uri.parse("https://github.com/plutocon/Plutocon_SDK/blob/master/Plutocon_Datasheet.pdf");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        } else if (id == R.id.nav_contact) {
            Uri uri = Uri.parse("https://www.kong-tech.com/contact");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setItemMenu(MenuItem item) {
        SpannableString styledMenuTitle = new SpannableString(item.getTitle());
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        styledMenuTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#4cffffff")), 0, item.getTitle().length(), 0);
        styledMenuTitle.setSpan(new AbsoluteSizeSpan((int) (12 * scaledDensity)), 0, item.getTitle().length(), 0);
        item.setTitle(styledMenuTitle);
    }

}
