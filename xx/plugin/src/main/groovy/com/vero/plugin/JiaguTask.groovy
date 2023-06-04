package com.vero.plugin
import com.android.builder.model.SigningConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JiaguTask extends DefaultTask {

    Jiagu jiagu
    SigningConfig signingConfig
    File apk

    // task分组
    JiaguTask(){
        group="jiagu"
    }
    @TaskAction
    def run() {
        //调用命令行工具

        // 加固
        project.exec {
            // java -jar jiagu.jar -login user password
            it.commandLine("java", "-jar", jiagu.jiaguTools, "-login", jiagu.username, jiagu.password)
        }

        // 证书signingConfig
        if (signingConfig) {//不为null
            project.exec {
                // java -jar jiagu.jar -login user password
                it.commandLine("java", "-jar", jiagu.jiaguTools, "-importsign",
                        signingConfig.storeFile.absolutePath,
                        signingConfig.storePassword,
                        signingConfig.keyAlias,
                        signingConfig.keyPassword

                )
            }
        }

        // 加固apk
        project.exec {
            // java -jar jiagu.jar
            it.commandLine("java", "-jar", jiagu.jiaguTools, "-jiagu",
                    apk.absolutePath,
                    apk.parent,
                    "-autosign"

            )
        }

    }

}
