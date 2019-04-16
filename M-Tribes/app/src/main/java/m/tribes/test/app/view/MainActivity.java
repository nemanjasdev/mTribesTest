package m.tribes.test.app.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import m.tribes.test.app.R;
import m.tribes.test.app.view.home.HomeFragment;
import m.tribes.test.app.view.map.GoogleMapFragment;

public class MainActivity extends AppCompatActivity {
  @BindView(R.id.navigation)
  BottomNavigationView bottomNavigationView;

  @BindView(R.id.viewpager)
  ViewPager viewPager;

  //Fragments
  HomeFragment homeFragment;
  GoogleMapFragment googleMapFragment;
  MenuItem prevMenuItem;

  @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    bottomNavigationView.setOnNavigationItemSelectedListener(
      item -> {
        switch (item.getItemId()) {
          case R.id.navigation_home:
            viewPager.setCurrentItem(0);
            break;
          case R.id.navigation_map:
            viewPager.setCurrentItem(1);
            break;
        }
        return false;
      });

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (prevMenuItem != null) {
          prevMenuItem.setChecked(false);
        } else {
          bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        Log.d("page", "" + position);
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigationView.getMenu().getItem(position);

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    setupViewPager(viewPager);
  }


  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    homeFragment = new HomeFragment();
    googleMapFragment = new GoogleMapFragment();

    adapter.addFragment(homeFragment);
    adapter.addFragment(googleMapFragment);
    viewPager.setAdapter(adapter);
  }

  private class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
      return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
      mFragmentList.add(fragment);
      FragmentManager manager = getSupportFragmentManager();
      FragmentTransaction ft = manager.beginTransaction();

      ft.addToBackStack(fragment.getTag());
      ft.commitAllowingStateLoss();
    }
  }
}
