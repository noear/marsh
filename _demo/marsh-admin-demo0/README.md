此示例采用直接 grit 数据库的模式

#### 1、引入 grit.server

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>grit.server</artifactId>
    <version>${grit.ver}</version>
</dependency>
```

#### 2、添加 grit 配置

```yaml
solon.cloud.water:
  server: "waterapi:9371"
  config:
    load: "grit:grit.yml"
```
