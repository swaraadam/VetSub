package muhammad.shulhi.muhibush.vetsub.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.List;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.activity.MapsActivityToko;
import muhammad.shulhi.muhibush.vetsub.activity.ProfileActivityToko;
import muhammad.shulhi.muhibush.vetsub.model.Toko;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokoFragment extends Fragment {
    private SlimAdapter saEvent;
    private RecyclerView rvToko;
    private Button btMap;
    private ArrayList<Toko> listTokoOriginal;


    public TokoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toko, container, false);
        rvToko = (RecyclerView) view.findViewById(R.id.rv_toko);
        btMap = (Button) view.findViewById(R.id.bt_map);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivityToko.class);
                startActivity(intent);
            }
        });
        getAllToko();
        return view;
    }

    private void getAllToko() {
        ApiServices.service_get.petcare_getall().enqueue(new Callback<ArrayList<Toko>>() {
            @Override
            public void onResponse(Call<ArrayList<Toko>> call, Response<ArrayList<Toko>> response) {
                listTokoOriginal = response.body();
                displayTokoRecycler(listTokoOriginal);
            }

            @Override
            public void onFailure(Call<ArrayList<Toko>> call, Throwable t) {
                Toast.makeText(getContext(), "Connection trouble", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTokoRecycler(List<Toko> listTokoFiltered) {
        rvToko.setNestedScrollingEnabled(false);
        rvToko.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rvToko.setFocusable(false);
        saEvent = SlimAdapter.create()
                .register(R.layout.item_toko, new SlimInjector<Toko>() {
                    @Override
                    public void onInject(final Toko data, IViewInjector injector) {
                        injector.text(R.id.tv_title,data.getNama())
                                .text(R.id.tv_location,data.getAlamat())
                                .text(R.id.tv_buka,data.getJam().getBuka())
                                .text(R.id.tv_tutup,data.getJam().getTutup())
                                .clicked(R.id.ll_view, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("Clicked","ok");
                                        Intent intent = new Intent(getContext(), ProfileActivityToko.class);
                                        intent.putExtra(ProfileActivityToko.EXTRA_ID_PROFILE,data.get_id());
                                        intent.putExtra(ProfileActivityToko.EXTRA_JENIS_LAYANAN,"toko");
                                        startActivity(intent);

                                    }
                                })
                                .with(R.id.item, new IViewInjector.Action() {
                                    @Override
                                    public void action(View view) {
                                        ImageView image = view.findViewById(R.id.img_event);

                                        Glide.with(getContext())
                                                .load("http://vetsub.herokuapp.com/images/petcare/"+data.getFoto())
                                                .into(image);
                                    }
                                });
                    }
                }).attachTo(rvToko);
        saEvent.updateData(listTokoFiltered);
        saEvent.notifyDataSetChanged();
    }

}
