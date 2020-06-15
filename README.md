在windwos界面 通过命令cmd 开启一个对话窗口
cd 到project目录

1：
执行gradlew.bat deployNodes
配置corda的模拟节点
2：
执行workflows-kotlin\build\nodes\runnodes.bat
启动节点（大概需要5-10分钟左右节点才能全部准备好）
3：
gradlew.bat runPartyBServer   http://localhost:50006/
gradlew.bat runPartyCServer   http://localhost:50007/
通过骑上命令在任意一个corda节点上启动一个webserver服务
然后通过后面的网址访问网页启动交易等各种相关操作