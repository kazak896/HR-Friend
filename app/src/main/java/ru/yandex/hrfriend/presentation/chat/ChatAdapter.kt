package ru.yandex.hrfriend.presentation.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.hrfriend.databinding.ItemContainerReceivedMessageBinding
import ru.yandex.hrfriend.databinding.ItemContainerSentMessageBinding
import ru.yandex.hrfriend.domain.models.Message


class ChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Message> = emptyList()
    private lateinit var currentName: String

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    fun setChatMessages(items: List<Message>, currentName: String) {
        Log.d("FromAdapter", items.toString())
        this.items = items
        this.currentName = currentName
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        Log.d("FromAdapter2", items.toString())
//        items.toMutableList().apply {
//            add(0, message)
//        }
//        notifyItemInserted(0)
        items.toMutableList().apply {
            add(message)
        }
        notifyData()
    }

    fun notifyData() {
        notifyDataSetChanged()
    }

    inner class SentMessageViewHolder(private val binding : ItemContainerSentMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(message: Message) {
            binding.textMessage.text = message.text
            binding.textDateTime.text = message.formattedTime
        }
    }

    inner class ReceiveMessageViewHolder(private val binding : ItemContainerReceivedMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(message: Message) {
            binding.textMessage.text = message.text
            binding.textDateTime.text = message.formattedTime
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).setData(items[position])
        } else {
            (holder as ReceiveMessageViewHolder).setData(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT) {
            return SentMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ReceiveMessageViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].username == currentName) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

}