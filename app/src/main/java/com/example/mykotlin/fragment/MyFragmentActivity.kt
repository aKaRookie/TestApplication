package com.example.mykotlin.fragment

import com.example.mykotlin.R
import com.example.mykotlin.base.BaseActivity

/**
 *
 * @ProjectName:    MyKotlinApplication
 * @Package:        com.example.mykotlin
 * @ClassName:      MyFragmentActivity
 * @Description:    java类作用描述
 * @Author:         fenghl
 * @CreateDate:     2020/4/30 16:44
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/4/30 16:44
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MyFragmentActivity : BaseActivity() {

    override fun getResLayoutId(): Int = R.layout.activity_root

    override fun init() {
        intFragment()
    }

    fun intFragment() {
    }

}