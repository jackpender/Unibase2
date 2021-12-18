package ie.wit.unibase2.models

import timber.log.Timber.i

class CollegeMemStore : CollegeStore {

    val colleges = ArrayList<CollegeModel>()

    override fun findAll(): List<CollegeModel> {
        return colleges
    }

    override fun create(college: CollegeModel) {
        college.id++
        colleges.add(college)
    }

    override fun update(college: CollegeModel) {
        val foundCollege: CollegeModel? = colleges.find { p -> p.id == college.id }
        if (foundCollege != null) {
            foundCollege.title = college.title
            foundCollege.description = college.description
            foundCollege.image = college.image
            foundCollege.lat = college.lat
            foundCollege.lng = college.lng
            foundCollege.zoom = college.zoom
            logAll()
        }
    }

    fun logAll() {
        colleges.forEach{ i("${it}") }
    }
}