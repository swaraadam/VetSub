package muhammad.shulhi.muhibush.vetsub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.model.User;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import muhammad.shulhi.muhibush.vetsub.sharedPref.SharedPrefLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private SharedPrefLogin sharedPrefLogin;
    private ProgressBar progressBar;
    private TextView linkRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        linkRegister = (TextView) findViewById(R.id.link_register);
        linkRegister.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        sharedPrefLogin = new SharedPrefLogin(LoginActivity.this);

        checkLogin();

    }

    private void checkLogin() {
        if (sharedPrefLogin.getID()!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                progressBar.setVisibility(View.VISIBLE);
                ApiServices.service_post.login(etEmail.getText().toString(),etPassword.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        sharedPrefLogin.setId(response.body().get_id());
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Akun belum terdaftar !", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.link_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
