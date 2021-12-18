package ie.wit.unibase2.models

interface CourseStore {
    fun findAll(): List<CourseModel>
    fun findOne(id: Long): CourseModel?
    fun create(course: CourseModel)
    fun update(course: CourseModel)
    fun delete(course: CourseModel)
}