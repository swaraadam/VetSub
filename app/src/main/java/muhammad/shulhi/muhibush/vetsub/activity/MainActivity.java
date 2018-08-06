package muhammad.shulhi.muhibush.vetsub.activity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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
import muhammad.shulhi.muhibush.vetsub.fragment.ChatFragment;
import muhammad.shulhi.muhibush.vetsub.fragment.DokterFragment;
import muhammad.shulhi.muhibush.vetsub.fragment.TokoFragment;
import muhammad.shulhi.muhibush.vetsub.model.Message;
import muhammad.shulhi.muhibush.vetsub.other.WsConfig;
import muhammad.shulhi.muhibush.vetsub.sharedPref.SharedPrefLogin;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String FLAG_MESSAGE = "privateMessage";

    private SharedPrefLogin sharedPrefLogin;
    private int prev;

    private TextView mTextMessage;

    private WebSocketClient client;
    private List<Message> listMessage = new ArrayList<>();

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

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,new TokoFragment()).commit();

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
    public void sendMessageToServer(String message) {
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
    public void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMessage.add(m);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Plays device's default notification sound
     * */
    public void playBeep() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MainActivity.this, notification);
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