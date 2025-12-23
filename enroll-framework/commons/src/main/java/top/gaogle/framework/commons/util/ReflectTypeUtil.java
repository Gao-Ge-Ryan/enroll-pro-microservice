package top.gaogle.framework.commons.util;

import io.github.classgraph.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 反射以及java类型工具类
 *
 * @author Gaogle
 * @since 1.0.0
 */
public class ReflectTypeUtil {

    /**
     * 扫描指定方法注解
     *
     * @param pkg        扫描包
     * @param annotation 获取的注解类型
     * @return 返回注解参数 [{name:name,value:value}]
     */
    public static List<AnnotationParameterValueList> methodAnnotationScan(String pkg, Annotation annotation) {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()          // Scan classes, methods, fields, annotations
                .acceptPackages(pkg)      // Scan com.xyz and subpackages
                .scan()) {                // Perform the scan and return a ScanResult
            // 获取类里指定方法注解
            ClassInfoList ciList = scanResult.getClassesWithMethodAnnotation(annotation.getClass());
            // 指定方法注解内容提取,提取流程: ClassInfoList -> ClassInfo -> MethodInfo -> AnnotationInfo -> ParameterValues -> AnnotationParameterValue
            return ciList.stream()
                    .flatMap(ci -> ci.getMethodInfo().stream()
                            .filter(me -> me.getAnnotationInfo(annotation.getClass()) != null)
                            .map(me -> me.getAnnotationInfo(annotation.getClass()).getParameterValues()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 扫描存在指定注解的方法
     *
     * @param pkg              扫描包
     * @param methodAnnotation 注解类型
     * @return 存在该注解的方法
     */
    public static List<Method> scanMethodWithAnnotation(Class<? extends Annotation> methodAnnotation, String... pkg) {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()          // Scan classes, methods, fields, annotations
                .acceptPackages(pkg)      // Scan package and subpackages
                .scan()) {                // Perform the scan and return a ScanResult
            // 获取类里指定方法注解
            ClassInfoList ciList = scanResult.getClassesWithMethodAnnotation(methodAnnotation);
            // 指定方法注解内容提取,提取流程: ClassInfoList -> ClassInfo -> MethodInfo -> AnnotationInfo -> ParameterValues -> AnnotationParameterValue
            return ciList.stream()
                    .flatMap(ci -> ci.getMethodInfo().stream()
                            .filter(me -> me.getAnnotationInfo(methodAnnotation) != null)
                            .map(MethodInfo::loadClassAndGetMethod))
                    .collect(Collectors.toList());
        }
    }
}