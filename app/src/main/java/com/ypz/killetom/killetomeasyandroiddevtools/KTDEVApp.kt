package com.ypz.killetom.killetomeasyandroiddevtools

import android.app.Application
import com.ypz.killetom.basedevsdk.tools.android.KTResHelperTools

/**
 *
 * @ProjectName:    KilleTomEasyAndroidDevTools
 * @Package:        com.ypz.killetom.killetomeasyandroiddevtools
 * @ClassName:      KTDEVApp
 * @Description:     java类作用描述
 * @Author:         KilleTom
 * @CreateDate:     2020/5/18 16:31
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/5/18 16:31
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class KTDEVApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KTResHelperTools.init(this)
    }
}