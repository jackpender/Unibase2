package ie.wit.unibase2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.unibase2.activities.CollegeListActivity
import ie.wit.unibase2.helpers.*
import ie.wit.unibase2.main.MainApp
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "colleges.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<List<CollegeModel>>() {}.type
val listCourseType: Type = object : TypeToken<List<CourseModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CollegeJSONStore(private val context: Context) : CollegeStore {

    var colleges = mutableListOf<CollegeModel>()
    var courses = mutableListOf<CourseModel>()
    var modules = mutableListOf<ModuleModel>()
    var collegeListActivity = CollegeListActivity()

    lateinit var app: MainApp

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CollegeModel> {
        logAll()
        return colleges
    }

    override fun findOne(id: Long) : CollegeModel? {
        var foundCollege: CollegeModel? = colleges.find { p -> p.id == id }
        return foundCollege
    }

    override fun create(college: CollegeModel) {
        college.id = generateRandomId()
        colleges.add(college)
        serialize()
    }

//    override fun createCourse(course: CourseModel) {
////        college.id = generateRandomId()
//        var college = app.colleges.findOne(collegeListActivity.collegeId1)
//        college!!.courses.add(course)
//        serialize()
//    }

    override fun update(college: CollegeModel) {
        var foundCollege = findOne(college.id!!)
        if (foundCollege != null) {
            foundCollege.title = foundCollege.title
            foundCollege.description = foundCollege.description
            foundCollege.courses = foundCollege.courses
        }
        serialize()
    }

    override fun delete(college: CollegeModel) {
        var foundCollege = findOne(college.id!!)
        if(foundCollege != null){
            colleges.remove(foundCollege)
        }
        serialize()
    }

    override fun serialize() {
        val jsonString = gsonBuilder.toJson(colleges, listType)
        val jsonCourseString = gsonBuilder.toJson(courses, listCourseType)
        write(context, JSON_FILE, jsonString)
//        write(context,JSON_FILE,jsonCourseString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        val jsonCourseString = read(context,JSON_FILE)
        colleges = gsonBuilder.fromJson(jsonString, listType)
//        courses = gsonBuilder.fromJson(jsonCourseString, listCourseType)
    }

    private fun logAll() {
        colleges.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}