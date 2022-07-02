

## 示例说明：


### 1、基于 marsh-admin 构建的管理后台示例（从简到繁的演化）：

| 示例   | 介绍                                           | 备注                  |
|------|----------------------------------------------|---------------------|
| marsh-admin-demo0 | 只有一个类，连接water环境即可运行；能看到菜单，但没有功能实现            | 有water环境即可运行单元测试 |
| marsh-admin-demo1 | marsh-admin-demo0 基础上，加了1个控制器；对应的功能菜单可操作 | 有water环境即可运行单元测试        |
| marsh-admin-demo2 | marsh-admin-demo1 基础上，增加更多的功能和体系结构            | 有water环境即可运行单元测试        |


如果要本地调试，需添加 "服务发现/上游配置"，tag=grit,service=gritapi,代理网关=gritapi的外网ip端口

### 2、基于 marsh-api 构建的接口服务示例：

| 示例   | 介绍                                    | 备注                       |
|------|---------------------------------------|--------------------------|
| marsh-api-demo0 | 只有一个网关、一个接口                           | 有water环境即可运行单元测试         |
| marsh-api-demo1 | marsh-api-demo0 基础上，增加真接实口实现           | 需 water 添加"访问密钥"和"多语言包"  |
| marsh-api-demo2 | marsh-api-demo1 基础，增加更多的功能、体系结构及业务接口单测 | 需 water 添加"访问密钥"和"多语言包"  |


**为了配合演示：**

* water/配置管理/日志配置，添加：demo/demoapi_log
* water/配置管理/访问密钥，添加: demo/demoapi 应用。并修改单测代码中的 app_key,app_secret_key


一些细节问题，可通过检查和实现完成配置。在过程中不断了解