# AdviceToAdvisor

辅导员评议系统，基于 SpringBoot 开发。

## 安装与使用

0. 所需环境为 JDK8 + MySQL8 + Maven

1. 在 MySQL 中创建一个名为 ata 的数据库。如果你觉得「ata」太难听，也可以起自己喜欢的名字，一会儿修改配置文件即可。

2. 使用 Maven 导包。

3. 修改 `/src/main/application.properties`。

```
# 运行端口
server.port=80

# MySQL 用户名和密码
spring.datasource.username=root
spring.datasource.password=root

# 如果第一步创建的数据库名不是「ata」可以在此修改
spring.datasource.url=jdbc:mysql://localhost:3306/ata?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true&allowMultiQueries=true

# 访问 http://ip:port/druid 即可进入数据源控制面板
spring.datasource.druid.stat-view-servlet.login-username=admin
spring.datasource.druid.stat-view-servlet.login-password=123456
```

4. 访问 `http://ip:port` 即可看到安装页面，在此页面设置管理员密码。

5. `http://ip:port/admin` 是管理员面板，依次添加学院账号、辅导员、问卷题目，学院账号也是由此链接登录。

6. 安装成功后访问 `http://ip:port` 即可看到问卷页面，默认需要使用微信浏览器打开。如果需要取消这一限制，修改 `src/main/resources/templates/index.html` 中的 Javascript 代码即可。
