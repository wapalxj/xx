package com.vero.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.builder.model.SigningConfig
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {

        //-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
        //配置我们自己的插件的扩展
        // 获取:project.extensions.jiagu
        Jiagu jiagu = project.extensions.create("jiagu", Jiagu.class)

        //gradle配置完成之后，解析完build.gradle之后
        project.afterEvaluate {
            AppExtension android = project.extensions.android
            android.applicationVariants.all {
                ApplicationVariant variant ->
                    // 对应变体的(debug/release)的签名配置
                    SigningConfig signingConfig = variant.signingConfig
                    variant.outputs.all {
                        BaseVariantOutput output ->
                            {
                                // 输出的apk文件
                                File apk = output.outputFile

                                // 创建加固任务
                                def task = project.tasks.create("jiagu-${variant.name}", JiaguTask)
                                task.jiagu = jiagu
                                task.signingConfig=signingConfig
                                task.apk=apk

                            }
                    }

            }
        }
    }
}
