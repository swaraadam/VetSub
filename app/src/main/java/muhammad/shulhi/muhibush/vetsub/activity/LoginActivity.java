package muhammad.shulhi.muhibush.vetsub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import muhammad.shulhi.muhibush.vetsub.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
        }
    }
}
