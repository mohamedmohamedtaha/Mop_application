package www.lycons.com.mop_application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SecondlyAct extends AppCompatActivity {
    ListView vlstView;
    String empname;
    String pass;
    RequestQueue mque;
    ProgressDialog progress;
    //public ArrayList mylist = new ArrayList();
    public String[] varList;
    public int[] varImage ;
    CustLstClass custLstClassAdapter;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_secondly);
        //callCustomlstView();
        vlstView = findViewById(R.id.lstView);
         custLstClassAdapter = new CustLstClass(this, new ArrayList<GetData>());
        vlstView.setAdapter(custLstClassAdapter);


        String url = "http://212.118.101.227/api/ora";
        new HttpGetRequest().execute(url);

    }


    //********************************************************************************************************
//********************************************************************************************************
    public class HttpGetRequest extends AsyncTask<String, Void, List<GetData>> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected List<GetData> doInBackground(String... params) {
            ArrayList<GetData> getDataList = new ArrayList<>();
            String stringUrl = params[0];
            String result = null;
            String inputLine;
            try {
                //  String url = "http://212.118.101.227/api/ora";
                String url = stringUrl;

                URL myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod(REQUEST_METHOD);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    reader.close();
                    streamReader.close();
                    result = stringBuilder.toString();


                }
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    name = object.optString("ename");

                    int image = R.drawable.logo;
                    GetData getData = new GetData(image, name);
                    getDataList.add(getData);


                }
            } catch (IOException e) {
                e.printStackTrace();
                result = null;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getDataList;
        }
            protected void onPostExecute(List<GetData> result) {
          //  super.onPostExecute(result);

                custLstClassAdapter.clear();

            if (result != null && !result.isEmpty()) {
                custLstClassAdapter.addAll(result);
                Toast.makeText(SecondlyAct.this ,name,Toast.LENGTH_LONG ).show();

/*
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        name = object.optString("ename");
                        GetData getData = new GetData(0, name);
                        getDataList.add(getData);

                      //  varList [i] = name;
                        Toast.makeText(SecondlyAct.this ,name,Toast.LENGTH_LONG ).show();
//                        varImage[i]= R.drawable.web_hi_res_512;

                    }*/
                    /****
                    ArrayAdapter adapter =  new ArrayAdapter(SecondlyAct.this, android.R.layout.simple_list_item_1, varList);
                    vlstView.setAdapter(adapter);

                    vlstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Toast.makeText(SecondlyAct.this, varList.get(position), Toast.LENGTH_LONG).show();
                        }
                    });
               ***/
            //    } catch (JSONException e) {
              //      e.printStackTrace();
                //    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                //}
            }
        }
    }

    public void callCustomlstView() {
        vlstView = findViewById(R.id.lstView);
        vlstView.setAdapter(new CustLstClass(this, new ArrayList<GetData>()));


        vlstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SecondlyAct.this, " Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class CustLstClass extends ArrayAdapter<GetData> {
        // i add this parameter
        Context con;
        String txt;
        int    img;

        //  and I selected constractor to genrate
        public CustLstClass(Context con, List<GetData> getData ) {
            super(con, 0, getData);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myview = convertView;
            if (myview == null){
                myview = LayoutInflater.from(getContext()).inflate(R.layout.lstitem, parent,false);

            }

            GetData currentGetData = getItem(position);

            ImageView iv = myview.findViewById(R.id.imgView);
            TextView tv = myview.findViewById(R.id.textView);


            iv.setImageResource(currentGetData.getImage());
            tv.setText(currentGetData.getName());

            return myview;
        }
    }

}
