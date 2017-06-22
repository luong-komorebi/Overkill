package luongvo.com.everythingtraffic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import luongvo.com.everythingtraffic.BusData.BusDataAdapter;
import luongvo.com.everythingtraffic.BusData.BusDataDetail;
import luongvo.com.everythingtraffic.BusData.routeInfo;

public class BusInfoActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private ArrayList<routeInfo> routeInfoList;
    private ListView listView;
    BusDataAdapter busDataAdapter;
    private EditText typeToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);
        initComponent();


        loadData();



    }

    private void loadData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(BusInfoActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading data...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Type listType = new TypeToken<ArrayList<routeInfo>>(){}.getType();
                routeInfoList = new GsonBuilder().create().fromJson(readJsonFromAssets(), listType);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                busDataAdapter = new BusDataAdapter(BusInfoActivity.this, R.layout.bus_item, routeInfoList);
                listView.setAdapter(busDataAdapter);
                progressDialog.dismiss();
            }
        }.execute();


    }

    private void initComponent() {
        listView = (ListView) findViewById(R.id.listOfBus);
        typeToSearch = (EditText) findViewById(R.id.typeToSearch);


        typeToSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BusInfoActivity.this.busDataAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeInfo item = busDataAdapter.getItem(position);
                Intent intent = new Intent(BusInfoActivity.this, BusDataDetail.class);
                intent.putExtra("routeInfoItem", item);
                startActivity(intent);
            }
        });
    }


    public String readJsonFromAssets() {
        String json;
        try {
            InputStream inputStream = getAssets().open("routeinfo.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
