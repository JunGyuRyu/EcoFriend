package my.application.ecogreen.datas

import android.app.Application

class MyData : Application(){
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}