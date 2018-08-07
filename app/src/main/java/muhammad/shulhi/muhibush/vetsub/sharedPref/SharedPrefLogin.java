package muhammad.shulhi.muhibush.vetsub.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefLogin {
    private final String PREF_NAME = "Pref_Login";
    private final String KEY_ID = "key_id";
    private final String KEY_SESSION_NAME = "sessionName";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefLogin(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setId(String id, String name){
        editor.putString(KEY_SESSION_NAME,name);
        editor.putString(KEY_ID,id);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_SESSION_NAME,null);
    }

    public String getID(){
        return sharedPreferences.getString(KEY_ID,null);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
