package ie.wit.unibase2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.unibase2.activities.CollegeListActivity
import ie.wit.unibase2.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE_COURSES = "courses.json"
val gsonCourseBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listTypeCourse: Type = object : TypeToken<List<CourseModel>>() {}.type

fun generateRandomCourseId(): Long {
    return Random().nextLong()
}

class CourseJSONStore(private val context: Context) : CourseStore {

    var courses = mutableListOf<CourseModel>()

    var collegeListActivity = CollegeListActivity()

    init {
        if (exists(context, JSON_FILE_COURSES)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CourseModel> {
        logAll()
        return courses
    }

    override fun findOne(id: Long) : CourseModel? {
        var foundCourse: CourseModel? = courses.find { p -> p.id == id }
        return foundCourse
    }

    override fun create(course: CourseModel) {
        course.id = generateRandomCourseId()
        courses.add(course)
        serialize()
    }

    override fun update(course: CourseModel) {
        var foundCourse = findOne(course.id!!)
        if (foundCourse != null) {
            foundCourse.title = foundCourse.title
            foundCourse.description = foundCourse.description
            foundCourse.years = foundCourse.years
            foundCourse.modules = foundCourse.modules
        }
        serialize()
    }

    override fun delete(course: CourseModel) {
        var foundCourse = findOne(course.id!!)
        if(foundCourse != null){
            courses.remove(foundCourse)
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonCourseBuilder.toJson(courses, listTypeCourse)
        write(context, JSON_FILE_COURSES, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE_COURSES)
        courses = gsonCourseBuilder.fromJson(jsonString, listTypeCourse)
    }

    private fun logAll() {
        courses.forEach { Timber.i("$it") }
    }
}

//class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Uri {
//        return Uri.parse(json?.asString)
//    }
//
//    override fun serialize(
//        src: Uri?,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        return JsonPrimitive(src.toString())
//    }
//}