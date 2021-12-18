package ie.wit.unibase2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.unibase2.databinding.CardCourseBinding
import ie.wit.unibase2.databinding.CardModuleBinding
import ie.wit.unibase2.models.CourseModel
import ie.wit.unibase2.models.ModuleModel

interface ModuleListener {
    fun onModuleClick(module: ModuleModel)
}

class ModuleAdapter(private var modules: List<ModuleModel>
                    , private val listener: ModuleListener
) :
    RecyclerView.Adapter<ModuleAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardModuleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val module = modules[holder.adapterPosition]
        holder.bind(module, listener)
    }

    override fun getItemCount(): Int = modules.size


    class MainHolder(private val binding : CardModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(module: ModuleModel, listener: ModuleListener) {
            binding.moduleTitle.text = module.title
            binding.moduleDescription.text = module.description
            binding.moduleCredits.text = module.credits
//            Picasso.get().load(college.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onModuleClick(module) }
        }
    }
}