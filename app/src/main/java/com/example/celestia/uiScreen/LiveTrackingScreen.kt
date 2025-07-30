package com.example.celestia.uiScreen

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.celestia.viewmodel.ApodViewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.delay
/*
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LiveTrackingScreen(viewModel: ApodViewModel) { // renders the live tracking screen using a WebView
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        AndroidView( //embeds a traditional Android WebView inside a Compose UI.
            modifier = Modifier.fillMaxSize(),
            factory = { context -> //Creates the WebView instance.
                android.webkit.WebView(context).apply {
                    webViewClient = WebViewClient() // prevents external browser from opening when navigating inside WebView.
                    settings.javaScriptEnabled = true  //enables JavaScript in the page (necessary for DOM manipulation).
                    loadUrl("https://stellarium-web.org/")

                    // runs 15 times, every 200 ms.
                    for (i in 0 until 15) {
                        postDelayed({
                            evaluateJavascript(
                                """
                                (function() {
                                    if (window.__iconHandled) return;
                                    var icon = document.querySelector('.v-app-bar__nav-icon');
                                    if (icon) {
                                        icon.click();      // close the menu    
                                        icon.remove();      // delete the button from DOM
                                        window.__iconHandled = true; // do not run again
                                        return "Icon clicked and removed";
                                    }
                                    return "Icon not found yet";
                                })();
                                """.trimIndent(), null
                            )
                        }, (i * 200).toLong())
                    }

                }
            }
        )
    }
}
*/

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LiveTrackingScreen(viewModel: ApodViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                android.webkit.WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl("https://stellarium-web.org/")

                    // Step 1: Close and remove hamburger menu icon
                    for (i in 0 until 15) {
                        postDelayed({
                            evaluateJavascript(
                                """
                                (function() {
                                    if (window.__iconHandled) return;
                                    var icon = document.querySelector('.v-app-bar__nav-icon');
                                    if (icon) {
                                        icon.click();      // close side menu
                                        icon.remove();      // remove icon
                                        window.__iconHandled = true;
                                        return "Icon clicked and removed";
                                    }
                                    return "Icon not found yet";
                                })();
                                """.trimIndent(), null
                            )
                        }, (i * 200).toLong())
                    }

                    // Step 2: Log device orientation for testing
                    postDelayed({
                        evaluateJavascript(
                            """
                            (function() {
                                window.addEventListener('deviceorientation', function(event) {
                                    console.log("Device Orientation → α: " + event.alpha + ", β: " + event.beta + ", γ: " + event.gamma);
                                });
                            })();
                            """.trimIndent(), null
                        )
                    }, 2000)

                    // ✅ Step 3: Automatically enable compass mode in Stellarium
                    postDelayed({
                        evaluateJavascript(
                            """
                            (function() {
                                const compassBtn = document.querySelector('button[aria-label="Start compass"]');
                                if (compassBtn) {
                                    compassBtn.click();
                                    return "Compass mode started";
                                }
                                return "Compass button not found";
                            })();
                            """.trimIndent(), null
                        )
                    }, 2500) // give enough time for page & DOM to load

                    // Step 4: Click compass button if exists
                    postDelayed({
                        evaluateJavascript(
                        """
                        (function() {
                            let compassButton = [...document.querySelectorAll('button')]
                                .find(btn => btn.innerText.toLowerCase().includes('compass') || btn.title.toLowerCase().includes('compass'));
                            if (compassButton) {
                                compassButton.click();
                                return "Compass mode activated";
                            } else {
                                return "Compass button not found";
                            }
                        })();
                        """.trimIndent(), null
                        )
                    }, 3000)

                }
            }
        )
    }
}
