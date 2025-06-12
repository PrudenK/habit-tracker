package com.pruden.habits.modules.configuracionesModule.metodos.licencias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pruden.habits.R

class LicensesFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.licensesToolbar)
        val webView = view.findViewById<WebView>(R.id.licensesWebView)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        webView.settings.apply {
            javaScriptEnabled = false
            builtInZoomControls = true
            displayZoomControls = false
        }

        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_res/raw/licenses.html")
    }
}
