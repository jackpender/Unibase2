package ie.wit.unibase2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.unibase2.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "colleges.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<CollegeModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CollegeJSONStore(private val context: Context) : CollegeStore {

    var colleges = mutableListOf<CollegeModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CollegeModel> {
        logAll()
        return colleges
    }

    override fun create(college: CollegeModel) {
        college.id = generateRandomId()
        colleges.add(college)
        serialize()
    }


    override fun update(college: CollegeModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(colleges, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        colleges = gsonBuilder.fromJson(jsonString, listType)
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