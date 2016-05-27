/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */
package ${packageName};
   
<#list imports as being>  
import ${being};  
</#list>

/**
 * 描述：
 * 类名：${className}
 * 作者：lufengcheng
 * 日期：
 */
public class ${className} extends BaseEntity<${className}> {
   
	private static final long serialVersionUID = 1L;

<#list attributes as being>  
    private ${being.type} ${being.name};  
</#list>   

<#list attributes as being>
    public ${being.type} get${being.name?cap_first}() {  
        return ${being.name};  
    }  
    public void set${being.name?cap_first}(${being.type} ${being.name}) {  
        this.${being.name} = ${being.name};  
    }  
</#list>  

}