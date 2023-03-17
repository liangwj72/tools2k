"use strict";(self["webpackChunklib_java_html"]=self["webpackChunklib_java_html"]||[]).push([[727],{85727:function(t,a,e){e.r(a),e.d(a,{default:function(){return d}});var s=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",[e("div",{staticClass:"article-header"},[e("div",[t._v(" "+t._s(this.$route.meta.title)+" ")]),e("auto-refresh",{attrs:{timer:!1,fixed:!1},on:{refresh:t.reload}})],1),e("div",{directives:[{name:"show",rawName:"v-show",value:!t.loading,expression:"!loading"}],staticClass:"main-content"},[e("el-row",{attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("主机信息")])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"名称:"}},[t._v(t._s(t.vm.name))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"启动时间:"}},[t._v(t._s(t._f("timeFormat")(t.vm.startTime))+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"操作系统:"}},[t._v(t._s(t.os.name))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"体系结构:"}},[t._v(t._s(t.os.arch))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"处理器数量:"}},[t._v(t._s(t.os.availableProcessors))])],1)],2)],1),e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("内存信息")])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"总物理内存:"}},[t._v(t._s(t._f("sizeToM")(t.os.totalPhysicalMemorySize))+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"空闲物理内存:"}},[t._v(t._s(t._f("sizeToM")(t.os.freePhysicalMemorySize))+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"总交换空间:"}},[t._v(t._s(t._f("sizeToM")(t.os.totalSwapSpaceSize))+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"提交的虚拟内存:"}},[t._v(t._s(t._f("sizeToM")(t.os.committedVirtualMemorySize))+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"空闲交换空间:"}},[t._v(t._s(t._f("sizeToM")(t.os.freeSwapSpaceSize))+" ")])],1)],2)],1)],1),e("br"),e("el-row",{attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("JRE信息 "),e("small",{staticClass:"text-muted"},[t._v(t._s(t._f("dateFormat")(t.updateTime)))])])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"JRE版本:"}},[t._v(t._s(t.vm.specVersion))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"型号:"}},[t._v(t._s(t.vm.vmName))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"供应商:"}},[t._v(t._s(t.vm.vmVendor))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"虚拟机版本:"}},[t._v(t._s(t.vm.vmVersion))])],1)],2)],1),e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("线程")])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"活动线程:"}},[t._v(t._s(t.threading.threadCount))]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"线程峰值:"}},[t._v(t._s(t.threading.peakThreadCount)+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"守护程序线程:"}},[t._v(t._s(t.threading.daemonThreadCount)+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"累计启动线程数:"}},[t._v(t._s(t.threading.totalStartedThreadCount)+" ")])],1)],2)],1)],1),e("br"),e("el-row",{attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("类加载")])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"当前加载类:"}},[t._v(t._s(t.classLoading.loadedClassCount)+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"累计加载类总数:"}},[t._v(t._s(t.classLoading.totalLoadedClassCount)+" ")]),e("el-form-item",{staticClass:"text-caption",attrs:{label:"已卸载类总数:"}},[t._v(t._s(t.classLoading.unloadedClassCount)+" ")])],1)],2)],1),e("el-col",{attrs:{span:12}},[e("el-card",{staticClass:"small-header"},[e("template",{slot:"header"},[e("div",[t._v("JRE启动参数")])]),e("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[e("el-form-item",{staticClass:"text-caption",attrs:{label:"启动参数:"}},t._l(t.vm.inputArguments,(function(a,s){return e("div",{key:s},[t._v(t._s(a))])})),0)],1)],2)],1)],1)],1)])},l=[],o=e(77959),i=e(98054),r={data:function(){return{loading:!1,updateTime:new Date,classLoading:{loadedClassCount:11296,totalLoadedClassCount:11296,unloadedClassCount:0},os:{arch:"amd64",availableProcessors:8,committedVirtualMemorySize:1016954880,freePhysicalMemorySize:9262739456,freeSwapSpaceSize:9011646464,name:"Windows 10",processCpuLoad:.048939607017920904,processCpuTime:1453125e4,systemCpuLoad:.24856311279013366,totalPhysicalMemorySize:17093902336,totalSwapSpaceSize:19644039168,version:10},threading:{daemonThreadCount:25,peakThreadCount:32,threadCount:32,totalStartedThreadCount:38,actionCount:0,wsUpCount:0,wsUpPayload:0,wsDownPayload:0,sendPacketCount:0,sendPacketPayload:0},vm:{inputArguments:["-agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:6739","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=6738","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-Djava.rmi.server.hostname=localhost","-Dspring.application.admin.enabled=true","-Xverify:none","-XX:TieredStopAtLevel=1","-Dfile.encoding=UTF-8"],name:"4916@LiangWJ-Z7M",specVersion:1.8,startTime:1532263770344,vmName:"Java HotSpot(TM) 64-Bit Server VM",vmVendor:"Oracle Corporation",vmVersion:"25.152-b16"}}},computed:{},activated:function(){this.loading=!0,this.reload(!1)},methods:{reload:function(t){var a=this;i.Z.ajax(o.Z.commonRuntime.osInfo,{},(function(e){a.loading=!1,a.classLoading=e.classLoading,a.os=e.os,a.threading=e.threading,a.vm=e.vm,a.updateTime=new Date,t&&i.Z.showMsg("刷新成功")}))}}},n=r,m=e(1001),c=(0,m.Z)(n,s,l,!1,null,null,null),d=c.exports}}]);