package ie.wit.unibase2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.unibase2.R
import ie.wit.unibase2.adapters.CollegeAdapter
import ie.wit.unibase2.adapters.CourseAdapter
import ie.wit.unibase2.adapters.CourseListener
import ie.wit.unibase2.databinding.ActivityCollegeListBinding
import ie.wit.unibase2.databinding.ActivityCourseListBinding
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.CollegeModel
import ie.wit.unibase2.models.CourseModel
import timber.log.Timber
import timber.log.Timber.i

var courseModel = CourseModel()
var courseId = courseModel.id

class CourseListActivity : AppCompatActivity(), CourseListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCourseListBinding
    private lateinit var bindingCollege: ActivityCollegeListBinding

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    var college = CollegeModel()
    var course = CourseModel()
    var courseId1 = courseId

    var collegeListActivity = CollegeListActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Course started...")
        i("${college.title}")

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = CourseAdapter(app.courses.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        loadCourses()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_college, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_addCourse -> {
                val launcherIntent = Intent(this, CourseActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_addModule -> {
                val launcherIntent = Intent(this, ModuleActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_deleteCollege -> {
                var foundCollege = app.colleges.findOne(collegeListActivity.collegeId1)
//                i("Found College: ${foundCollege}")
                app.colleges.delete(foundCollege!!)
                val launcherIntent = Intent(this, CollegeListActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
                app.colleges.serialize()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadCourses() }
    }

    private fun loadCourses() {
        showCourses(app.courses.findAll())
    }

    fun showCourses (courses: List<CourseModel>) {
        var foundCollege = app.colleges.findOne(collegeListActivity.collegeId1)
        binding.recyclerView.adapter = CourseAdapter(foundCollege!!.courses, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCourseClick(course: CourseModel) {
        courseId = course.id
        i("Module List Starting")
        val launcherIntent = Intent(this, ModuleListActivity::class.java)
        refreshIntentLauncher.launch(launcherIntent)
        i("Course ID: ${courseId}")
    }
}