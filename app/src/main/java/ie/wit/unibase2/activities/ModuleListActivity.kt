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
import ie.wit.unibase2.adapters.*
import ie.wit.unibase2.databinding.ActivityCollegeListBinding
import ie.wit.unibase2.databinding.ActivityCourseListBinding
import ie.wit.unibase2.databinding.ActivityModuleListBinding
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.CollegeModel
import ie.wit.unibase2.models.CourseModel
import ie.wit.unibase2.models.ModuleModel
import timber.log.Timber
import timber.log.Timber.i

var moduleModel = ModuleModel()
var moduleId = moduleModel.id

class ModuleListActivity : AppCompatActivity(), ModuleListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityModuleListBinding

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    var course = CourseModel()
    var moduleId1 = moduleId

    var courseListActivity = CourseListActivity()
    var collegeListActivity = CollegeListActivity()
//    var courseActivity = CourseActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
//        i("Col ID: ${courseListActivity.courseId1}")
        super.onCreate(savedInstanceState)
        binding = ActivityModuleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Module started...")
//        i("${course.title}")

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ModuleAdapter(app.modules.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        loadModules()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_course, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_addModule -> {
                val launcherIntent = Intent(this, ModuleActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_editCourse -> {
                var foundCourse = app.courses.findOne(courseListActivity.courseId1)
//                i("Found Course: ${foundCourse}")
                val launcherIntent = Intent(this, CourseActivity::class.java)
                launcherIntent.putExtra("course_edit", foundCourse)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_deleteCourse -> {
                var foundCourse = app.courses.findOne(courseListActivity.courseId1)
                app.courses.delete(foundCourse!!)
                var foundCollege = app.colleges.findOne(collegeListActivity.collegeId1)
                foundCollege!!.courses.remove(foundCourse)
                val launcherIntent = Intent(this, CourseListActivity::class.java)
//                launcherIntent.putExtra("course_edit", course)
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
//            { binding.recyclerView.adapter?.notifyDataSetChanged() }
            { loadModules() }
    }

    private fun loadModules() {
        showModules(app.modules.findAll())
    }

    fun showModules (modules: List<ModuleModel>) {
        var foundCourse = app.courses.findOne(courseListActivity.courseId1)
        binding.recyclerView.adapter = ModuleAdapter(foundCourse!!.modules, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
//        bindingCollege.recyclerView.adapter = CollegeAdapter(college.courses, this)
    }

    override fun onModuleClick(module: ModuleModel) {
        moduleId = module.id
        val launcherIntent = Intent(this, ModuleActivity::class.java)
        launcherIntent.putExtra("module_edit", module)
        refreshIntentLauncher.launch(launcherIntent)
        i("Module ID: ${moduleId}")
    }
}