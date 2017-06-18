package luongvo.com.everythingtraffic.FavoritePlace;

import java.io.Serializable;

/**
 * Created by luongvo on 18/06/2017.
 */

public class PlaceInfo implements Serializable {
    private String title;
    private String body;

    public PlaceInfo(String title, String address) {
        this.title = title;
        this.body = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String address) {
        this.body = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
