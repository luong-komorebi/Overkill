package luongvo.com.everythingtraffic.BusData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import luongvo.com.everythingtraffic.R;

/**
 * Created by luongvo on 22/06/2017.
 */

public class BusDataDetail extends Activity {
    routeInfo routeInfoItem;
    private StringBuffer postList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_item_detailed);
        TextView txtView = (TextView) findViewById(R.id.txtPostList);
        Intent intent = getIntent();
        Bundle intentExtra = intent.getExtras();
        routeInfoItem = (routeInfo) intentExtra.getSerializable("routeInfoItem");

        postList = new StringBuffer();
        postList.append("-----------" +
                "\n• Xe số : " + routeInfoItem.getRouteNo() +
                "\n• Tuyến : " + routeInfoItem.getRouteName() +
                "\n• Loại : " + routeInfoItem.getType() +
                "\n• Tổng cự ly hành trình : " + routeInfoItem.getDistance() +
                "\n• Đơn vị chủ quản : " + routeInfoItem.getOrgs() +
                "\n• Tổng thời gian đi : " + routeInfoItem.getTimeOfTrip() + " phút" +
                "\n• Khoảng đợi giữa 2 tuyến liên tiếp : " + routeInfoItem.getHeadway() + " phút" +
                "\n• Thời gian hoạt động : " + routeInfoItem.getOperationTime() +
                "\n• Số chỗ ngồi : " + routeInfoItem.getNumOfSeats() +
                "\n• Xuất Phát tại : " + routeInfoItem.getOutBoundName() +
                "\n• Kết Thúc tại : " + routeInfoItem.getInBoundName() +
                "\n• Lộ Trình lượt đi : " + routeInfoItem.getOutBoundDescription() +
                "\n• Lộ Trình lượt về : " + routeInfoItem.getInBoundDescription() +
                "\n• Tổng số chuyến : " + routeInfoItem.getTotalTrip() +
                "\n• Giá vé thường : " + routeInfoItem.getNormalTicket() +
                "\n• Giá vé sinh viên : " + routeInfoItem.getStudentTicket() +
                "\n• Giá vé tháng : " + routeInfoItem.getMonthlyTicket());

        txtView.setText(postList);


    }
}
