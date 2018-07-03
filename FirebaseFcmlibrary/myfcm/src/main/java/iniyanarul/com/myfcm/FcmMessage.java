package iniyanarul.com.myfcm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import iniyanarul.com.myfcm.OntaskCompleted;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public  class FcmMessage extends Activity  {
    Context context;
    OntaskCompleted ontaskCompleted;



      
    public FcmMessage(Context context, OntaskCompleted response) {
        this.context = context;
        this.ontaskCompleted = response;
    }


    public  void sendNotification_toUser(String title, String message, String FirebaseId) {
        try {


            title = URLEncoder.encode(title, "UTF-8");
            message = URLEncoder.encode(message, "UTF-8");

            Log.e("Fire", "" + FirebaseId);

            RequestQueue requestQueue;
         requestQueue = Volley.newRequestQueue(this.context);
            String URL = "https://fcm.googleapis.com/fcm/send";


            JSONObject main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("body", message);
            jsonObject.put("image", "https://ibin.co/2t1lLdpfS06F.png");
            jsonObject.put("AnotherActivity", "True");
            main.put("data", jsonObject);
            main.put("priority", "high");

            main.put("to", FirebaseId);

            final String requestBody = main.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response != null) {
                        ontaskCompleted.onSuccess(response);
                    }


              //   Toast.makeText(this.context, "Notification Send Successfully+" + response, Toast.LENGTH_LONG).show();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    //Toast.makeText(context, "" + error.toString(), Toast.LENGTH_SHORT).show();
                    ontaskCompleted.onError( error.getMessage());

                }
            }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "key=AAAAFXgJ7hg:APA91bHHgDGDJnwLs-TGOYWq5K05uumpTYe10S67FNPDnryaMCfdRiR9WTY8mwjZ5NI-mM9Ll40KYiEAnes6BDUMdcbWemsa0ag5Nho1GtvOBwwHDSfl4K3zLMQgU-PGq94Z9u-WREaweZKG4IqmHmeqEBimXD67Xw");
                    params.put("Accept", "application/json");
                    params.put("project_id", "92208229912");


                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.toString().getBytes("utf-8");


                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        Log.e("Admin", "" + response);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

           requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


}
