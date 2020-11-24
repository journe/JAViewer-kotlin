package io.github.javiewer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.javiewer.JAViewer;
import io.github.javiewer.R;
import io.github.javiewer.adapter.ViewPagerAdapter;
import io.github.javiewer.fragment.DownloadFragment;

public class DownloadActivity extends SecureActivity {

    @BindView(R.id.download_toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.download_tabs)
    public TabLayout mTabLayout;

    @BindView(R.id.download_view_pager)
    public ViewPager mViewPager;

    public String keyword;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        this.keyword = this.getIntent().getExtras().getString("keyword");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(this.keyword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment fragment;

        fragment = new DownloadFragment();
        bundle = (Bundle) bundle.clone();
        bundle.putString("provider", "btso");
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "BTSO");

        fragment = new DownloadFragment();
        bundle = (Bundle) bundle.clone();
        bundle.putString("provider", "torrentkitty");
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "Torrent Kitty");

        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        long downloadCounter = JAViewer.CONFIGURATIONS.getDownloadCounter();
        if (downloadCounter == -1) {
            return;
        }
        downloadCounter++;
        JAViewer.CONFIGURATIONS.setDownloadCounter(downloadCounter);
        JAViewer.CONFIGURATIONS.save();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
