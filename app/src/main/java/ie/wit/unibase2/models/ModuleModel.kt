package ie.wit.unibase2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var credits: String = "0"
): Parcelable