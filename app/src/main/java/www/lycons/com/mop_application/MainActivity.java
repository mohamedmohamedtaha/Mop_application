
//********************************************************************************************************
// created By Ehab Hosni//
//*****************************************************************
//10-02-2019//
//********************************************************************************************************


package www.lycons.com.mop_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public EditText edusername, edpasswd;
    public TextView vresult;
    Button vbtnsign, register;
    SharedPreferences share;
    RequestQueue mque;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        share = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (share.getString("save", "").equals("done!")) {
            startActivity(new Intent(MainActivity.this, SecondlyAct.class));
        } else {
            setContentView(R.layout.activity_main);
            if (share.getInt("shortcut", 0) != 5) {
                createShortCut();
            }

            calling();
            handling();
        }
    }

    public void createShortCut() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.setAction(Intent.ACTION_MAIN);
        Intent shortcut = new Intent();
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Mop system");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, R.drawable.ic_launcher_background);
        // shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, R.drawable.web_hi_res_512);
        shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        sendBroadcast(shortcut);
        share.edit().putInt("shortcut", 5).commit();
    }

    public void calling() {
        edusername = findViewById(R.id.edname);
        edpasswd   = findViewById(R.id.edpasswd);
        vbtnsign   = findViewById(R.id.btnsign);
        register   = findViewById(R.id.btnregister);
        vresult    = findViewById(R.id.result);
    }

    public void handling() {
        mque = Volley.newRequestQueue(this);

            vbtnsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edusername.getText().toString().isEmpty()) {
                        edusername.setError("Insert Name correcly");
                    }else
                    if (edpasswd.getText().toString().isEmpty()) {
                        edpasswd.setError("Insert Passwrd correcly");
                    } else {
                        String url = "http://212.118.101.227/api/usrdata" + edusername.getText().toString();
                        progress = ProgressDialog.show(MainActivity.this, "Please Wait", "Loading");
                        new HttpGetRequest().execute(url);
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, RegisterAct.class));

                }
            });


        }

    //********************************************************************************************************
//********************************************************************************************************
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                String url = "http://212.118.101.227/api/usrdata/'" + edusername.getText().toString() + "'";
                URL myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ArrayList mylist = new ArrayList();
            if (result != null) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String pass = object.getString("passwd");
                        String empid = object.getString("emp_id");
                        String empname = object.getString("emp_name_a");
                        progress.dismiss();
                        mylist.add(pass);
                        mylist.add(empid);
                        mylist.add(empname);


                        if (edpasswd.getText().toString().equals(mylist.get(0).toString())) {
                            Toast.makeText(getApplicationContext(), "مرحبا بالسيد / " + mylist.get(2).toString(), Toast.LENGTH_LONG).show();
                            vresult.setText(null);
                            share.edit().putString("data", edusername.getText().toString()).commit();
                            share.edit().putString("save", "done").commit();
                            share.edit().putString("empNo", mylist.get(1).toString()).commit();

                            startActivity(new Intent(MainActivity.this, SecondlyAct.class));

                        } else {
                            vresult.setText("يوجد خطأ فى اسم المستخدم/ كلمة المرور");
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    vresult.setText(null);
                }

            }
        }
    }
 }
