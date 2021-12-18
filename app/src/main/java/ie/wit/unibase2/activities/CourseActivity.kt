package ie.wit.unibase2.activities

import android.content.Context
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
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.*
import timber.log.Timber.i

class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    var course = CourseModel()
//    var college = CollegeModel()
    lateinit var app: MainApp
    var collegeActivity = CollegeActivity()
    var collegeListActivity = CollegeListActivity()
    var courseListActivity = CourseListActivity()

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

//    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
//
//    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

//    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edit = false

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Course Activity started...")

        if (intent.hasExtra("course_edit")) {
            edit = true
            course = intent.extras?.getParcelable("course_edit")!!
            binding.courseTitle.setText(course.title)
            binding.courseDescription.setText(course.description)
            binding.courseYears.setText(course.years)
            binding.btnAddCourse.setText(R.string.save_course)
//            binding.btnDeleteCourse.setText(R.string.button_deleteCourse)
        }

        binding.btnAddCourse.setOnClickListener() {
            var foundCollege = app.colleges.findOne(collegeListActivity.collegeId1)
//            i("Found College: ${foundCollege}")
            course.id = generateRandomId()
            course.title = binding.courseTitle.text.toString()
            course.description = binding.courseDescription.text.toString()
            course.years = binding.courseYears.text.toString()
            if (course.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_course_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    i("this course: ${courseListActivity.courseId1}")
                    var foundCourse = app.courses.findOne(courseListActivity.courseId1)
                    var i: Int = 0
                    for(i in foundCollege!!.courses){
                        i("id: ${course.id}")
                        if (i.id == courseListActivity.courseId1){
                            var index = foundCollege!!.courses.indexOf(i)
                            foundCollege!!.courses.set(index, course)
                            i("index: ${index}")
                            app.colleges.serialize()
                        }
//                        i("index: ${i}")
                    }
//                    var index = foundCollege!!.courses.indexOf(foundCourse)
//                    i("Found Course: ${foundCourse}")
//                    app.courses.update(course.copy())
                } else {
                    app.courses.create(course)
                    foundCollege!!.courses.add(course)
                    app.colleges.serialize()
//                    app.college.courses.add()
                    i("${course}")
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_course, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
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