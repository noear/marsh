此示例采用直接 grit rpc 的模式

* 需要 water 有 gritapi 服务的注册；
* 如果本地调试，还需添加 "服务发现/上游配置"
  * tag=grit
  * service=gritapi
  * 代理网关=gritapi的外网ip端口
