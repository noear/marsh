## 对外接口协议（建议草案）

**约定：**

1. 客户端版本约定为三段字符串，且每段限定为1或2位数字。例：1.0.1，2.1.0
2. 客户端版本协议传输时约定为数字，并与应用版本号对应（每个段换1位数字）。例：1.0.1 应对为 101；2.0.0 对应为:200

**请求：**

1. 增加签名头信息（防数据包串改）
2. 增加渠道，每个渠道有自己独立的密钥；由water的访问密钥进行配置
3. 接口名做为路径的一部份
4. 采用body json格式+post方式提交
5. 请求body整体加密
6. 编码使用："UTF-8"

**公共参数：**

1. g_lang        //语言(中文:zh_CN 英文:en_US 日文:ja_JP)
2. g_platform //平台 (1ios 2android 3web )
3. g_deviceId //设备id


**响应：**

1. 响应body整体加密
2. 响应增加body签名输出header[Sign]
3. 协议状态码改为数字（借用http code的设定：200为成功；400xxxx为失败。具体参考协议文档）
4. 编码使用："UTF-8"




##### 格式示例

* 请求格式
```
POST /api/v2/app/config.get   //请求地址外放
HEADER Sign=$sign   //协议签名（会话数据签名，避免串改）
HEADER Token=$token //协议令牌（会话信息）
HEADER Content-type=application/json
BODY ::加密{  //参数，整体加密
  "tag":"water"
}
```

* 响应格式
```json
HEADER Sign=$sign   //协议签名（会话数据签名，避免串改）
HEADER Token=$token //协议令牌（会话信息）
BODY ::加密{
   "code": 200, 
   "description":"",          
   "data": null     //val 或 map 或 list  
}
```

##### 附：协议封装示例(javascript)：

```javascript
var app_id     = "abc138356a624c15b1d1defb7c50ee23";                    //渠道号，由后端分配
var app_secret = "e6eQ1hM2OrOFdfL8";   //渠道密钥（用于加密和签名）

var client_ver_id = 101;             //客户端版本号  1.0.1

//
// 协议调用包装（token 由后端传过来的header[Token]，回传即可）
//
function call(var apiName, var args, var token) {

    let json1 = JSON.stringify(args);
    let json_encoded1 = base64Encode(aesEncrypt(json1, app_secret, "AES/ECB/PKCS5Padding", "utf-8")); //使用aes算法编码


    //生成签名
    let timestamp = new Date().getTime();
    let sign_content = `${apiName}#${client_ver_id}#${json_encoded1}#${app_secret}#${timestamp}`;
    let sign_md5 = md5(sign_content, 'utf-8');
    let sign = `${app_id}.${client_ver_id}.${sign_md5}.${timestamp}`;

    //请求并获取结果
    let response = path("/api/v2.app/" + apiName)
            .header("Token", token)
            .header("Sign", sign)
            .bodyTxt(json_encoded1)
            .post();
            
    let json_encoded2  = response.body().toString();  
    let sign2  = response.header("Sign");
    let sign22 = md5(`${apiName}#${json_encoded2}#${app_secret}`, "utf-8");
    
    //数据签名校验
    if(sign2 != sign22){
        throw "数据已被串改!";
    }
    
    //对结果解码
    let json2 = aesDecrypt(base64Decode(json_encoded2), app_secret, "AES/ECB/PKCS5Padding", "utf-8"); //使用aes算法解码

    return JSON.parse(json2);
}

//
//接口调用包装（基于协议包装的业务包装）
//
function config_get(var tag){
    return call("config.get", {tag:tag} , null);
}

//接口调用示例
config_get("water");

```


**基础状态码：**



| 状态码 | 描述 | 
| -------- | -------- | 
| 200     | 成功     | 
| 400     | 失败，未知错误     | 
| 4001010     | 请求的通道不存在或不再支持     | 
| 4001011     | 请求的接口不存在或不再支持     | 
| 4001012     | 请求的不符合规范     | 
| 4001013     | 请求的签名校验失败     | 
| 4001014     | 请求的参数缺少或有错误     | 
| 4001015     | 请求太频繁了     | 
| 4001016     | 请求不在白名单     | 
| 4001017     | 请求容量超限     | 
| 4001018     | 请求加解密失败     | 
| 4001021     | 登录已失效或未登录     |