package com.kuldeepsiraswar.zookaresult.Jobs_Tab;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kuldeepsiraswar.zookaresult.MainActivity;
import com.kuldeepsiraswar.zookaresult.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TEACHING extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public WebView mWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static TEACHING newInstance(int position) {
        TEACHING fragment = new TEACHING();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public TEACHING() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* mPosition = getArguments().getInt("position");*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.webview, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        mWebView.loadUrl("https://zookaresult.in/app/teaching-jobs/");
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().getCacheMode();
        mWebView.clearCache(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                request.setTitle(URLUtil.guessFileName(url,contentDisposition,mimetype));
                request.setDescription("Downloading File");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url,contentDisposition,mimetype));
                DownloadManager downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(getActivity().getApplicationContext(),"Downloading File",Toast.LENGTH_LONG).show();

            }
        });
        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return v;
    }

    @Override
    public void onRefresh() {
        mWebView.reload();
    }
}