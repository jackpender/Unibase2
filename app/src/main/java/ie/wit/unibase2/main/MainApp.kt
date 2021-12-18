package ie.wit.unibase2.main

import android.app.Application
import ie.wit.unibase2.models.CollegeJSONStore
import ie.wit.unibase2.models.CollegeMemStore
import ie.wit.unibase2.models.CollegeStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var colleges: CollegeStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        colleges = CollegeJSONStore(applicationContext)
        i("College started")
    }
}