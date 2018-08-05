package muhammad.shulhi.muhibush.vetsub.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.List;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.activity.MapsActivityDokter;
import muhammad.shulhi.muhibush.vetsub.activity.ProfileActivityToko;
import muhammad.shulhi.muhibush.vetsub.model.Dokter;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DokterFragment extends Fragment {
    private SlimAdapter slimAdapter;
    private MaterialSearchView searchView;
    private RecyclerView rvDokter;
    private RelativeLayout rlMap;
    private Activity activity;
    private ArrayList<Dokter> listDokterOriginal;
    private ArrayList<Dokter> listDokterFilter = new ArrayList<>();

    public DokterFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Activity) {
            this.activity = activity;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dokter, container, false);
        rvDokter = view.findViewById(R.id.rv_dokter);



        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        searchView = view.findViewById(R.id.search_view);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean found = false;
                listDokterFilter.clear();
                ArrayList<Dokter> Filter = new ArrayList<>();
                ArrayList<Dokter> UnFilter = new ArrayList<>();

                for (Dokter toko:listDokterOriginal){
                    if (toko.getAlamat().toLowerCase().contains(query.toLowerCase())){
                        Filter.add(toko);
                        found = true;
                    }
                    else{
                        UnFilter.add(toko);
                    }
                }
                listDokterFilter.addAll(Filter);
                listDokterFilter.addAll(UnFilter);
                displayDokterRecycler(listDokterFilter);

                if (!found){
                    Toast.makeText(activity,"Toko dengan Alamat "+query.toString()+" tidak ditemukan",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        rlMap = view.findViewById(R.id.rl_map);
        rlMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivityDokter.class);
                startActivity(intent);
            }
        });
        getAllDokter();
        return view;
    }

    private void getAllDokter() {
        ApiServices.service_get.dokter_getall().enqueue(new Callback<ArrayList<Dokter>>() {
            @Override
            public void onResponse(Call<ArrayList<Dokter>> call, Response<ArrayList<Dokter>> response) {
                listDokterOriginal = response.body();
                listDokterFilter.addAll(listDokterOriginal);
                displayDokterRecycler(listDokterFilter);
            }

            @Override
            public void onFailure(Call<ArrayList<Dokter>> call, Throwable t) {
                Toast.makeText(getActivity(), "Connection trouble", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDokterRecycler(List<Dokter> listTokoFiltered) {
        rvDokter.setNestedScrollingEnabled(false);
        rvDokter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        rvDokter.setFocusable(false);
        slimAdapter = SlimAdapter.create()
                .register(R.layout.item_toko, new SlimInjector<Dokter>() {
                    @Override
                    public void onInject(final Dokter data, IViewInjector injector) {
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
                                        intent.putExtra(ProfileActivityToko.EXTRA_JENIS_LAYANAN,"dokter");
                                        startActivity(intent);

                                    }
                                })
                                .with(R.id.item, new IViewInjector.Action() {
                                    @Override
                                    public void action(View view) {
                                        ImageView image = view.findViewById(R.id.img_event);

                                        Glide.with(getContext())
                                                .load("http://vetsub.herokuapp.com/images/dokter/"+data.getFoto())
                                                .into(image);
                                    }
                                });
                    }
                }).attachTo(rvDokter);
        slimAdapter.updateData(listTokoFiltered);
        slimAdapter.notifyDataSetChanged();
    }

}