
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/marsh.base.svg)](https://search.maven.org/search?q=org.noear%20marsh)
[![Apache 2.0](https://img.shields.io/:license-Apache2-blue.svg)](https://license.coscl.org.cn/Apache2/)
[![JDK-8+](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![QQ交流群](https://img.shields.io/badge/QQ交流群-22200020-orange)](https://jq.qq.com/?_wv=1027&k=kjB5JNiC)


# Marsh 集成应用开发框架（solon cloud + water）

## 一、marsh-api（统一接口开发集成框架）

#### 集成框架

参考 marsh-api/pom.xml 配置内容

#### 特性

* 采用 solon cloud 框架
* 采用 water 一站式服务治理中台
* 采用 weed3 ORM框架
* 采用集成式网关
* 集成大量常用网关拦截器
* 集成 jwt token
* 集成接口性能、日志自动记录（由 Water 提供支持）
* 集成慢SQL自动记录
* 集成渠道密钥控制能力（由 Water 提供支持）
* 集成状态码自动国际化（由 Water 提供支持）
* 集成配置服务、事件总线、日志服务、监测服务
* 集成静态内容国际化支持
* 集成缓存控制、事务控制
* 集成solon.boot

#### 要求

* 每接口一个文件
* 以开发控制器的形式开发接口
* 使用 xml sql 开发dao（以强调sql透明性和可审核性）
* 采用 service 层进行缓存与事务控制

#### 依赖配置

```xml
<project>
    <parent>
        <groupId>org.noear</groupId>
        <artifactId>marsh-parent</artifactId>
        <version>1.1.0</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-api</artifactId>
        </dependency>
    </dependencies>
</project>
```


## 二、marsh-admin（统一管理后台开发集成框架）

#### 集成框架

参考 marsh-admin/pom.xml 配置内容

#### 特性

* 采用 solon cloud 框架
* 采用 water 一站式服务治理中台
* 采用 grit 权限控制中台
* 采用 weed3 ORM框架
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
* 集成 solon.boot
* 支持 gritdock 跨系统整合

#### 要求

* 只需要开发具体内容页
* 使用 weed3.table 接口开发 Dao 层，以快速开发
* 视图模型采用统一的 ModelAndView 模型组装并输出

#### 依赖配置

```xml
<project>
    <parent>
        <groupId>org.noear</groupId>
        <artifactId>marsh-parent</artifactId>
        <version>1.1.0</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-admin</artifactId>
        </dependency>
    </dependencies>
</project>
```
