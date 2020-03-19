博客: https://www.cnblogs.com/vipstone/p/9728244.html

使用 mybatis generator 插件说明
1. pom.xml 中要添加对mybatis 的依赖；
2. pom.xml 中要添加mybatis generator 插件的配置；
3. 添加自动生成mybatis 相关数据的配置文件(generatorConfig.xml) 它在pom.xml 中配置mybatis generator 时有指定路径。
4. 需要数据库配置以及mybatis 的相关配置。这个一般使用 application.properties 文件中的。
    不过这个也可以在generatorConfig.xml 中指定。
5. 安装插件: "Free MyBatis plugin"
    我不知道这个插件是必须要安装的，还是说有其他帮助
    file --> settings...
    plugins  --> 搜索  "Free MyBatis plugin"  -->  安装插件   -->  重启IDEA


怎样使用
1. idea 界面
    点击右边的MAVEN  -->  找到对应的项目(demo02-springboot-06-mybatis-generator-01)
    --> 找到mybaties-generator  --> 点击"mybatis-generator:generate"
    即可
2. 终端命令行
    > mvn mybatis-generator:generate




