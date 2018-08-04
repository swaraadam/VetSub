package muhammad.shulhi.muhibush.vetsub.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.fragment.ChatFragment;
import muhammad.shulhi.muhibush.vetsub.fragment.DokterFragment;
import muhammad.shulhi.muhibush.vetsub.fragment.TokoFragment;
import muhammad.shulhi.muhibush.vetsub.sharedPref.SharedPrefLogin;

public class MainActivity extends AppCompatActivity {
    private SharedPrefLogin sharedPrefLogin;
    private int prev;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            if (prev==item.getItemId()){
                return true;
            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new TokoFragment();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new DokterFragment();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new ChatFragment();
                    break;
            }
            prev = item.getItemId();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefLogin = new SharedPrefLogin(MainActivity.this);
        Log.d("onCreate: ", sharedPrefLogin.getID());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,new TokoFragment()).commit();
    }

}
