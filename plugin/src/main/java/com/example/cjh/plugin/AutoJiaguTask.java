package com.example.cjh.plugin;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;
import org.gradle.wrapper.Install;

import java.io.File;

import javax.inject.Inject;

public class AutoJiaguTask extends DefaultTask {
    private final File mApk;
    private final ConfigModel mConfigModel;

    @Inject
    public AutoJiaguTask(File apk, ConfigModel configModel) {
        setGroup("jiagu");
        this.mApk = apk;
        this.mConfigModel = configModel;
    }

    @TaskAction//双击的task的时候会执行这个方法
    public void jiaguAction() {
        //登陆360加固
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                //具体的命令在 360jiagubao_windows_64\jiagu\help.txt中有
                execSpec.commandLine("java", "-jar", mConfigModel.getJarToolsPath(),
                        "-login", mConfigModel.getUserName(), mConfigModel.getUserPassword());
            }
        });
        //签名
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.commandLine("java", "-jar", mConfigModel.getJarToolsPath(),
                        "-importsign", mConfigModel.getKeyStorePath(),
                        mConfigModel.getKeyStorePassword(), mConfigModel.getKeyAlias(),
                        mConfigModel.getKeyAliasPassword());
            }
        });

        //加固
        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.commandLine("java", "-jar", mConfigModel.getJarToolsPath(),
                        "-jiagu", mApk.getAbsolutePath(), mApk.getParentFile(), "-autosign");
            }
        });

    }




}



