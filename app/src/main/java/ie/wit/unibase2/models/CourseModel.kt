package ie.wit.unibase2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseModel (
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var years: String = "0",
    var modules: ArrayList<ModuleModel> = ArrayList<ModuleModel>()
) : Parcelable