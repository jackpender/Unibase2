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
import ie.wit.unibase2.adapters.CollegeListener
import ie.wit.unibase2.databinding.ActivityCollegeListBinding
import ie.wit.unibase2.main.MainApp
import ie.wit.unibase2.models.CollegeModel

class CollegeListActivity : AppCompatActivity(), CollegeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCollegeListBinding

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = CollegeAdapter(app.colleges.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        loadColleges()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, CollegeActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCollegeClick(college: CollegeModel) {
        val launcherIntent = Intent(this, CollegeActivity::class.java)
        launcherIntent.putExtra("college_edit", college)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { binding.recyclerView.adapter?.notifyDataSetChanged() }
            { loadColleges() }
    }

    private fun loadColleges() {
        showColleges(app.colleges.findAll())
    }

    fun showColleges (colleges: List<CollegeModel>) {
        binding.recyclerView.adapter = CollegeAdapter(colleges, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}