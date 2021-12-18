package ie.wit.unibase2.main

import android.app.Application
import ie.wit.unibase2.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    var college = CollegeModel()

    lateinit var colleges: CollegeStore
    lateinit var courses: CourseStore
    lateinit var modules: ModuleStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        colleges = CollegeJSONStore(applicationContext)
        courses = CourseJSONStore(applicationContext)
        modules = ModuleJSONStore(applicationContext)
        i("College started")
    }
}