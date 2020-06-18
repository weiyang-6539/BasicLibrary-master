package com.github.android.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.android.common.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

/**
 * Created by *** on 2018/6/30.
 */
public class MyWebView extends WebView {
    private final String TAG = getClass().getSimpleName();
    private ProgressBar progressbar;
    private String originalUrl;
    private Handler handler;

    public MyWebView(Context context) {
        super(context);

        initWebView();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWebView();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        handler = new Handler();
        progressbar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));

        Drawable drawable = getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);

        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new MyWebChromeClient());

        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        getSettings().setLoadWithOverviewMode(false);
        getSettings().setUseWideViewPort(false);
        getSettings().setDomStorageEnabled(true);
        getSettings().setNeedInitialFocus(true);
        getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        getSettings().setDefaultFontSize(16);
        getSettings().setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        getSettings().setGeolocationEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && canGoBack()
                    && !noCanGoBack()) {
                goBack();
                return true;
            }
            return false;
        });
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        originalUrl = url;
    }

    private boolean noCanGoBack() {
        String url = getUrl();

        Log.e(TAG, "url:" + url);
        Log.e(TAG, "original:" + originalUrl);
        if (url.contains(originalUrl) && url.contains("#"))
            return true;
        return false;
    }

    public void quickCallJs(String method, String... params) {

        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append(method);
        if (params == null || params.length == 0) {
            sb.append("()");
        } else {
            sb.append("(").append(concat(params)).append(")");
        }

        Log.e(TAG, sb.toString());
        handler.post(() -> {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                evaluateJavascript(sb.toString(), null);
            } else {
                loadUrl(sb.toString());
            }*/
            loadUrl(sb.toString());
        });
    }

    private String concat(String... params) {
        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (!isJson(param)) {

                mStringBuilder.append("\"").append(param).append("\"");
            } else {
                mStringBuilder.append(param);
            }

            if (i != params.length - 1) {
                mStringBuilder.append(" , ");
            }

        }

        return mStringBuilder.toString();
    }

    private boolean isJson(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }

        boolean tag;
        try {
            if (target.startsWith("[")) {
                new JSONArray(target);
            } else {
                new JSONObject(target);
            }
            tag = true;
        } catch (JSONException ignore) {
            tag = false;
        }
        return tag;
    }


    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView mWebView = new WebView(view.getContext());
            WebViewTransport transport = (WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebView);
            resultMsg.sendToTarget();
            return true;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            Log.i("test", "openFileChooser 1");
            MyWebView.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
            Log.i("test", "openFileChooser 2");
            MyWebView.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i("test", "openFileChooser 3");
            MyWebView.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android  >= 5.0
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
            MyWebView.this.uploadFiles = filePathCallback;
            openFileChooseProcess();
            return true;
        }
    }

    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    public void onReceiveValue(Uri uri) {
        if (uploadFile != null)
            uploadFile.onReceiveValue(uri);
        uploadFile = null;
    }

    public void onReceiveValues(Uri[] uris) {
        if (uploadFiles != null)
            uploadFiles.onReceiveValue(uris);
        uploadFiles = null;

        String url = getUrl();

        Log.e("MyWebView", "originalUrl:" + originalUrl + "\nurl:" + url);
    }

    private void openFileChooseProcess() {
        if (delegate != null) {
            delegate.onShowChooseFile();
        }
    }

    public class MyWebViewClient extends WebViewClient {
        /**
         * 记录URL的栈
         */
        private final Stack<String> mUrls = new Stack<>();
        /**
         * 判断页面是否加载完成
         */
        private boolean mIsLoading;
        private String mUrlBeforeRedirect;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.e(TAG, "页面开始,url:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.e(TAG, "页面结束,url:" + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void onDestroy() {
        clearHistory();
        loadUrl("about:blank");
        stopLoading();
        setWebChromeClient(null);
        setWebViewClient(null);
        destroy();
    }

    private HtmlDelegate delegate;

    public void setHtmlDelegate(HtmlDelegate delegate) {
        this.delegate = delegate;
    }

    public interface HtmlDelegate {
        void onShowChooseFile();
    }
}
