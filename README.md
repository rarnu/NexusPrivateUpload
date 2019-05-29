# NexusPrivateUpload

这是一个简易的上传私有库工具2333

使用此工具需要确保你的 Nexus 私库版本为 ```3.x```，不支持 ```2.x``` 的上传。

编译工具:

```shell
$ gradle build
```

在环境变量中增加以下 ```alias```：

```shell
alias upnexus='java -jar path/to/upnexus-1.0.jar'
```

在需要发布到 nexus 的项目中，确保以下内容的存在:

***build.gradle***

```
group 'your group'
version 'your version'
```

***settings.gradle***

```
rootProject.name = 'your project name'
```

***gradle.properties***

```
nexusUploadUrl=http://0.0.0.0:8081
nexusAccount=admin
nexusPassword=admin123
```

```gradle.properties``` 内的内容替换成自己的私库地址，以及有权限上传的帐号密码即可，如果是搭建在本地的私库，默认以上配置即可上传。

- - -

## 发布到私库

```shell
$ cd path/to/project
$ gradle publishToMavenLocal
$ upnexus
```


