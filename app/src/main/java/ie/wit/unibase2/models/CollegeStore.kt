package ie.wit.unibase2.models

interface CollegeStore {
    fun findAll(): List<CollegeModel>
    fun create(college: CollegeModel)
    fun update(college: CollegeModel)
}