package ru.yandex.hrfriend.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentChatBinding
import javax.inject.Inject

import ru.yandex.hrfriend.domain.models.Message
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding: FragmentChatBinding

    private val stompViewModel by viewModels<StompViewModel>()

    private lateinit var adapter: ChatAdapter

    private var pagedListLiveData: LiveData<List<Message>>? = null

    private var isFirst = true

    private val messagesBank = ArrayList<Message>()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        initialize()
        binding.imageBack.setOnClickListener { btnBackPressed() }

        binding.btnSend.setOnClickListener { stompViewModel.sendMessage( binding.inputMessage.text.toString().trim()) }

        //observeLiveChat()
        getMessages()
        observeLiveChat()
    }

    private fun observeLiveChat() {
        stompViewModel.liveChatState.observe(requireActivity()){
            binding.inputMessage.text.clear()
            messagesBank.add(it)
            adapter.notifyData()
        }
    }

    private fun getMessages() {
        stompViewModel.initChatState.observe(requireActivity()) {
            messagesBank.addAll(it)
            val username = "${preferencesManager.getString(Constants.USERNAME)} ${preferencesManager.getString(Constants.LASTNAME)}"
            adapter.setChatMessages(messagesBank, username)
            binding.chatRV.visibility = View.VISIBLE;
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun initialize() {

        adapter = ChatAdapter()
        binding.chatRV.adapter = adapter
    }



    private fun btnBackPressed() {
        findNavController().popBackStack()
    }

    private fun <T> Fragment.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ): Job {
        return lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.cancellable().collectLatest(collect)
            }
        }
    }

}