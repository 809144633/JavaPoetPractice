package Bean;

import com.squareup.javapoet.ClassName;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class ClsBean {
    private ClassName cls;
    private Map<String, Integer> widgetMap;
    private String packageName;
    private String simpleClsName;

    public ClsBean(VariableElement variableElement, Elements elementUtils) {
        packageName = elementUtils.getPackageOf(variableElement).getQualifiedName().toString();
        simpleClsName = ((TypeElement) variableElement.getEnclosingElement()).getSimpleName().toString();
        String fullClsName = ((TypeElement) variableElement.getEnclosingElement()).getQualifiedName().toString();
        cls = ClassName.bestGuess(fullClsName);
    }

    public void put(String kjName, int id) {
        if (widgetMap == null) {
            widgetMap = new HashMap<>();
        }
        widgetMap.put(kjName, id);
    }

    public ClassName getCls() {
        return cls;
    }

    public void setCls(ClassName cls) {
        this.cls = cls;
    }

    public Map<String, Integer> getWidgetMap() {
        return widgetMap;
    }

    public void setWidgetMap(Map<String, Integer> widgetMap) {
        this.widgetMap = widgetMap;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSimpleClsName() {
        return simpleClsName;
    }

    public void setSimpleClsName(String simpleClsName) {
        this.simpleClsName = simpleClsName;
    }
}
