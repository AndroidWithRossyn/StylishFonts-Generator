package com.stylishfonts.fancyascii.facegenrator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.stylishfonts.fancyascii.facegenrator.adapter.ViewPagerAdapter;
import com.stylishfonts.fancyascii.facegenrator.fragment.EmojiFragment;
import com.stylishfonts.fancyascii.facegenrator.fragment.FontFragment;
import com.stylishfonts.fancyascii.facegenrator.fragment.PrettifyFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView menu_options;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] tabs;

    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();


        menu_options = findViewById(R.id.menu_options);
        menu_options.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = new String[3];
        tabs[0] = getResources().getString(R.string.fancy);
        tabs[1] = getResources().getString(R.string.prettify);
        tabs[2] = getResources().getString(R.string.emoji);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabViewUn(i));
        }

        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
           /*     assert tabs != null;
                tabs.setCustomView(null);*/
                tabs.setCustomView(getTabView(tab.getPosition()));


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                assert tabs != null;
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finishAffinity();
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.font_to_emoji) {
            startActivity(new Intent(MainActivity.this, ActivityFontToEmoji.class));
        }
        if (itemId == R.id.text_repeater) {
            startActivity(new Intent(MainActivity.this, ActivityTextRepeater.class));
        }
        if (itemId == R.id.ascii_face) {
            startActivity(new Intent(MainActivity.this, ActivityAsciiFaceText.class));
        } else if (itemId == R.id.nav_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String message = getResources().getString(R.string.share_app_message);
                shareIntent.putExtra(Intent.EXTRA_TEXT, message + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(shareIntent, "choose one"));

            } catch (Exception e) {

            }
        } else if (itemId == R.id.nav_rate_us) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
            }

        } else if (itemId == R.id.nav_feedback) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {this.getResources().getString(R.string.email_feedback)};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_CC, this.getResources().getString(R.string.email_feedback));
                intent.putExtra(Intent.EXTRA_TEXT, "Feedback: " + "\n\n" + "App Version: V " + BuildConfig.VERSION_NAME + "\n SDK Level: " + Build.VERSION.SDK_INT);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                this.startActivity(Intent.createChooser(intent, "Send mail"));
            } catch (Exception e) {

            }

        } else if (itemId == R.id.nav_privacy) {

            openCustomTab(MainActivity.this, getResources().getString(R.string.policy_url));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void openCustomTab(Context context, String str) {
        Uri uri = Uri.parse(str);
        try {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.items));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.items));
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            customTabsIntent.launchUrl(context, uri);
        } catch (Exception e) {
//            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }

    }

    ViewPagerAdapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new FontFragment(), getResources().getString(R.string.fancy));
        adapter.addFragment(new PrettifyFragment(), getResources().getString(R.string.prettify));
        adapter.addFragment(new EmojiFragment(), getResources().getString(R.string.emoji));

        viewPager.setAdapter(adapter);
    }


    private void setupTabIcons() {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[0]);
        txt.setTextColor(getResources().getColor(R.color.tab_txt_press));
        txt.setBackgroundResource(R.drawable.press_tab);

        FrameLayout.LayoutParams tabp = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 340 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tabp);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);
    }

    public View getTabView(int pos) {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[pos]);
        txt.setTextColor(getResources().getColor(R.color.tab_txt_press));
        txt.setBackgroundResource(R.drawable.press_tab);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 340 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tab);
        return v;
    }

    public View getTabViewUn(int pos) {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[pos]);
        txt.setTextColor(getResources().getColor(R.color.brand_color));
        txt.setBackgroundResource(R.drawable.unpress_tab);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 340 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tab);
        return v;
    }

}