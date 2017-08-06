package com.hubtele.android.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hubtele.android.R

/**
 * Created by nakama on 2015/12/10.
 */
class ReviewDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(): ReviewDialogFragment {
            val frag = ReviewDialogFragment();
            return frag;
        }
    }

//    override fun onCreateView(inflater: LayoutInflater?,container: ViewGroup?,savedInstanceState: Bundle?): View?{
//        return inflater?.inflate(R.layout.dialog_review,container);
//    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}