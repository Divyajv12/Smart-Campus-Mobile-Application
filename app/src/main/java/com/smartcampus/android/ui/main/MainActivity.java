package com.smartcampus.android.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.smartcampus.android.R;
import com.smartcampus.android.databinding.ActivityMainBinding;
import com.smartcampus.android.ui.attendance.AttendanceFragment;
import com.smartcampus.android.ui.dashboard.DashboardFragment;
import com.smartcampus.android.ui.events.EventsFragment;
import com.smartcampus.android.ui.profile.ProfileFragment;
import com.smartcampus.android.ui.timetable.TimetableFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Fragment dashboardFragment = new DashboardFragment();
    private final Fragment attendanceFragment = new AttendanceFragment();
    private final Fragment timetableFragment = new TimetableFragment();
    private final Fragment eventsFragment = new EventsFragment();
    private final Fragment profileFragment = new ProfileFragment();

    private Fragment activeFragment = dashboardFragment;
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFragments();
        setupBottomNavigation();
    }

    private void initFragments() {
        // Add all fragments and hide non-active ones to preserve their state and avoid recreating views
        fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, "5").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, eventsFragment, "4").hide(eventsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, timetableFragment, "3").hide(timetableFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, attendanceFragment, "2").hide(attendanceFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, dashboardFragment, "1").commit();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_dashboard) {
                switchFragment(dashboardFragment);
                return true;
            } else if (itemId == R.id.navigation_attendance) {
                switchFragment(attendanceFragment);
                return true;
            } else if (itemId == R.id.navigation_timetable) {
                switchFragment(timetableFragment);
                return true;
            } else if (itemId == R.id.navigation_events) {
                switchFragment(eventsFragment);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                switchFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        if (activeFragment != targetFragment) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.hide(activeFragment);
            transaction.show(targetFragment);
            transaction.commit();
            activeFragment = targetFragment;
        }
    }

    public void navigateToTab(int menuItemId) {
        binding.bottomNavigation.setSelectedItemId(menuItemId);
    }
}

