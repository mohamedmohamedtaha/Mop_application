package www.lycons.com.mop_application;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RegisterAct extends AppCompatActivity {
    public EditText vemp, vpasswd, vusername;
    public TextView vmsg;

    Button vconfirm;
    SharedPreferences share;
    RequestQueue mque;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        share = PreferenceManager.getDefaultSharedPreferences(RegisterAct.this);
        calling();
        handling();

    }

    public void calling() {
        vemp = findViewById(R.id.emp);
        vusername = findViewById(R.id.empusername);
        vpasswd = findViewById(R.id.emppasswd);

        vconfirm = findViewById(R.id.btnConfirm);

        vmsg = findViewById(R.id.msg);
    }

    public void handling() {
        mque = Volley.newRequestQueue(this);


    }
}