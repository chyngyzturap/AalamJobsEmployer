package com.pharos.aalamjobsemployer.ui.vacancies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import kotlinx.android.synthetic.main.activity_cv_web_preview.*

class CvWebPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_web_preview)
        openWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebView() {
        val html = intent.getIntExtra("cvId", 0)
        WebView.setWebContentsDebuggingEnabled(true)
        web_view.settings.javaScriptEnabled = true
        web_view.settings.builtInZoomControls = true
        web_view.settings.domStorageEnabled = true
        web_view.settings.setSupportZoom(true)
        web_view.loadUrl("http://165.22.88.94:9000/api/resumes/$html/webview/")
        web_view.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                btn_print.setOnClickListener {
                    createWebPrintJob(view)
                }
            }
        }
    }

    private fun createWebPrintJob(webView: WebView) {
        (getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->
            val jobName = "${getString(R.string.app_name)} CV"
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            ).also { printJob ->
                Log.d("ololo", "createWebPrintJob: $printJob")
            }
        }
    }
}
