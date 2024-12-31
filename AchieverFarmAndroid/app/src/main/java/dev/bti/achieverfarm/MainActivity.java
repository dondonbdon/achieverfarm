package dev.bti.achieverfarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.common.Credentials;
import dev.bti.achieverfarm.fragments.CartFragment;
import dev.bti.achieverfarm.fragments.HomeFragment;
import dev.bti.achieverfarm.fragments.OrdersFragment;
import dev.bti.achieverfarm.fragments.SearchFragment;
import lombok.Getter;

public class MainActivity extends AppCompatActivity {

    @Getter
    private BottomNavigationView mainBottomNavigationView;
    private CircleImageView profile;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private OrdersFragment ordersFragment;

    @Getter
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        initListeners();
        initBottomNav();
    }

    private void initUi() {
        mainBottomNavigationView = findViewById(R.id.mainBottomNavigationView);
        profile = findViewById(R.id.profile);
        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        activeFragment = homeFragment;
        fragmentManager.beginTransaction().add(R.id.mainFragContainer, homeFragment, "home").commit();
    }

    private void initListeners() {
        profile.setOnClickListener(v -> handleLogout());
    }

    private void initBottomNav() {
        mainBottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            String tag;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                if (searchFragment == null) searchFragment = new SearchFragment();
                selectedFragment = searchFragment;
                tag = "search";
            } else if (itemId == R.id.nav_orders) {
                if (ordersFragment == null) ordersFragment = new OrdersFragment();
                selectedFragment = ordersFragment;
                tag = "orders";
            } else if (itemId == R.id.nav_cart) {
                if (cartFragment == null) cartFragment = new CartFragment();
                selectedFragment = cartFragment;
                tag = "cart";
            } else {
                selectedFragment = homeFragment;
                tag = "home";
            }

            if (selectedFragment != activeFragment) {
                switchFragment(selectedFragment, tag);
            }
            return true;
        });

        mainBottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void switchFragment(Fragment newFragment, String tag) {
        if (!newFragment.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.mainFragContainer, newFragment, tag).commit();
        }
        fragmentManager.beginTransaction().hide(activeFragment).show(newFragment).commit();
        activeFragment = newFragment;
    }

    private void handleLogout() {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getAuthHelper().invalidateToken().execute();
        Credentials.GetInstance().logout(getApplicationContext());
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
        Toast.makeText(this, "Logout Successful.", Toast.LENGTH_SHORT).show();
    }
}

