AntReplaceChannel
=================

##ant多渠道批量打包

* 基于Android sdk自带的tools/ant里的build.xml，实现了替换渠道值或其他多个参数的批量打包功能，修改内容可以查看build.xml的历史记录。
目前只是简单实现基本功能，效率不高，每个包都相当于重新打，没有把不变的内容提取出去。之后再增加修改包名的功能，这样可以在手机上同时安装开发版和正式版，方便对比查看。  
* <http://developer.android.com/sdk>下载的adt-bundle-mac-x86_64-20140702.zip里面已经没有ant插件了，只能去<http://ant.apache.org/>下载安装，终端运行`ant -f build.xml`

