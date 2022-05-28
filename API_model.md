一般项目里会用到三种实体：


* Do，数据表对应的实体。一般由Dao层返回（对应目录：model/data）
* Vo，控制器输出时的实体，一般由Controller层返回（对应目录：model/view）
* Dto，跨服务调用时，数据传来传去的实体。比如，使用第三方接口包装时，用到的实体（对应目录：model/transfer）


#### 1、实体字段风格

*  Do 使用与数据表字段name原生对应。使用时，不用属性方式。
* 反例：long userId = userDo.getUser_id() //用起来太难看
* 正例：long userId = userDo.user_id;

```java
public class UserDo implements Serializable{
    public long user_id;
}

UserDo userDo = new UserDo();
userDo.user_id = 12;
```


如果数据表的字段，本就是用"小驼峰式"风格的，那采用下面的风格。（一般数据表字段，都采用：xxx_xxx 风格）

* Dto、Vo。字段名统一用“小驼峰式”风格，且使用属性方式。
    * 正例：long userId = userVo.getUserId();


```java
@Setter
@Getter
public class UserVo implements Serializable{
    private long userId;
}

UserVo userVo = new UserVo();
userVo.setUserId(12);
```



#### 2、实体类的使用原则：

* Dao 层，用 Do 或 List[Do] 输出
* Service 层，用强类型结构输出，不得输出Map之类的弱类型。缓存控制和事务控制也按排在这一层
* Controller 层，用 Vo （强类型） 或者 Map（弱类型）输出