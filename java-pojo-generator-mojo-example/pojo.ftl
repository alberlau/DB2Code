package ${package};

@SuppressWarnings({"PMD.DataClass"})
public class ${className} {

<#list properties as property>
    private ${property.propertyType} ${property.propertyName};
</#list>

<#list properties as property>
    public ${property.propertyType} get${property.methodName}() {
    return this.${property.propertyName};
    }

    public void set${property.methodName}(${property.propertyType} ${property.propertyName}) {
    this.${property.propertyName} = ${property.propertyName};
    }
</#list>

}
