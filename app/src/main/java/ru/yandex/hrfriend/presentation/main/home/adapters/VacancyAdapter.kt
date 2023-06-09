package ru.yandex.hrfriend.presentation.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.hrfriend.data.dto.vacancy.Content
import ru.yandex.hrfriend.databinding.ItemVacancyBinding
import ru.yandex.hrfriend.domain.models.home.Vacancy
import java.text.SimpleDateFormat
import java.util.Date

class VacancyAdapter : RecyclerView.Adapter<VacancyAdapter.MyViewHolder>() {

    lateinit var listener: OnItemClickInterface

    private var items : List<Content?> = emptyList()

    inner class MyViewHolder(private val binding : ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Content?) {
            //val position = absoluteAdapterPosition
            binding.tvPost.text = item?.position?.position// + "(${item?.responsesCount})"
            binding.tvCity.text = item?.location
            binding.tvFirm.text = "Опыт от ${item?.startYearsXP} до ${item?.endYearsXP} лет"
            binding.tvPrice.text = item?.salary
            binding.btnRespond.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION){
                    if (item != null) {
                        listener.onResponseClick(item)
                    }
                }
            }
        }
    }

    fun setVacancies(items: List<Content?>) {
        this.items = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVacancyBinding.inflate(inflater, parent, false)
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

    interface OnItemClickInterface{
        fun onResponseClick(content: Content)
    }

    fun setOnItemClickInterface(listener: OnItemClickInterface){
        this.listener = listener
    }
}