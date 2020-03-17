package com.yolo.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yolo.annotation.ARouter;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.yolo.annotation.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("myValue")
public class ARouterProcesser extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "没有被@Arouter修饰的类");
            return false;
        }

        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(ARouter.class);

        for (Element element : elementsAnnotatedWith) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String clsName = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "@Arouter修饰：" + packageName + ":" + clsName);
            /**
             * package com.example.helloworld;
             *
             * public final class HelloWorld {
             *   public static void main(String[] args) {
             *     System.out.println("Hello, JavaPoet!");
             *   }
             * }
             */

//            MethodSpec mainMethod = MethodSpec.methodBuilder("main")
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .returns(void.class)
//                    .addParameter(String[].class, "args")
//                    .addStatement("$T.out.println($S)", System.class, "Hello,JavaPoet!")
//                    .build();
//
//            TypeSpec mainCls = TypeSpec.classBuilder("HelloWorld")
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .addMethod(mainMethod)
//                    .build();
//
//            JavaFile jf = JavaFile.builder("com.zj.aptNjavapoet", mainCls)
//                    .build();
//
//            try {
//                jf.writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//                messager.printMessage(Diagnostic.Kind.NOTE,"Javapoet构建失败");
//            }

            /**
             * public class MainActivity$$$$$$$$$ARouter {
             *   public static Class findTargetClass(String path) {
             *     return path.equals("app/MainActivity") ? MainActivity.class : null;
             *   }
             * }
             */

            ARouter aRouterCls = element.getAnnotation(ARouter.class);
            String realClsName = clsName + "$$$ARouter";

            MethodSpec findMethod = MethodSpec.methodBuilder("findTargetClass")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Class.class)
                    .addParameter(String.class, "path")
                    .addStatement("$T.out.println(path)", System.class)
                    .addStatement("return path.equals($S)? MainActivity.class : null", aRouterCls.path())
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(realClsName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(findMethod)
                    .build();

            JavaFile jf = JavaFile.builder("com.zj.aptNjavapoet", typeSpec).build();

            try {
                jf.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.NOTE, "Javapoet构建失败");
            }
        }

        return true;
    }
}
