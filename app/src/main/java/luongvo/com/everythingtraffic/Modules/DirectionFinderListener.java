package luongvo.com.everythingtraffic.Modules;

import java.util.List;



public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
