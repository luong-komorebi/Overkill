package luongvo.com.everythingtraffic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;



import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;



/**
 * Created by luongvo on 20/06/2017.
 */

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntro2Fragment.newInstance("Welcome to Overkill...", "Your one stop for all transportation problems", R.drawable.school_bus, Color.parseColor("#303f9f")));
        addSlide(AppIntro2Fragment.newInstance("Never get lost again", "Find locations, nearby services and routes quickly", R.drawable.find_location, Color.parseColor("#e91e63")));
        addSlide(AppIntro2Fragment.newInstance("Forget no place", "Save your favorite places with customizable favorite lists", R.drawable.saveaplace, Color.parseColor("#827717")));
        addSlide(AppIntro2Fragment.newInstance("Words out now", "Capture and share your favorite places and photos with friends", R.drawable.adventuretime, Color.parseColor("#311b92")));
        addSlide(AppIntro2Fragment.newInstance("Wanna go for a ride?", "Call public taxi services like Uber at a button click", R.drawable.taxi, Color.parseColor("#304ffe")));
        addSlide(AppIntro2Fragment.newInstance("No wifi no worry", "View offline map when no wifi no cellular network available", R.drawable.light_bulb, Color.parseColor("#212121")));
        addSlide(AppIntro2Fragment.newInstance("Wait no more!!", "Explore now", R.drawable.explore_now, Color.parseColor("#3949ab")));

        showSkipButton(false);
        showStatusBar(false);
        setProgressButtonEnabled(true);
        setDepthAnimation();
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

}
