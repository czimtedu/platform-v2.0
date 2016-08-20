package ${packageName};  
   
<#list imports as being>  
import ${being};  
</#list>  
   
public class ${className} implements Serializable {  
   
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