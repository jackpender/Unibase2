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
import com.squareup.picasso.Picasso
import ie.wit.unibase2.R
import ie.wit.unibase2.adapters.CourseAdapter
import ie.wit.unibase2.databinding.ActivityCollegeBinding
import ie.wit.unibase2.databinding.ActivityCourseListBinding
import ie.wit.unibase2.helpers.showImagePicker
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.CollegeModel
import ie.wit.unibase2.models.CourseModel
import ie.wit.unibase2.models.Location
import timber.log.Timber.i

class CollegeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCollegeBinding
//    private lateinit var bindingCourse: ActivityCourseListBinding
    var college = CollegeModel()
    lateinit var app: MainApp

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

//    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegeBinding.inflate(layoutInflater)
//        bindingCourse = ActivityCourseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edit = false

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("College Activity started...")

        if (intent.hasExtra("college_edit")) {
            edit = true
            college = intent.extras?.getParcelable("college_edit")!!
            binding.collegeTitle.setText(college.title)
            binding.description.setText(college.description)
            binding.btnAdd.setText(R.string.save_college)
            Picasso.get()
                .load(college.image)
                .into(binding.collegeImage)
            if (college.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_college_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            college.title = binding.collegeTitle.text.toString()
            college.description = binding.description.text.toString()
            if (college.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_college_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.colleges.update(college.copy())
                    i("${college}")
                } else {
                    app.colleges.create(college.copy())
                    i("${college}")
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            i("Select image")
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.collegeLocation.setOnClickListener {
            i ("Set Location Pressed")
        }

//        binding.collegeLocation.setOnClickListener {
//            val launcherIntent = Intent(this, MapActivity::class.java)
//            mapIntentLauncher.launch(launcherIntent)
//        }

        binding.collegeLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (college.zoom != 0f) {
                location.lat =  college.lat
                location.lng = college.lng
                location.zoom = college.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
//        registerRefreshCallback()
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
                i("Add Course Button Pressed")
                val launcherIntent = Intent(this, CourseActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { bindingCourse.recyclerView.adapter?.notifyDataSetChanged() }
//    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            college.image = result.data!!.data!!
                            Picasso.get()
                                .load(college.image)
                                .into(binding.collegeImage)
                            binding.chooseImage.setText(R.string.change_college_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            college.lat = location.lat
                            college.lng = location.lng
                            college.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    fun getId() : Long {
        var strId : String? // String to hold user input
        var searchId : Long // Long to hold converted id
        print("Enter id to Search/Update : ")
        strId = readLine()!!
        searchId = if (strId.toLongOrNull() != null && !strId.isEmpty())
            strId.toLong()
        else
            -9
        return searchId
    }

    fun search(id: Long) : CollegeModel? {
        var foundCollege = app.colleges.findOne(id)
        return foundCollege
    }

//    private fun register
}