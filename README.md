AntReplaceChannel
=================

##ant多渠道批量打包

* 打包方法：找到项目里的build.xml，右键菜单里选择“run”，使用eclipse自带的ant插件运行。但是<http://developer.android.com/sdk>下载的最新的开发环境adt-bundle-mac-x86_64-20140702.zip里面已经没有ant插件了，只能去<http://ant.apache.org/>下载安装ant完整版安装到电脑上，终端运行`ant -f build_new.xml`
* build.xml以Android sdk自带的tools/ant里的build.xml为基础进行了修改，实现了替换渠道值或其他多个参数的的批量打包功能，目前基于的版本是`Android SDK Tools Revision 23.0.2`，其他很老的版本可能修改的内容有一点不一致，可升级sdk或根据当前的build.xml修改。目前只是简单实现基本功能，效率和兼容性不够，之后再优化。

修改内容为3处：

1.文件开头。配置项目打包需要的基本信息


    <project name="AntReplaceChannel" default="deployableAllSrc">
		<!-- 是否使用签名 -->
		<property name="has.keystore" value="true" />
		<!-- 签名密码 -->
		<property name="has.password" value="true" />
		<!--签名相关的key -->
		<property name="key.store" value="key" />
		<property name="key.alias" value="AntReplaceChannel" />
		<!-- 签名相关的密码 -->
		<property name="key.store.password" value="123456" />
		<property name="key.alias.password" value="123456" />
		
		<property file="project.properties" />
	
		<!-- sdk的路径 -->
		<property name="sdk.dir" value="/Users/XXX/android/adt-bundle-mac-x86_64_old/sdk"/>

2.中间部分，修改target release。


		<target name="release"
                depends="clean, replace_src, -set-release-mode, -release-obfuscation-check, -package, -post-package, -release-prompt-for-password, -release-nosign, -release-sign, -post-build, restore_manifest"
                description="Builds the application in release mode.">
                <!-- only create apk if *not* a library project -->
        <do-only-if-not-library elseText="Library project: do not create apk..." >
            <sequential>
                <property name="out.unaligned.file" location="${out.absolute.dir}/${ant.project.name}-release-unaligned.apk" />

                <!-- Signs the APK -->
                <echo>Signing final apk...</echo>
                <signjar
                        jar="${out.packaged.file}"
                        signedjar="${out.unaligned.file}"
                        keystore="${key.store}"
                        storepass="${key.store.password}"
                        alias="${key.alias}"
                        keypass="${key.alias.password}"
                        verbose="${verbose}" />

                <!-- Zip aligns the APK -->
                <zipalign-helper in.package="${out.unaligned.file}"
                                           out.package="${out.final.file}" />
                <echo>Release Package: ${out.final.file}</echo>
                
                <xpath input="AndroidManifest.xml" expression="/manifest/@android:versionName" output="build.versionName" default="0.1" />
   						 	<copy file ="${out.final.file}" tofile = "release/AntReplaceChannel_${build.versionName}_${src}.apk" overwrite = "true"/>
            </sequential>
        </do-only-if-not-library>
        <record-build-info />
    </target>
		
3.文件结尾，项目的渠道值位于AndroidManifest.xml里，所以打包前将原文件复制到临时目录，然后修改成要打包的渠道值，打包完成后还原。

    <target name="replace_src">
		<copy file ="AndroidManifest.xml" todir = "temp"/>
		<replace file ="AndroidManifest.xml" token="CHANNEL_VALUE" value="${src}" encoding="utf-8"/>
		<replace file ="AndroidManifest.xml" token="VALUE" value="${value}" encoding="utf-8"/>
	</target>
		
	<target name="restore_manifest">
		<copy file ="temp\AndroidManifest.xml" todir = "" overwrite = "true"/>
	</target>
	
	<target name="deployableAllSrc" description="build all device packet">
	    <!-- value值不能是纯数字 -->
		<antcall target="release" inheritAll="true"><param name="src" value="360sc" /><param name="value" value="_12" /></antcall><!--奇虎360  -->
		<antcall target="release" inheritAll="true"><param name="src" value="anzhuo" /><param name="value" value="_34" /></antcall><!--安卓市场  -->
		<antcall target="release" inheritAll="true"><param name="src" value="91sc" /><param name="value" value="_56" /></antcall><!-- 91 -->
		<antcall target="release" inheritAll="true"><param name="src" value="wdj" /><param name="value" value="_78" /></antcall><!-- 豌豆荚 -->
		<antcall target="release" inheritAll="true"><param name="src" value="xiaomi" /><param name="value" value="_90" /></antcall><!-- 小米 -->

		<antcall target="clean" inheritAll="true"/>
	</target>
