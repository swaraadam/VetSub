package muhammad.shulhi.muhibush.vetsub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.model.Toko;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivityToko extends AppCompatActivity {
    private ImageView ivFoto;
    private TextView tvNama,tvEmail,tvTelepon,tvAlamat;
    public static final String EXTRA_ID_PROFILE = "200";
    public static final String EXTRA_JENIS_LAYANAN = "201";
    private Toko toko;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_toko);
        ivFoto = findViewById(R.id.ib_foto);
        tvNama = findViewById(R.id.tv_nama);
        tvEmail = findViewById(R.id.tv_email);
        tvTelepon = findViewById(R.id.tv_telepon);
        tvAlamat = findViewById(R.id.tv_alamat);

        final Intent intent = getIntent();
        Log.d( "onCreate: ",intent.getStringExtra(EXTRA_ID_PROFILE));
        ApiServices.service_get.toko_getone(intent.getStringExtra(EXTRA_ID_PROFILE)).enqueue(new Callback<Toko>() {
            @Override
            public void onResponse(Call<Toko> call, Response<Toko> response) {
                toko = response.body();

                if (intent.getStringExtra(EXTRA_JENIS_LAYANAN).equals("dokter")){
                    Glide.with(ProfileActivityToko.this)
                            .load("http://vetsub.herokuapp.com/images/dokter/"+toko.getFoto())
                            .into(ivFoto);
                }
                else {
                    Glide.with(ProfileActivityToko.this)
                            .load("http://vetsub.herokuapp.com/images/petcare/"+toko.getFoto())
                            .into(ivFoto);
                }

                tvNama.setText(toko.getNama());
                tvEmail.setText(toko.getEmail());
                tvTelepon.setText(toko.getTelepon());
                tvAlamat.setText(toko.getAlamat());
            }

            @Override
            public void onFailure(Call<Toko> call, Throwable t) {

            }
        });

    }
}
