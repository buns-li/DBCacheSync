package ${packageName};

import ${baseNodePackageName};
import java.util.List;
import java.util.Map;

/**
*<#if creator == "">@Created by Administrator on ${createTime} <#elseif creater!="">@Created by  ${creater} on ${createTime}</#if>
* 封装参数
* 封装对象
* 封装数据库操作
* 平台封装好数据，远程启动测试
* 写好测试类
*/

public class ${className} extends BaseNode {

    /**
     * 业务点逻辑执行的主入口
     */
    @Override
    public Map<Object,Object> executeNode(){

        System.out.println("the node is -> ${className}");

        /**
        * 判断条件：
        *  1.当返回值User对象不等于null或者内容不为空时，则方法返回nodeLoginUtils.userLoginSuccess
        *  2.当返回值User对象等于null或者内容为空时，则方法返回nodeLoginUtils.userLoginFailure
        *  **************************************************************************************************
        *  **************************************************************************************************
        *  TO Be Do Something!!!
        *  users: 平台执行相关SQL之后,返回的结果集,可以直接用来做逻辑操作使用.
        **/
        /*************************************************************************************************************/

        //TODO something
        //List<Object> listParam = new ArrayList<>();
        //List<Map<String,Object>> list = (List<Map<String,Object>>) this.dbOptions.executeNodeSQL(this.SQLKey,listParam);

        /*************************************************************************************************************/
        /**
         * 逻辑代码编写区域
         */
        <#if !isEmpty><#include logicTemplatePath></#if>
    }
}