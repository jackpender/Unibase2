package ie.wit.unibase2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.unibase2.databinding.CardCourseBinding
import ie.wit.unibase2.models.CourseModel

interface CourseListener {
    fun onCourseClick(course: CourseModel)
}

class CourseAdapter(private var courses: List<CourseModel>
                    , private val listener: CourseListener
) :
    RecyclerView.Adapter<CourseAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCourseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val course = courses[holder.adapterPosition]
        holder.bind(course, listener)
    }

    override fun getItemCount(): Int = courses.size


    class MainHolder(private val binding : CardCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseModel, listener: CourseListener) {
            binding.courseTitle.text = course.title
            binding.courseDescription.text = course.description
            binding.courseYears.text = course.years
//            Picasso.get().load(college.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onCourseClick(course) }
        }
    }
}