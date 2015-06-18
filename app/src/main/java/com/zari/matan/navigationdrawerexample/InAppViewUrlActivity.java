package com.zari.matan.navigationdrawerexample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zari.matan.navigationdrawerexample.items.ItemData;


public class InAppViewUrlActivity extends Activity {
	private ItemData itemData;
	private String url;
	final String TAG = InAppViewUrlActivity.class.getName();
	private ProgressBar preloader;
	private WebView webView;
	private ImageView backBtn;
	private TextView title;
	private ImageView more;
	private static WebChromeClient chromeClient;
	private FrameLayout customViewContainer;
	private WebChromeClient.CustomViewCallback customViewCallback;
	private View mCustomView;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_app_view_url);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("InAppViewUrl_item")
				&& savedInstanceState.containsKey("InAppViewUrl_url")) {
			itemData = ItemData.pars(savedInstanceState
					.getString("InAppViewUrl_item"));
			url = savedInstanceState.getString("InAppViewUrl_url");
		} else {
			if (getIntent() != null && getIntent().getExtras() != null
					&& getIntent().getExtras().containsKey("itemData")
					&& getIntent().getExtras().containsKey("url")) {
				itemData = ItemData.pars(getIntent().getExtras().getString(
						"itemData"));
				url = getIntent().getExtras().getString("url");
			} else {
				finish();
				return;
			}
		}
		backBtn = (ImageView) findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				webView.clearHistory();
				onBackPressed();
			}
		});
		title = (TextView) findViewById(R.id.title);
		preloader = (ProgressBar) findViewById(R.id.preloader);
		webView = (WebView) findViewById(R.id.web_view);
		webView.setVerticalFadingEdgeEnabled(false);
		webView.setInitialScale(1);
		WebSettings settings = webView.getSettings();
		customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setSaveFormData(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			settings.setDisplayZoomControls(false);
		}
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);
		chromeClient = new WebChromeClient() {
			private View mVideoProgressView;

			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress != 100) {
					if (preloader.getVisibility() == View.GONE)
						preloader.setVisibility(View.VISIBLE);
				} else if (preloader.getVisibility() == View.VISIBLE) {
					preloader.setVisibility(View.GONE);
				}
				preloader.setProgress(newProgress);
			}

			@Override
			public void onShowCustomView(View view, int requestedOrientation,
					CustomViewCallback callback) {
				onShowCustomView(view, callback);
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				if (mCustomView != null) {
					callback.onCustomViewHidden();
					return;
				}
				mCustomView = view;
				webView.setVisibility(View.GONE);
				customViewContainer.setVisibility(View.VISIBLE);
				customViewContainer.addView(view);
				customViewCallback = callback;
			}

			@Override
			public View getVideoLoadingProgressView() {
				if (mVideoProgressView == null) {
					LayoutInflater inflater = LayoutInflater
							.from(InAppViewUrlActivity.this);
					mVideoProgressView = inflater.inflate(
							R.layout.video_progress, null);
				}
				return mVideoProgressView;
			}

			@Override
			public void onHideCustomView() {
				super.onHideCustomView();
				if (mCustomView == null)
					return;
				webView.setVisibility(View.VISIBLE);
				customViewContainer.setVisibility(View.GONE);
				mCustomView.setVisibility(View.GONE);
				customViewContainer.removeView(mCustomView);
				customViewCallback.onCustomViewHidden();
				mCustomView = null;
			}
		};
		webView.setWebChromeClient(chromeClient);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String pUrl) {
				if (pUrl.startsWith("http://www.youtube.com")
						|| pUrl.startsWith("vnd.youtube")) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(pUrl)));
					return true;
				}
				if (pUrl.contains("auto.co.il")) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(pUrl)));
					finish();
					return true;
				}
				return false;
			}
		});
		more = (ImageView) findViewById(R.id.more);

		if (url != null) {
			loadUrl();
		} else {
			preloader.setVisibility(View.GONE);
		}
		title.setText(itemData.text);
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		chromeClient.onHideCustomView();
	}

	@Override
	protected void onStop() {
		super.onStop(); // To change body of overridden methods use File |
						// Settings | File Templates.
		if (inCustomView()) {
			hideCustomView();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("InAppViewUrl_item", itemData.toJson().toString());
		outState.putString("InAppViewUrl_url", url);
		super.onSaveInstanceState(outState);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.getSettings().setDisplayZoomControls(false);
		}
		webView.onResume();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onPause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.getSettings().setDisplayZoomControls(true);
		}
		webView.onPause();
		super.onPause();
	}

	private void loadUrl() {
		preloader.setVisibility(View.VISIBLE);
		webView.loadUrl(url);
	}









	public void onBackPressed() {
		if (webView != null && webView.canGoBack()) {
			webView.goBack();
			return;
		}
		webView.loadData("<html><body>Pause <b>" + itemData.text
				+ "</b> points.</body></html>", "text/html", "UTF-8");
		super.onBackPressed();

	}
}