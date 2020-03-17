package com.yolo.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yolo.annotation.BindView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import Bean.ClsBean;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.yolo.annotation.BindView"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BindViewProcesser extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;
    private Filer filer;
    private Map<String, ClsBean> container = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        container.clear();
        if (set.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "没有使用@BindView的类");
            return false;
        }


        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;

            String clsName = variableElement.getEnclosingElement().getSimpleName().toString();
            //1.方法
            messager.printMessage(Diagnostic.Kind.NOTE, "类名：" + clsName);
            ClsBean bean = container.get(clsName);
            if (bean == null) {
                bean = new ClsBean(variableElement, elementUtils);
                container.put(clsName, bean);
            }

            String widgetName = variableElement.getSimpleName().toString();
            BindView bindView = element.getAnnotation(BindView.class);
            int widgetId = bindView.value();
            bean.put(widgetName, widgetId);
        }

        /**
         *example
         *public class  MainActivity_ViewBinding{
         *     public void bind(MainActivity activity){
         *         activity.btn = activity.findViewById(R.id.btn);
         *     }
         * }
         */

        for (String cls : container.keySet()) {
            ClsBean clsBean = container.get(cls);
            //1.方法
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(clsBean.getCls(), "activity");
            Map<String, Integer> map = clsBean.getWidgetMap();
            for (String widgetName : map.keySet()) {
                String s = String.format(
                        Locale.CHINA,
                        "activity.%s=activity.findViewById(%d)",
                        widgetName,
                        map.get(widgetName));
                methodBuilder.addStatement(s);
            }
            //2.类
            TypeSpec type = TypeSpec.classBuilder(clsBean.getSimpleClsName() + "_ViewBinding")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();

            //3.包
            JavaFile javaFile = JavaFile.builder(clsBean.getPackageName(), type).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.NOTE, "Javapoet构建失败");
            }
        }
        return true;
    }
}
