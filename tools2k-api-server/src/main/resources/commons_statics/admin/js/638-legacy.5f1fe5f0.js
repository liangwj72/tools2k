"use strict";(self["webpackChunklib_java_html"]=self["webpackChunklib_java_html"]||[]).push([[638],{53638:function(t,e,a){a.r(e),a.d(e,{default:function(){return u}});var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("div",{staticClass:"article-header"},[a("div",[t._v(" "+t._s(this.$route.meta.title)+" ")])]),a("el-card",{staticClass:"small-header"},[a("template",{slot:"header"},[a("div",[t._v("数据库连接池基础信息")])]),a("el-form",{staticClass:"info-form",attrs:{labelPosition:"left","label-width":"120px"}},[a("el-form-item",{staticClass:"text-caption",attrs:{label:"版本:"}},[t._v(t._s(t.info.Version))]),a("el-form-item",{staticClass:"text-caption",attrs:{label:"是否运行重置:"}},[t._v(t._s(t.info.ResetEnable))]),a("el-form-item",{staticClass:"text-caption",attrs:{label:"重置次数:"}},[t._v(t._s(t.info.ResetCount))]),a("el-form-item",{staticClass:"text-caption",attrs:{label:"驱动:"}},t._l(t.info.Drivers,(function(e,i){return a("div",{key:i},[t._v(" "+t._s(e)+" ")])})),0)],1)],2)],1)},s=[],n=a(77959),l=a(17190),o={components:{},props:{},data:function(){return{info:{Version:"1.2.3",Drivers:[],ResetEnable:!0,ResetCount:0,JavaVMName:"Java HotSpot(TM) 64-Bit Server VM",JavaVersion:"11.0.6"}}},computed:{},mounted:function(){},activated:function(){this.reload()},methods:{reload:function(){var t=this;l.Z.ajax.post(n.Z.commonDurid.getBasic,{}).then((function(e){console.debug("获取了Druid连接池的基础信息",e.info),t.info=e.info}))}}},r=o,c=a(1001),f=(0,c.Z)(r,i,s,!1,null,null,null),u=f.exports}}]);