package luongvo.com.everythingtraffic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FindBus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView webView = new WebView(getApplicationContext());
        setContentView(R.layout.waiting);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('header')[0].style.display='none'; })()");
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('NewsTwoBottom')[0].style.display='none'; })()");
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('NewsTwoContent')[0].style.display='none'; })()");
                webView.loadUrl("javascript:(function () {" +
                        "    elements = document.getElementsByClassName('cms-button');" +
                        "    for (var i = 0; i < elements.length; i++) {" +
                        "        elements[i].style.backgroundColor=\"blue\";" +
                        "    }" +
                        "})()");
                setContentView(webView);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getApplicationContext(), "Error, pls check your internet connection!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




        webView.loadUrl("http://buyttphcm.com.vn/RouteFinding");
    }
}
