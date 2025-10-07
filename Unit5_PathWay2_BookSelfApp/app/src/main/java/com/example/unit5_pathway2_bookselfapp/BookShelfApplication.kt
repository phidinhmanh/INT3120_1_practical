package com.example.unit5_pathway2_bookselfapp
import android.app.Application
import com.example.unit5_pathway2_bookselfapp.data.AppContainer
import com.example.unit5_pathway2_bookselfapp.data.DefaultAppContainer


class BookShelfApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}