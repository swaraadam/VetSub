package muhammad.shulhi.muhibush.vetsub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import muhammad.shulhi.muhibush.vetsub.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkLogin = (TextView) findViewById(R.id.link_login);
        linkLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.link_login:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
