<h1 align="center" style="text-align:center;">
  Marsh
</h1>

<p align="center">
微服务开发脚手架（solon + water）
</p>

<p align="center">
打出的服务包会很小很小；启动会很快很快。所有微服务架构模式中的能力，只有一个客户端且只用http协议。
</p>

<p align="center">

<a target="_blank" href="https://search.maven.org/search?q=org.noear%20marsh">
    <img src="https://img.shields.io/maven-central/v/org.noear/marsh.base.svg?label=Maven%20Central" alt="Maven" />
</a>
<a target="_blank" href="https://license.coscl.org.cn/Apache2/">
    <img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
</a>
<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
    <img src="https://img.shields.io/badge/JDK-8+-green.svg" alt="jdk-8+" />
</a>
<br />
<a target="_blank" href='https://gitee.com/noear/marsh/stargazers'>
    <img src='https://gitee.com/noear/marsh/badge/star.svg' alt='gitee star'/>
</a>
<a target="_blank" href='https://github.com/noear/marsh/stargazers'>
    <img src="https://img.shields.io/github/stars/noear/marsh.svg?logo=github" alt="github star"/>
</a>
</p>
<br/>
<p align="center">
	<a href="https://jq.qq.com/?_wv=1027&k=6hGHvT1l">
	<img src="https://img.shields.io/badge/QQ交流群-1410383-orange"/></a>
</p>


## 一、marsh-api（接口开发集成框架）

#### 集成框架

参考 marsh-api/pom.xml 配置内容

#### 特性

* 采用 solon 框架
* 采用 water 一站式服务治理中台（完整支持 solon cloud 接口定义）
* 采用 wood ORM框架
* 采用集成式网关
* 集成大量常用网关拦截器
* 集成 jwt token
* 集成接口性能、日志自动记录（由 Water 提供支持）
* 集成慢SQL自动记录（由 Water 提供支持）
* 集成渠道密钥控制能力（由 Water 提供支持）
* 集成状态码自动国际化（由 Water 提供支持）
* 集成配置服务、事件总线、日志服务、监测服务
* 集成静态内容国际化支持（由 Water 提供支持）
* 集成缓存控制、事务控制
* 集成solon.boot

#### 约定

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
        <version>1.3.1-M1</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-api</artifactId>
        </dependency>
    </dependencies>
</project>
```


## 二、marsh-admin（管理后台开发集成框架）

#### 集成框架

参考 marsh-admin/pom.xml 配置内容

#### 特性

* 采用 solon 框架
* 采用 water 一站式服务治理中台（完整支持 solon cloud 接口定义）
* 采用 grit 权限控制中台
* 采用 wood ORM框架
* 采用前后不分离模式，避免前后扯皮
* 集成慢SQL自动记录（由 Water 提供支持）
* 集成登录用户的行为自动记录（由 Water 提供支持）
* 集成登录界面
* 集成动态菜单加载与导航框架
* 集成 grit 账号与权限体系
* 集成分页等必要的通用自定义控件
* 集成 durian ui css 框架（纯 CSS 语义化标签框架）***
* 集成 jtadmin js 框架（含 jquery ）
* 集成配置服务、事件总线、日志服务、监测服务（由 Water 提供支持）
* 集成静态内容国际化支持（由 Water 提供支持）
* 集成 solon.boot
* 支持 gritdock 跨系统整合

#### 约定

* 只需要开发具体内容页
* 使用 wood.table 接口开发 Dao 层，以快速开发
* 视图模型采用统一的 ModelAndView 模型组装并输出

#### 依赖配置

```xml
<project>
    <parent>
        <groupId>org.noear</groupId>
        <artifactId>marsh-parent</artifactId>
        <version>1.3.1-M1</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-admin</artifactId>
        </dependency>
    </dependencies>
</project>
```
