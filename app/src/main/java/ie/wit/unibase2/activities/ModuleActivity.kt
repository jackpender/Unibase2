package ie.wit.unibase2.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import ie.wit.unibase2.R
import ie.wit.unibase2.databinding.ActivityCourseBinding
import ie.wit.unibase2.databinding.ActivityModuleBinding
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.*
import timber.log.Timber.i

//var courseModel = CourseModel()
//var courseId = courseModel.id

class ModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModuleBinding
    var module = ModuleModel()
    lateinit var app: MainApp

    var collegeListActivity = CollegeListActivity()
    var courseListActivity = CourseListActivity()
    var moduleListActivity = ModuleListActivity()

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        i("Module Col ID: ${courseListActivity.courseId1}")
        super.onCreate(savedInstanceState)
        binding = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edit = false

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Module Activity started...")

        if (intent.hasExtra("module_edit")) {
            edit = true
            module = intent.extras?.getParcelable("module_edit")!!
            binding.moduleTitle.setText(module.title)
            binding.moduleDescription.setText(module.description)
            binding.moduleCredits.setText(module.credits)
            binding.btnAddModule.setText(R.string.save_module)
        }

        binding.btnAddModule.setOnClickListener() {
            var foundCourse = app.courses.findOne(courseListActivity.courseId1)
            i("Found Course: ${foundCourse}")
            module.id = generateRandomId()
            module.title = binding.moduleTitle.text.toString()
            module.description = binding.moduleDescription.text.toString()
            module.credits = binding.moduleCredits.text.toString()
            if (module.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_module_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
//                    var foundModule = app.modules.findOne(moduleListActivity.moduleId1)
//                    var i: Int = 0
                    for(i in foundCourse!!.modules){
                        i("id: ${module.id}")
                        if (i.id == courseListActivity.courseId1){
                            var index = foundCourse!!.modules.indexOf(i)
                            foundCourse!!.modules.set(index, module)
                            i("index: ${index}")
                            app.colleges.serialize()
                        }
//                        i("index: ${i}")
                    }
//                    var index = foundCollege!!.courses.indexOf(foundCourse)
//                    i("Found Course: ${foundCourse}")
                    app.modules.update(module.copy())
                } else {
                    app.modules.create(module.copy())
                    foundCourse!!.modules.add(module.copy())
//                    app.college.courses.add()
                    i("${module}")
                    app.colleges.serialize()
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_module, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
//                val launcherIntent = Intent(this, CourseListActivity::class.java)
//                refreshIntentLauncher.launch(launcherIntent)
                finish()
            }
//            R.id.item_addModule -> {
//                i("Add Module Button Pressed")
//                val launcherIntent = Intent(this, ModuleActivity::class.java)
//                refreshIntentLauncher.launch(launcherIntent)
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}