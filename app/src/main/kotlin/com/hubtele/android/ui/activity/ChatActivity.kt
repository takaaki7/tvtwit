package com.hubtele.android.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.hubtele.android.Constants
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.componentbuilder.SceneComponentBuilder
import com.hubtele.android.data.validator.PostValidator
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.manager.ChatManager
import com.hubtele.android.model.Entry
import com.hubtele.android.model.EntryType
import com.hubtele.android.model.Post
import com.hubtele.android.ui.adapter.ChatRecyclerAdapter
import com.hubtele.android.ui.fragment.ReplyDialogFragment
import com.hubtele.android.ui.helper.ChatDecoration
import com.hubtele.android.ui.helper.InputHelper
import com.hubtele.android.ui.view.ChatLayoutManager
import com.hubtele.android.util.PrefUtil
import kotlinx.android.synthetic.main.activity_chat.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by nakama on 2015/10/24.
 */
class ChatActivity : BaseChatActivity(), ReplyDialogFragment.ReplyDialogListener {
    override fun getPageName(): String? = "chat:${program.id}"


    private val layoutManager: LinearLayoutManager = ChatLayoutManager(this, 4F);

    override fun onFinishDialog(post: Post) {
        chatManager.emitEntry(post)
    }

    override val contentViewId: Int = R.layout.activity_chat;
    @Inject lateinit var chatManager: ChatManager;

    private var adapter: ChatRecyclerAdapter = ChatRecyclerAdapter(arrayListOf(), object : ChatRecyclerAdapter.OnEntryClickListener {
        override fun onEntryClick(v: View, entry: Entry) {
            if (!chatManager.isProgramEnd())
                ReplyDialogFragment.
                        newInstance(entry).show(supportFragmentManager, "reply_dialog")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        toolbar.title = program.title
        toolbar.subtitle = DateFormatter.toHmFormat(program.startAt) + "~" + DateFormatter.toHmFormat(program.endAt)
        SceneComponentBuilder.init((application as MyApplication).component).inject(this)
        setupRecyclerView()

        if (!PrefUtil.loadBooleanDefFalse(Constants.PrefKey.CHAT_LAUNCHED)) {
            showSnackBar(recyclerView, "最下部(青い線)までスクロールすると投稿が自動で流れます。", Snackbar.LENGTH_LONG)
            PrefUtil.saveBoolean(Constants.PrefKey.CHAT_LAUNCHED, true)
        }

        commentSubmitButton.setOnClickListener {
            val text = commentEdit.text.toString()

            if (chatManager.isProgramEnd()) {
                finish();
            } else {
                val validateResult = PostValidator.isValid(text)
                if (!validateResult.ok) {
                    showSnackBar(recyclerView, validateResult.errorMessage!!);
                } else {
                    chatManager.emitEntry(Post(commentEdit.text.toString(), null))
                    InputHelper.hideKeyboard(commentEdit);
                    commentEdit.setText("");
                }
            }
        }
        setupNetwork()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(ChatDecoration(this))
        scroller.recyclerView = recyclerView
    }

    private fun setupNetwork() {
        bind(chatManager.getEntryStream().subscribe({
            val isBottom = recyclerView.shouldAutoScroll()
            adapter.add(it)
            if (!it.hasReplies()) adapter.notifyItemInserted(adapter.itemCount - 1) else adapter.notifyDataSetChanged()
            if (isBottom) recyclerView.scrollToBottom()
        }, { error -> Timber.e(error.message) }))

        bind(chatManager.getEventStream().subscribe({
            Timber.d("come event!" + it.eventName)
            when (it.eventName) {
                Constants.Chat.ON_ENTRY_LOG -> {
                    adapter.addAll(it.data as List<Entry>);
                    adapter.notifyDataSetChanged()
                    layoutManager.scrollToPosition((adapter.itemCount ) - 1)
                }
                Constants.Chat.ON_PROGRAM_END -> {
                    adapter.add(Entry(id = "programEnd", content = "この番組は終了しました", userName = "", date = Date(), type = EntryType.U,
                            programId = program.id, imagePath = null, screenName = null, userId = null, replyTo = null, replies = null))
                    adapter.notifyItemInserted(adapter.itemCount - 1)
                    recyclerView.scrollIfNeeded()
                    commentEdit.isEnabled = false
                    commentEdit.hint = "この番組は終了しました"
                    commentSubmitButton.text = "戻る"
                }
            }
        }, { error -> Timber.e(error.message) }))
        chatManager.onCreate(program.id)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        toolbar.onScreenTouched(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatManager.onDestroy()
    }
}