package com.coding.girl.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.coding.girl.R
import kotlinx.android.synthetic.main.dialog_info.*

/**
 * @author: Coding.He
 * @date: 2020/7/3
 * @emil: 229101253@qq.com
 * @des:
 */
class InfoDialog private constructor() : DialogFragment() {
    private var onItemClickListener: OnItemClickListener? = null

    companion object {
        fun newInstance(args: Bundle): InfoDialog {
            val fragment = InfoDialog()
            fragment.setStyle(STYLE_NORMAL, R.style.InfoDialog)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.setGravity(Gravity.BOTTOM)
        val lp = window!!.attributes!!
        lp.width = WindowManager.LayoutParams.MATCH_PARENT  
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_info, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_store_pic.setOnClickListener {
            onItemClickListener?.click(it)
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun click(view: View)
    }
}