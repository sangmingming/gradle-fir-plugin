#gradle-fir-plugin

a gradle plugin for update apk to fir.im

## 使用方法

### tasks

uploadFir  --上传apk到fir

###build.gradle

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
        classpath 'com.squareup.okhttp:okhttp:2.2.0'
        classpath 'com.squareup.okhttp:okhttp-urlconnection:2.2.0'
        classpath 'org.json:json:20090211'
        classpath 'me.isming:firup:0.4.1'
  }
}

apply plugin: 'me.isming.fir'

fir {
    appId = ""   //app的appid,在fir中可以找到
    userToken = ""  //fir用户的token，也在在fir中找到

    apks {
        release {
            // 要上传的apk的路径,类似下面
            sourceFile  file("/project/main/build/outputs/apk/xxx.apk")
            name ""  //app的名称
            version "3.3.0"  //app的版本version
            build "330"   //app的版本号
            changelog ""  //更新日志
            icon file("....../res/drawable-xxhdpi/icon_logo.png")  //app的icon的路径
        }
    }
}

```


###Run

> $ ./gradlew uploadFir


##其他

你也可以在本任务的基础上，在你的build脚本中增加以下内容:

```groovy
uploadFir.dependsOn assembleRelease  //后面为你生成apk的任务
```

这样就可以在执行上传到fir之前首先会生成一个最新的安装包了

## 关于

本插件基于fir.im官方提供的api文档进行编写，时间匆忙，可能还有一些地方不够完善，还有许多地方可以优化，欢迎star，fork，共同完善。

也可以给我提意见，我来优化。


## 关于我

blog:[blog.isming.me](http://blog.isming.me)

mail: linming1007@gmail.com

weibo: [@码农明明桑](http://weibo.com/mingmingsang)




## License
    Copyright 2015 Isming.me

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
