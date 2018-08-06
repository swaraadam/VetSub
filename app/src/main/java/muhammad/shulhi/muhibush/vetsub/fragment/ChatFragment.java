package muhammad.shulhi.muhibush.vetsub.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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
import muhammad.shulhi.muhibush.vetsub.adapter.ChatAdapter;
import muhammad.shulhi.muhibush.vetsub.model.Message;
import muhammad.shulhi.muhibush.vetsub.other.WsConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();
    private static final String KEY_SHARED_PREF = "vetSubChat";
    private static final String KEY_SESSION_NAME = "sessionName";
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String FLAG_MESSAGE = "privateMessage";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private RecyclerView rcMessage;
    private EditText etMessage;
    private Button btnSend;

    private WebSocketClient client;

    private List<Message> listMessage = new ArrayList<>();


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        rcMessage = view.findViewById(R.id.rc_view_message);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);

        preferences = getContext().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcMessage.setLayoutManager(linearLayoutManager);
        rcMessage.setItemAnimator(new DefaultItemAnimator());
        rcMessage.setAdapter(new ChatAdapter(getContext(), listMessage));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sending message to web socket server
                sendMessageToServer(getSendMessageJSON(etMessage.getText().toString()));
                Message m = new Message("Me", etMessage.getText().toString(), true);
                appendMessage(m);

                // Clearing the input filed once message was sent
                etMessage.setText("");
            }
        });

        /**
         * Creating web socket client. This will have callback methods
         * */
        client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
                showToast("Got string message! "+ message);

                Message m = new Message("Dokter", message, false);
                appendMessage(m);
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                Log.d(TAG, String.format("Got binary message! %s", bytesToHex(bytes.array())));
                showToast("Got binary message! "+ bytesToHex(bytes.array()));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                String message = String.format(Locale.US, "Disconnected! Code: %d Reason: %s", code, reason);
                showToast(message);

                // Clear the session id from shared preferences
                // editor.putString(KEY_SESSION_ID, null);
                // editor.apply();
            }

            @Override
            public void onError(Exception ex) {
                Log.e(TAG, "Error! : " + ex);
                showToast("Error! : "+ ex);
            }
        };
        client.connect();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(client != null & client.isOpen()){
            client.close();
        }
    }

    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message) {
        if (client != null && client.isOpen()) {
            // Sending message to web socket server
            JSONObject jObj = new JSONObject();
            try {
                jObj.put("token", "5b608dbd0cf3ab1f1c0d7318");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            client.send(jObj.toString());
            client.send(message);
            Log.d("Data send", message);
        }
    }

    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMessage.add(m);
                rcMessage.getAdapter().notifyDataSetChanged();
                rcMessage.scrollToPosition(listMessage.size() - 1);
                Log.d("Message added", m.toString());

                // Playing device's notification
                playBeep();
            }
        });
    }

    /**
     * Show message on toast
     * */
    private void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Plays device's default notification sound
     * */
    public void playBeep() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert bytes to hex
     * */
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get JSON Object of message before send it to web socket server
     * */
    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("type", FLAG_MESSAGE);
            jObj.put("data", new JSONObject()
                    .put("to", "5b61b3f680347d0014710b0c")
                    .put("message", message)
            );

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}
