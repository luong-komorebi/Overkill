package luongvo.com.everythingtraffic;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import luongvo.com.everythingtraffic.BusData.routeInfo;

public class BusInfoActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private TextView txtPostList;
    private ArrayList<routeInfo> routeInfoList;
    private StringBuffer postList;
    private ListView listView;
    BusDataAdapter busDataAdapter;
    private EditText typeToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);
        initComponent();
       // txtPostList=(TextView)findViewById(R.id.txtPostList);

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
//                postList = new StringBuffer();
//                for (routeInfo routeInfo : routeInfoList) {
//                    postList.append("\n-----------" +
//                            "\nXe số : " +routeInfo.getRouteNo() +
//                            "\nTuyến :" + routeInfo.getRouteName() +
//                            "\nLoại :" + routeInfo.getType() +
//                            "\nTổng cự ly hành trình :" + routeInfo.getDistance() +
//                            "\nĐơn vị chủ quản :" + routeInfo.getOrgs() +
//                            "\nTổng thời gian đi :" + routeInfo.getTimeOfTrip() +
//                            "\nKhoảng đợi giữa 2 tuyến liên tiếp :" + routeInfo.getHeadway() +
//                            "\nThời gian hoạt động :" + routeInfo.getOperationTime() +
//                            "\nSố chỗ ngồi :" + routeInfo.getNumOfSeats() +
//                            "\nXuất Phát tại :" + routeInfo.getOutBoundName() +
//                            "\nKết Thúc tại :" + routeInfo.getInBoundName() +
//                            "\nLộ Trình lượt đi :" + routeInfo.getOutBoundDescription() +
//                            "\nLộ Trình lượt về :" + routeInfo.getInBoundDescription() +
//                            "\nTổng số chuyến :" + routeInfo.getTotalTrip() +
//                            "\nGiá vé thường :" + routeInfo.getNormalTicket() +
//                            "\nGiá vé sinh viên :" + routeInfo.getStudentTicket() +
//                            "\nGiá vé tháng :" + routeInfo.getMonthlyTicket()
//                    );
//                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                txtPostList.setText(postList);
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
                routeInfo item = routeInfoList.get(position);
                // work to be done here
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
