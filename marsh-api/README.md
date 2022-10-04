
# marsh-api（统一接口开发集成框架）

#### 集成框架

参考 pom.xml 配置内容

#### 特性

* 采用 luna 结构组织模型
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
        <version>1.1.5</version>
    </parent>
    
    <dependencies>
        <dependency>
            <groupId>org.noear</groupId>
            <artifactId>marsh-api</artifactId>
        </dependency>
    </dependencies>
</project>
```