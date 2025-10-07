package com.example.unit5_pathway2_bookselfapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.unit5_pathway2_bookselfapp.ui.BookShelfApp
import com.example.unit5_pathway2_bookselfapp.ui.theme.Unit5_PathWay2_BookSelfAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Unit5_PathWay2_BookSelfAppTheme {
                BookShelfApp()
            }
        }
    }
}
