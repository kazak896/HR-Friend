package ru.yandex.hrfriend.presentation.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import ru.yandex.hrfriend.databinding.ItemTaskBinding
import ru.yandex.hrfriend.domain.models.home.Horizontal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.MyViewHolder>() {

    private var items : List<ResumeResponseResponse?> = emptyList()

    inner class MyViewHolder(private val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResumeResponseResponse?) {
            //val position = absoluteAdapterPosition
            binding.tvTask.text = getDateTime(item?.time)
            binding.tvTextTask.text = item?.vacancy?.position?.position
            binding.btnAddNote.text = item?.status
        }
    }

    fun setHorizontals(items: List<ResumeResponseResponse?>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addItem(response: ResumeResponseResponse) {
        items.toMutableList().add(response)
        notifyItemInserted(items.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    private fun getDateTime(s: String?): String? {
        try {
            s?.let {
                val parsedDate = LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME)
                return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
            return ""
        } catch (e: Exception) {
            return e.toString()
        }
    }
}