#### 1.7.2
* solon 升为 2.8.0
* grit 升为 1.8.4
* water 升为 2.13.2

#### 1.7.0
* solon 升为 2.7.0
* grit 升为 1.8.0
* water 升为 2.13.0

#### 1.6.1
* solon 升为 2.6.6
* wood 升为 1.2.6
* snack3 升为 3.2.88
* grit 升为 1.7.1
* water 升为 2.12.1

#### 1.6.0
* 升级 solon 为 2.6.0

#### 1.5.4
* 升级 solon 为 2.5.10

#### 1.5.3
* 升级 solon 为 2.5.7
* 升级 water 为 2.11.4
* 升级 grit 为 1.6.6
* 升级 wood 为 1.2.2
* 调整视图目录为 `resources/WEB-INF/templates/`

#### 1.5.2
* 升级 solon 为 2.5.4
* 升级 water 为 2.11.3
* 升级 grit 为 1.6.5
* 升级 snack3 为 3.2.80

#### 1.5.1
* 升级 snack3 为 3.2.76

#### 1.5.0
* 升级 solon 为 2.4.2 (CloudMetricService 调整过)
* 升级 grit 为 1.6.3
* 升级 snack3 为 3.2.75
* 升级 wood 为 1.1.8

#### 1.4.2
* 升级 solon 为 2.3.8
* 升级 grit 为 1.6.2

#### 1.4.1
* 升级 solon 为 2.3.6

#### 1.4.0
* 升级 slf4j 为 2.0.7 （重要变化）
* 升级 solon 为 2.3.0

#### 1.3.14
* 升级 solon 为 2.2.17
* 升级 grit 为 1.5.4


#### 1.3.13
* 升级 solon 为 2.2.16
* 升级 snack3 为 3.2.66
* 升级 wood 为 1.1.1
* 优化日志，开始计时就增加 MDC.tag0 

#### 1.3.10
* 升级 solon 为 2.2.11
* 升级 snack3 为 3.2.65
* 升级 redisx 为 1.4.7

#### 1.3.9
* 修复不能注入 List<List<Long>> 这种多重嵌套的泛型
* 升级 solon 为 2.2.9
* 升级 snack3 为 3.2.64
* 升级 water 为 2.10.3

#### 1.3.8
* 增加常量 Attrs::g_region, Attrs::h_authorization

#### 1.3.7
* WaterAppImpl 增加 app_group_id 和 user_group_id 支持
* 升级 solon 为 2.2.6
* 升级 snack3 为 3.2.61
* 升级 water 为 2.10.2

#### 1.3.6
* 升级 solon 为 2.2.4
* 升级 snack3 为 3.2.60

#### 1.3.4
* 升级 solon 为 2.2.1
* 升级 snack3 为 3.2.56
* 升级 water 为 2.10.1

#### 1.3.3
* 升级 solon 为 2.1.4

#### 1.3.0
* 升级 solon 为 2.0.0
* 升级 snack3 为 3.2.53
* 升级 water 为 2.10.0
* 升级 rock 为 2.6.0
* 升级 grit 为 1.5.0

#### 1.2.7
* 升级 solon 为 1.12.2 //修复 water-solon-cloud-plugin 1.11.5+
* 升级 water 为 2.9.3
* 升级 rock 为 2.5.4
* 升级 grit 为 1.4.4

#### 1.2.6
* 升级 solon 为 1.12.1 //有问题，water-solon-cloud-plugin 1.11.5+

#### 1.2.5
* 升级 solon 为 1.11.6 //有问题，water-solon-cloud-plugin 1.11.5+
* 升级 water 为 2.9.2
* 升级 rock 为 2.5.3
* 升级 snack3 为 3.2.50
* 升级 wood 为 1.0.7

#### 1.2.4
* 升级 solon 为 1.11.3
* 取消 water-solon-cloud-plugin 的直接依赖
* 支持 cloud 与 local 切换。即排除 water-solon-cloud-plugin，切换为 local-solon-cloud-plugin
* 添加 ParamsRebuildNoAppHandler 类，用于无 App 的网关
* 调整 ParamsLocaleHandler 类，增加默认 Locale 设定

#### 1.2.3
* 升级 solon 为 1.10.13
* 升级 grit 为 1.4.2
* 升级 rock  为 2.5.2
* 升级 snack 为 3.2.46
* 升级 water 为 2.9.1

#### 1.2.2
* 升级 solon 为 1.10.11
* 升级 grit 为 1.4.1
* 升级 rock  为 2.5.1
* 升级 snack 为 3.2.45

#### 1.2.1
* 升级 solon 为 1.10.9

#### 1.2.0
* 升级 solon 为 1.10.7
* 升级 grit 为 1.4.0
* 升级 rock  为 2.5.0
* 升级 snack 为 3.2.44
* 升级 water 为 2.9.0（需要 water server 2.8.0+ 支持）
* 迁移 weed3 到 wood 1.0.1（这个是重要变更）

#### 1.1.5
* 升级 grit 为 1.3.5
* 升级 rock  为 2.4.1

#### 1.1.4
* 升级 solon 为 1.10.5
* 升级 grit 为 1.3.4

#### 1.1.3
* 升级 solon 为 1.10.2

#### 1.1.2
* 升级 solon 为 1.10.0
* 升级 water 为 2.8.2（需要 water server 2.8.0+ 支持）

#### 1.1.1
* 升级 solon 为 1.9.3
* 升级 water 为 2.8.1（需要 water server 2.8.0+ 支持）

#### 1.1.0
* 升级 solon 为 1.9.1
* 升级 water 为 2.8.0（需要 water server 2.8.0 支持）
* 升级 snack 为 3.2.31
* 升级 weed3 为 3.4.27

#### 1.0.2
* 升级 solon 为 1.9.0
* 升级 weed3 为 3.4.27

#### 1.0.2
* 升级 solon 为 1.8.3
* 升级 water 为 2.7.2
* 升级 snack 为 3.2.29
* 升级 weed3 为 3.4.26

#### 1.0.1
* solon 升为：1.8.2
* water 升为：2.7.1
* grit 升为 1.2.1
* IApp 提供者由 IAppFactory 进行切换（以前由Rock固定提供）