package ie.wit.unibase2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.unibase2.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

//const val JSON_FILE = "colleges.json"
//val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
//    .registerTypeAdapter(Uri::class.java, UriParser())
//    .create()
val listTypeModule: Type = object : TypeToken<List<ModuleModel>>() {}.type

fun generateRandomModuleId(): Long {
    return Random().nextLong()
}

class ModuleJSONStore(private val context: Context) : ModuleStore {

    var modules = mutableListOf<ModuleModel>()

//    init {
//        if (exists(context, JSON_FILE)) {
//            deserialize()
//        }
//    }

    override fun findAll(): MutableList<ModuleModel> {
        logAll()
        return modules
    }

    override fun findOne(id: Long) : ModuleModel? {
        var foundModule: ModuleModel? = modules.find { p -> p.id == id }
        return foundModule
    }

    override fun create(module: ModuleModel) {
        module.id = generateRandomModuleId()
        modules.add(module)
//        serialize()
    }

    override fun update(module: ModuleModel) {
        var foundModule = findOne(module.id!!)
        if (foundModule != null) {
            foundModule.title = foundModule.title
            foundModule.description = foundModule.description
            foundModule.credits = foundModule.credits
        }
//        serialize()
    }

    override fun delete(module: ModuleModel) {
        var foundModule = findOne(module.id!!)
        if(foundModule != null){
            modules.remove(foundModule)
        }
//        serialize()
    }

//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(courses, listType)
//        write(context, JSON_FILE, jsonString)
//    }

//    private fun deserialize() {
//        val jsonString = read(context, JSON_FILE)
//        colleges = gsonBuilder.fromJson(jsonString, listType)
//    }

    private fun logAll() {
        modules.forEach { Timber.i("$it") }
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