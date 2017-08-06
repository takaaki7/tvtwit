package com.hubtele.android.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ActionMenuView
import android.widget.EditText
import android.widget.Toast
import com.hubtele.android.R
import com.hubtele.android.data.validator.PostValidator
import com.hubtele.android.model.Entry
import com.hubtele.android.model.Post
import com.hubtele.android.ui.activity.ChatActivity
import kotlinx.android.synthetic.main.dialog_reply.*
import kotlinx.android.synthetic.main.dialog_reply.view.*

class ReplyDialogFragment : DialogFragment () {

    companion object {
        fun newInstance(entry: Entry): ReplyDialogFragment {
            val frag = ReplyDialogFragment()
            val arg = Bundle()
            arg.putString("entryId", entry.id)
            arg.putString("userName", entry.userName)
            frag.arguments = arg
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //        dialog.setTitle("jiji")
        return inflater?.inflate(R.layout.dialog_reply, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replyEdit.setText("@${arguments.getString("userName")} ")
        replyEdit.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        replySubmitButton.setOnClickListener { postIt(); }
        replyEdit.setOnEditorActionListener ({ textView, actionId, keyEvent ->
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                postIt()
                true
            } else {
                false
            }
        })
    }

    private fun postIt() {
        val text = replyEdit.text.toString()
        val validateResult = PostValidator.isValid(text)
        if (!validateResult.ok) {
            Toast.makeText(activity, validateResult.errorMessage, Toast.LENGTH_LONG)
        } else {
            (activity as ReplyDialogListener).onFinishDialog(
                    Post(replyEdit.text.toString(), arguments.getString("entryId")))
            this.dismiss()
        }
    }

    interface ReplyDialogListener {
        fun onFinishDialog(post: Post)
    }
}