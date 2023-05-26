package ru.yandex.hrfriend.presentation.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.hrfriend.databinding.ItemTaskBinding
import ru.yandex.hrfriend.domain.models.home.Horizontal
import java.text.SimpleDateFormat
import java.util.Date

class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.MyViewHolder>() {

    private var items : List<Horizontal?> = emptyList()

    inner class MyViewHolder(private val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Horizontal?) {
            //val position = absoluteAdapterPosition
            binding.tvTask.text = item?.param1
            binding.tvTextTask.text = item?.param2

        }
    }

    fun setHorizontals(items: List<Horizontal?>) {
        this.items = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    private fun getDateTime(s: Long?): String? {
        try {
            val sdf = SimpleDateFormat("yyyy-dd-MM")
            val netDate = Date((s)?.times(1000) ?: 100000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}