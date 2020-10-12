package com.example.cjh.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.BaseVariantOutput;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;


public class AutoJiaguPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        /**
         * jiagu 就是app build.gradle里面jiagu，相当于android，就是外面引用的名字
         * ConfigModel 是一个包含了配置信息的类
         * 这样我们就可以得到app中的配置信息
         */
        final ConfigModel model = project.getExtensions().create("jiagu", ConfigModel.class);
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(final Project project) {
                /**
                 * 下列操作是为了获取我们需要加固的apk的文件
                 */
                AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
                appExtension.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    public void execute(ApplicationVariant applicationVariant) {
                        //name=debug或者release
                        final String name=applicationVariant.getName();
                        applicationVariant.getOutputs().all(new Action<BaseVariantOutput>() {
                            @Override
                            public void execute(BaseVariantOutput variantOutput) {
                                //得到apk文件--E:\AndroidStudioAllProjects\MyCustomGradlePlugins\app\build\outputs\apk\debug\app-debug.apk
                                File outputFile = variantOutput.getOutputFile();
                                project.getTasks().create("jiagu"+name, AutoJiaguTask.class, outputFile, model);
                            }
                        });
                    }
                });
            }
        });
    }
}
