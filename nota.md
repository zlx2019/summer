# 遇到的问题记录
> 因为大部分组件都是采用最新版本，难免会踩坑,这里简单记录一下.
1. 新版Nacos有了权限控制,由于我是直接下载了新版Nacos,但是用于持久数据的数据库只是更新了下结构,没有数据的补充,导致默认的`Nacos`用户也没有超级权限.所以会不断抛出以下异常
```textmate
login failed: {"code":403,"message":"unknown user!","header":{"header":{"Accept-Charset":"UTF-8","Connection":"keep-alive","Content-Length":"13","Content-Security-Policy":"script-src 'self'","Content-Type":"text/html;charset=UTF-8","Date":"Fri, 17 Feb 2023 13:23:23 GMT","Keep-Alive":"timeout=60","Vary":"Access-Control-Request-Headers"},"originalResponseHeader":{"Connection":["keep-alive"],"Content-Length":["13"],"Content-Security-Policy":["script-src 'self'"],"Content-Type":["text/html;charset=UTF-8"],"Date":["Fri, 17 Feb 2023 13:23:23 GMT"],"Keep-Alive":["timeout=60"],"Vary":["Access-Control-Request-Headers","Access-Control-Request-Method","Origin"]},"charset":"UTF-8"}}
```
2. 之前使用的`net.devh`gRPC与SpringBoot整合包,目前不兼容SpringBoot3.
切换为`io.github.lognet`。

3.千万不要在本地调试rpc接口时开梯子挂全局!!!! /微笑😊