package muhammad.shulhi.muhibush.vetsub.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.activity.MainActivity;
import muhammad.shulhi.muhibush.vetsub.adapter.ChatAdapter;
import muhammad.shulhi.muhibush.vetsub.model.Message;
import muhammad.shulhi.muhibush.vetsub.other.WsConfig;
import muhammad.shulhi.muhibush.vetsub.sharedPref.SharedPrefLogin;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();

    private SharedPrefLogin sharedPrefLogin;

    private RecyclerView rcMessage;
    private EditText etMessage;
    private Button btnSend;

    private Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getListMessage();
        }
    };


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sharedPrefLogin = new SharedPrefLogin(getContext());

        rcMessage = view.findViewById(R.id.rc_view_message);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcMessage.setLayoutManager(linearLayoutManager);
        rcMessage.setItemAnimator(new DefaultItemAnimator());
        rcMessage.setAdapter(new ChatAdapter(getContext(), ((MainActivity) getActivity()).getListMessage()));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sending message to web socket server
                ((MainActivity) getActivity()).sendMessageToServer(((MainActivity) getActivity()).getSendMessageJSON(etMessage.getText().toString()));
                Message m = new Message(sharedPrefLogin.getName(), etMessage.getText().toString(), true);
                ((MainActivity) getActivity()).appendMessage(m);

                // Clearing the input filed once message was sent
                etMessage.setText("");
            }
        });

        updateListMessage();

        return view;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void getListMessage() {
        rcMessage.getAdapter().notifyDataSetChanged();
        rcMessage.scrollToPosition(((MainActivity) getActivity()).getListMessage().size() - 1);
        handler.post(runnable);
    }

    private void updateListMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getListMessage();
            }
        });
    }
}
