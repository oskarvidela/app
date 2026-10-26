package cl.efe.cultura.mse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

public class MainActivity extends Activity {

    private static final String BASE = "file:///android_asset/www/index.html";
    private WebView web;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setBuiltInZoomControls(false);

        // Los enlaces externos se abren en el navegador
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                if ("file".equals(u.getScheme())) return false;
                startActivity(new Intent(Intent.ACTION_VIEW, u));
                return true;
            }
        });

        // Audioguía con la voz del teléfono
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int r = tts.setLanguage(new Locale("es", "CL"));
                if (r < 0) tts.setLanguage(new Locale("es"));
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override public void onStart(String id) {}
                    @Override public void onDone(String id) { done(); }
                    @Override public void onError(String id) { done(); }
                });
            }
        });
        web.addJavascriptInterface(new Voz(), "AndroidTTS");

        web.loadUrl(BASE + hashFrom(getIntent(), ""));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String h = hashFrom(intent, null);
        if (h != null) web.evaluateJavascript("location.hash='" + h.substring(1) + "'", null);
    }

    /** efecultura://mse/p3  →  #p3 */
    private String hashFrom(Intent i, String fallback) {
        Uri u = i == null ? null : i.getData();
        if (u == null) return fallback;
        String last = u.getLastPathSegment();
        if (last != null && last.matches("p[1-9]|ruta|info|mapa")) return "#" + last;
        return fallback;
    }

    private void done() {
        runOnUiThread(() -> web.evaluateJavascript("window.ttsDone&&window.ttsDone()", null));
    }

    class Voz {
        @JavascriptInterface public void speak(String text) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "parada");
        }
        @JavascriptInterface public void stop() { tts.stop(); }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript(
            "(function(){var s=document.getElementById('sheet');" +
            "if(s&&s.classList.contains('open')){document.getElementById('s-close').click();return 1}" +
            "if(document.getElementById('v-mapa').hidden){document.querySelector('.tab[data-v=mapa]').click();return 1}" +
            "return 0})()",
            v -> { if ("0".equals(v)) finish(); });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) tts.shutdown();
        super.onDestroy();
    }
}
