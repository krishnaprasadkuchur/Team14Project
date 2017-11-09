package group14.mc.asu.edu.brainnetteam14;

import android.app.VoiceInteractor;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krish on 11/8/2017.
 */

public class VolleyRequest {
    private static VolleyRequest volleyInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyRequest(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized VolleyRequest getInstance(Context context) {

        if (volleyInstance == null) {
            volleyInstance = new VolleyRequest(context);
        }
        return volleyInstance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public void postToServer(final double[] data){

     String  url = ApplicationConstants.URL;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("VolleyRequest","Do Work");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("data", data.toString());
                return params;
            }
        };
        volleyInstance.getRequestQueue().add(postRequest);
    }
}
