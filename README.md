# HTTPLogService

公司内部的一个项目使用了类似友宝那种贩卖机的安卓设备，这样的安卓设备开发调试比较痛苦。

所以就想到了使用HTTP来显示日志，这样就可以在线看日志了。

代码很简单，用了NanoHTTP来做Web服务器，默认监听10086端口，最多保存2000条日志，可以根据需要自己调整。

### 使用指南
#### 1. 根据项目情况修改package

#### 2. 在build.gradle中加入依赖


    compile 'org.nanohttpd:nanohttpd:2.3.1'


#### 3. 在AndroidManifest.xml中注册服务（注意根据实际情况调整`android:name`）


    <service
        android:name=".util.HTTPLogService"
        android:enabled="true"
        android:exported="true" />


#### 4. 在Application的onCrate中启动Service


    Intent http = new Intent(this, HTTPLogService.class);
    startService(http);


#### 5. 输出Log


    HTTPLogService.log("AppStarted!");

