# Summer
## 架构介绍
基于SpringBoot3,整合一些常用的开发框架、组件.



## 技术栈
#### 基层架构
    SpringBoot3
    SpringCloud 2022
    SpringCloudAlibaba2022.0.0.0-RC1.
    MybatisPlus
    Dynamic
    SpringData
    SpringCloudGateway
    OpenFeign
    RSocket
    gRPC
    Sentinel
    Nacos
    Redis
    ElasticSearch
    RoketMQ
    Etcd
    Mysql
#### 
    
#### 项目结构
```text
├── README.md
├── bin
├── summer-commons
│   ├── summer-auth-spring-boot-starter
│   ├── summer-cache-spring-boot-starter
│   ├── summer-core-spring-boot-starter
│   ├── summer-db-spring-boot-starter
│   ├── summer-lock-spring-boot-starter
│   ├── summer-log-spring-boot-starter
│   └── summer-rpc-spring-boot-starter
│       ├── summer-feign-spring-boot-starter
│       ├── summer-rsocket-spring-boot-starter
│       └── summer-webclient-spring-boot-starter
├── summer-config
├── summer-doc
├── summer-gateway
│   └── summer-spring-cloud-gateway
└── summer-services
    ├── summer-example
    └── summer-user

```