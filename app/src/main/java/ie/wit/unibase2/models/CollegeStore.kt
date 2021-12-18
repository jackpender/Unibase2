package ie.wit.unibase2.models

interface CollegeStore {
    fun findAll(): List<CollegeModel>
    fun findOne(id: Long): CollegeModel?
    fun create(college: CollegeModel)
//    fun createCourse(course: CourseModel)
    fun update(college: CollegeModel)
    fun delete(college: CollegeModel)
    fun serialize()
}