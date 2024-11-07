package com.example.firstapplication;

import android.os.Bundle;
import com.example.firstapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Обратите внимание на этот импорт
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.firstapplication.databinding.ActivityMain3Binding;
public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // По умолчанию отображаем фрагмент "Список книг"
        if (savedInstanceState == null) {
            loadFragment(new BookListFragment());
        }

        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_book_list) {
                selectedFragment = new BookListFragment();
            } else if (itemId == R.id.navigation_selected_book) {
                selectedFragment = new SelectedBookFragment();
            } else if (itemId == R.id.navigation_favorites) {
                selectedFragment = new FavoritesFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    // Метод для замены фрагментов
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}