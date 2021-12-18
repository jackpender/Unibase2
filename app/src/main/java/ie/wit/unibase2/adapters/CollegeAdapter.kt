package ie.wit.unibase2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.unibase2.databinding.CardCollegeBinding
import ie.wit.unibase2.models.CollegeModel

interface CollegeListener {
    fun onCollegeClick(college: CollegeModel)
}

class CollegeAdapter constructor(private var colleges: List<CollegeModel>
                                ,private val listener: CollegeListener) :
    RecyclerView.Adapter<CollegeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCollegeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val college = colleges[holder.adapterPosition]
        holder.bind(college, listener)
    }

    override fun getItemCount(): Int = colleges.size


    class MainHolder(private val binding : CardCollegeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(college: CollegeModel, listener: CollegeListener) {
            binding.collegeTitle.text = college.title
            binding.description.text = college.description
            Picasso.get().load(college.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onCollegeClick(college) }
        }
    }
}
