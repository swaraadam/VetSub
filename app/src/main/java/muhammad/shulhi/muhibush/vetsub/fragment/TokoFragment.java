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
import muhammad.shulhi.muhibush.vetsub.activity.LoginActivity;
import muhammad.shulhi.muhibush.vetsub.activity.MapsActivityToko;
import muhammad.shulhi.muhibush.vetsub.activity.ProfileActivityToko;
import muhammad.shulhi.muhibush.vetsub.model.Toko;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import muhammad.shulhi.muhibush.vetsub.sharedPref.SharedPrefLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokoFragment extends Fragment {
    private SlimAdapter slimAdapter;
    private MaterialSearchView searchView;
    private RecyclerView rvToko;
    private RelativeLayout rlMap;
    private Activity activity;
    private ArrayList<Toko> listTokoOriginal = new ArrayList<>();
    private ArrayList<Toko> listTokoFilter = new ArrayList<>();
    private Button btnLogout;
    private SharedPrefLogin sharedPrefLogin;

    public TokoFragment() {
        // Required empty public constructor
    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Activity) {
            this.activity = (Activity) activity;
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
        View view = inflater.inflate(R.layout.fragment_toko, container, false);
        rvToko = (RecyclerView) view.findViewById(R.id.rv_toko);
        sharedPrefLogin = new SharedPrefLogin(getActivity());
        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefLogin.clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) view.findViewById(R.id.search_view);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean found = false;
                listTokoFilter.clear();
                ArrayList<Toko> Filter = new ArrayList<>();
                ArrayList<Toko> UnFilter = new ArrayList<>();

                for (Toko toko:listTokoOriginal){
                    if (toko.getAlamat().toLowerCase().contains(query.toLowerCase())){
                        Filter.add(toko);
                        found = true;
                    }
                    else{
                        UnFilter.add(toko);
                    }
                }
                listTokoFilter.addAll(Filter);
                listTokoFilter.addAll(UnFilter);
                displayTokoRecycler(listTokoFilter);

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

        rlMap = (RelativeLayout) view.findViewById(R.id.rl_map);
        rlMap.setOnClickListener(new View.OnClickListener() {
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
                listTokoFilter.addAll(listTokoOriginal);
                displayTokoRecycler(listTokoFilter);
            }

            @Override
            public void onFailure(Call<ArrayList<Toko>> call, Throwable t) {
                Toast.makeText(getActivity(), "Connection trouble", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTokoRecycler(List<Toko> listTokoFiltered) {
        rvToko.setNestedScrollingEnabled(false);
        rvToko.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rvToko.setFocusable(false);
        slimAdapter = SlimAdapter.create()
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
        slimAdapter.updateData(listTokoFiltered);
        slimAdapter.notifyDataSetChanged();
    }

}