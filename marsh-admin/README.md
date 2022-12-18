
# marsh-admin（统一管理后台开发集成框架）

#### 集成框架

参考 pom.xml 配置内容

#### 特性

* 采用 luna 结构组织模型
* 采用前后不分离模式，避免前后扯皮
* 集成慢SQL自动记录
* 集成登录用户的行为自动记录  
* 集成登录界面
* 集成动态菜单加载与导航框架
* 集成 grit 账号与权限体系
* 集成分页等必要的通用自定义控件  
* 集成 durian ui css 框架（纯 CSS 语义化标签框架）***
* 集成 jtadmin js 框架（含 jquery ）  
* 集成配置服务、事件总线、日志服务、监测服务
* 集成静态内容国际化支持
* 集成solon.boot  
* 支持 gritdock 跨系统整合

#### 要求

* 只需要开发具体内容页
* 使用 wood.table 接口开发 Dao 层，以快速开发
* 视图模型采用统一的 ModelAndView 模型组装并输出

#### 依赖配置

```xml
<project>
    <parent>
        <groupId>org.noear</groupId>
        <artifactId>marsh-parent</artifactId>
        <version>1.2.5</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-admin</artifactId>
        </dependency>
    </dependencies>
</project>
```