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
            public void execute(final Project project1) {
                AppExtension appExtension = project1.getExtensions().getByType(AppExtension.class);
                appExtension.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    public void execute(ApplicationVariant applicationVariant) {
                        final String name=applicationVariant.getName();
                        applicationVariant.getOutputs().all(new Action<BaseVariantOutput>() {
                            @Override
                            public void execute(BaseVariantOutput variantOutput) {
                                File outputFile = variantOutput.getOutputFile();
                                System.out.println("jiagu"+name+"-----"+outputFile.getAbsolutePath());
                                //jiaguSurpad_geoRelease-----G:\AllProjects\MyCustomGradlePlugins\app\Surpad_geo\release\Surpad_geo_release.apk
                                project1.getTasks().create("jiagu"+name, AutoJiaguTask.class, outputFile, model);
                            }
                        });
                    }
                });
            }
        });
    }
}
