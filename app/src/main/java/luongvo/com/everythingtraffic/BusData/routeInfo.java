package luongvo.com.everythingtraffic.BusData;

/**
 * Created by luongvo on 21/06/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class routeInfo {

    @SerializedName("RouteId")
    @Expose
    private Integer routeId;
    @SerializedName("RouteNo")
    @Expose
    private String routeNo;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Distance")
    @Expose
    private Integer distance;
    @SerializedName("Orgs")
    @Expose
    private String orgs;
    @SerializedName("TimeOfTrip")
    @Expose
    private String timeOfTrip;
    @SerializedName("Headway")
    @Expose
    private String headway;
    @SerializedName("OperationTime")
    @Expose
    private String operationTime;
    @SerializedName("NumOfSeats")
    @Expose
    private String numOfSeats;
    @SerializedName("OutBoundName")
    @Expose
    private String outBoundName;
    @SerializedName("InBoundName")
    @Expose
    private String inBoundName;
    @SerializedName("OutBoundDescription")
    @Expose
    private String outBoundDescription;
    @SerializedName("InBoundDescription")
    @Expose
    private String inBoundDescription;
    @SerializedName("TotalTrip")
    @Expose
    private String totalTrip;
    @SerializedName("NormalTicket")
    @Expose
    private String normalTicket;
    @SerializedName("StudentTicket")
    @Expose
    private String studentTicket;
    @SerializedName("MonthlyTicket")
    @Expose
    private String monthlyTicket;

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }

    public String getTimeOfTrip() {
        return timeOfTrip;
    }

    public void setTimeOfTrip(String timeOfTrip) {
        this.timeOfTrip = timeOfTrip;
    }

    public String getHeadway() {
        return headway;
    }

    public void setHeadway(String headway) {
        this.headway = headway;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(String numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public String getOutBoundName() {
        return outBoundName;
    }

    public void setOutBoundName(String outBoundName) {
        this.outBoundName = outBoundName;
    }

    public String getInBoundName() {
        return inBoundName;
    }

    public void setInBoundName(String inBoundName) {
        this.inBoundName = inBoundName;
    }

    public String getOutBoundDescription() {
        return outBoundDescription;
    }

    public void setOutBoundDescription(String outBoundDescription) {
        this.outBoundDescription = outBoundDescription;
    }

    public String getInBoundDescription() {
        return inBoundDescription;
    }

    public void setInBoundDescription(String inBoundDescription) {
        this.inBoundDescription = inBoundDescription;
    }

    public String getTotalTrip() {
        return totalTrip;
    }

    public void setTotalTrip(String totalTrip) {
        this.totalTrip = totalTrip;
    }

    public String getNormalTicket() {
        return normalTicket;
    }

    public void setNormalTicket(String normalTicket) {
        this.normalTicket = normalTicket;
    }

    public String getStudentTicket() {
        return studentTicket;
    }

    public void setStudentTicket(String studentTicket) {
        this.studentTicket = studentTicket;
    }

    public String getMonthlyTicket() {
        return monthlyTicket;
    }

    public void setMonthlyTicket(String monthlyTicket) {
        this.monthlyTicket = monthlyTicket;
    }
}
