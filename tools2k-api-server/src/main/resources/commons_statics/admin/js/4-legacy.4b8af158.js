(self["webpackChunklib_java_html"]=self["webpackChunklib_java_html"]||[]).push([[4],{16004:function(t,e,a){"use strict";a.r(e),a.d(e,{default:function(){return S}});var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[t.loading?t._e():a("div",[a("el-table",{staticClass:"table-fixed only-header",attrs:{border:""}},[a("div",{attrs:{slot:"empty"},slot:"empty"}),a("el-table-column",{attrs:{label:"ObjectName"}}),a("el-table-column",{attrs:{label:"说明"}}),a("el-table-column",{attrs:{label:"Class"}})],1),t._l(t.list,(function(e,n){return a("div",{key:n},[a("div",{staticClass:"table-caption"},[t._v(" "+t._s(e.name)+" ")]),a("el-table",{staticClass:"table-fixed1",staticStyle:{width:"100%"},attrs:{border:"",showHeader:!1,data:e.beans}},[a("el-table-column",{attrs:{label:"ObjectName"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text"},on:{click:function(a){return t.view(e.row)}}},[t._v(t._s(e.row.displayName))])]}}],null,!0)}),a("el-table-column",{attrs:{prop:"desc",label:"说明"}}),a("el-table-column",{attrs:{prop:"className",label:"Class"}})],1)],1)}))],2),a("mbean-view")],1)},i=[],s=a(77959),o=a(98054),r=a(62222),l=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-drawer",{attrs:{title:"MBean详情:"+t.objectName,visible:t.visible,size:"90%"},on:{"update:visible":function(e){t.visible=e}}},[a("div",{staticClass:"mbean-view"},[a("auto-refresh",{attrs:{timer:!1},on:{refresh:t.reload}}),t.loading?t._e():a("div",[a("attrs-comp",{attrs:{objectName:t.objectName,sendTime:t.sendTime,attrs:t.attrs},on:{refresh:t.reload}}),a("opts-comp",{attrs:{info:t.info},on:{refresh:t.reload}})],1)],1)])},u=[],c=a(77965),d=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"my-pannel attr-pannel"},[a("div",{staticClass:"p-header flex-container"},[a("div",{staticClass:"flex1"},[t._v(" 属性 "),a("span",{staticClass:"text-muted"},[t._v(t._s(t._f("timeFormat")(t.sendTime)))])]),a("div",[a("el-button",{attrs:{size:"mini"},on:{click:t.allColumn}},[t._v(t._s(t.showAllColText))])],1)]),a("div",{staticClass:"p-body"},[a("table",{staticClass:"el-table my-table"},[a("thead",[a("tr",[a("th",{directives:[{name:"show",rawName:"v-show",value:t.showAllCol,expression:"showAllCol"}]},[t._v("名字")]),a("th",{attrs:{width:"150"}},[t._v("说明")]),a("th",[t._v("值")])])]),a("tbody",t._l(t.attrs,(function(e,n){return a("tr",{key:n},[a("td",{directives:[{name:"show",rawName:"v-show",value:t.showAllCol,expression:"showAllCol"}]},[a("span",{staticClass:"text-caption"},[t._v(t._s(e.info.name))]),e.info.writable&&!e.info.readable?a("el-tag",{attrs:{type:"warning",size:"mini"}},[t._v("只写 ")]):t._e(),e.info.writable?t._e():a("el-tag",{attrs:{type:"success",size:"mini"}},[t._v("只读 ")]),a("span",{staticClass:"text-muted",attrs:{align:"right"}},[t._v(t._s(e.info.type))])],1),a("td",[t._v(t._s(e.desc))]),a("td",{staticClass:"attr-value"},[e.info.writable&&e.inputable?a("div",["boolean"==e.info.type||"java.lang.Boolean"==e.info.type?a("div",[t._v(" "+t._s(e._value)+" "),a("el-switch",{attrs:{disabled:!e.editMode,"active-value":"true","inactive-value":"false"},model:{value:e._value,callback:function(a){t.$set(e,"_value",a)},expression:"row._value"}}),a("edit-mode",{on:{reset:function(a){return t.resetEditMode(e)},submitChange:function(a){return t.submitChange(e)}},model:{value:e.editMode,callback:function(a){t.$set(e,"editMode",a)},expression:"row.editMode"}})],1):a("div",[a("div",{staticClass:"flex-container"},[a("div",{staticClass:"flex1"},[e.editMode?a("el-input",{attrs:{type:"textarea",rows:3},model:{value:e._value,callback:function(a){t.$set(e,"_value","string"===typeof a?a.trim():a)},expression:"row._value"}}):a("div",{staticClass:"text-primary",domProps:{innerHTML:t._s(e.value)}})],1),a("div",[a("edit-mode",{on:{reset:function(a){return t.resetEditMode(e)},submitChange:function(a){return t.submitChange(e)}},model:{value:e.editMode,callback:function(a){t.$set(e,"editMode",a)},expression:"row.editMode"}})],1)])])]):a("div",[e.jsonValue?a("pre",{domProps:{innerHTML:t._s(e.value)}}):t._e(),e.jsonValue?t._e():a("span",{staticClass:"text-primary",domProps:{innerHTML:t._s(e.value)}})])])])})),0)])])])},m=[],f=(a(9653),a(68309),function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("span",{staticClass:"edit-btn"},[a("el-button",{directives:[{name:"show",rawName:"v-show",value:!t.editMode,expression:"!editMode"}],attrs:{size:"mini",icon:"el-icon-edit"},on:{click:function(e){return t.switchToEditMode()}}}),a("el-button-group",{directives:[{name:"show",rawName:"v-show",value:t.editMode,expression:"editMode"}]},[a("el-button",{attrs:{size:"mini",type:"info"},on:{click:function(e){return t.resetProp()}}},[a("i",{staticClass:"el-icon-close"})]),a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(e){return t.submitChange()}}},[a("i",{staticClass:"el-icon-check"})])],1)],1)}),p=[],v={name:"edit-mode",components:{},props:{value:{type:Boolean,required:!0,default:!1}},data:function(){return{editMode:!1}},watch:{value:function(t){this.editMode=t}},computed:{},methods:{switchToEditMode:function(){this.updateProp(!0)},updateProp:function(t){this.editMode=t,this.$emit("input",t)},resetProp:function(){this.updateProp(!1),this.$emit("reset")},submitChange:function(){this.editMode=!1,this.$emit("submitChange")}}},h=v,b=a(1001),_=(0,b.Z)(h,f,p,!1,null,null,null),w=_.exports,y={name:"mbean-attrs-comp",components:{EditMode:w},props:{attrs:{type:Array,required:!0},objectName:{type:String,required:!0},sendTime:{type:Number,default:0}},data:function(){return{showAllCol:!1}},computed:{showAllColText:function(){return this.showAllCol?"隐藏名字列":"显示所有列"}},methods:{allColumn:function(){this.showAllCol=!this.showAllCol},resetEditMode:function(t){t._value=t.value},submitChange:function(t){var e=this;t.editMode=!1;var a={name:t.info.name,objectName:this.objectName,value:t._value};o.Z.ajax(s.Z.jmxInWeb.changeAttr,a,(function(){e.$emit("refresh",!1)}))},backToList:function(){this.$router.push("MBeanList")}}},C=y,x=(0,b.Z)(C,d,m,!1,null,null,null),g=x.exports,M=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"my-pannel opt-pannel mbean-info-card"},[a("div",{staticClass:"p-header"},[a("span",{staticClass:"text-caption"},[t._v(t._s(t.info.desc))]),a("code",[t._v(t._s(t.info.className))])]),a("div",{staticClass:"p-body"},[a("div",{staticClass:"opt-nav"},[a("ul",t._l(t.info.opts,(function(e){return a("li",{key:e.info.name,staticClass:"text-link",attrs:{index:e.info.name},on:{click:function(a){return t.onNavClick(e)}}},[a("div",{staticClass:"text-wrap",staticStyle:{width:"209px"}},[t._v(" "+t._s(e.info.name)+" "),a("span",{staticClass:"text-muted"},[t._v(t._s(e.info.description))])])])})),0)]),t.showForm?a("div",{staticClass:"opt-forms article"},[a("h4",[a("span",{staticClass:"label label-success"},[t._v(t._s(t.curOpt.info.returnType))]),a("span",{staticClass:"text-caption"},[t._v(t._s(t.curOpt.info.name))])]),a("div",{staticClass:"text-muted"},[t._v(" 备注: "+t._s(t.curOpt.info.description)+" ")]),a("hr"),a("el-form",{attrs:{"label-width":"120px"},nativeOn:{submit:function(e){return e.preventDefault(),t.onSubmitOpt.apply(null,arguments)}}},[t._l(t.curOpt.params,(function(e){return a("el-form-item",{key:e.info.name,attrs:{label:e.info.name}},["boolean"===e.info.type?a("div",[a("el-switch",{attrs:{"active-value":"true","inactive-value":"false"},model:{value:e.defaultValue,callback:function(a){t.$set(e,"defaultValue",a)},expression:"param.defaultValue"}})],1):a("div",[a("el-input",{attrs:{size:"small",type:"textarea",clearable:""},model:{value:e.defaultValue,callback:function(a){t.$set(e,"defaultValue","string"===typeof a?a.trim():a)},expression:"param.defaultValue"}})],1),a("div",[a("span",{staticClass:"label label-success"},[t._v(t._s(e.info.type))]),a("span",{staticClass:"text-muted"},[t._v(" "+t._s(e.info.description))])])])})),a("el-button",{attrs:{type:"primary",nativeType:"submit"}},[t._v("执行 ")]),a("div",{directives:[{name:"show",rawName:"v-show",value:t.returnData.show,expression:"returnData.show"}]},[a("br"),a("div",{staticClass:"my-header"},[a("span",[t._v("执行返回结果")])]),a("pre",[t._v(t._s(t.returnData.data)+"\n          ")])])],2)],1):t._e()])])},j=[],N={name:"opts-comp",props:{info:{type:Object,required:!0}},data:function(){return{showForm:!1,curOpt:{},returnData:{hasReturn:!1,data:"",show:!1}}},computed:{},activated:function(){this.showForm=!1,this.curOpt={}},methods:{onNavClick:function(t){this.showForm=!0,this.curOpt=t,console.debug("-------",this.curOpt)},onSubmitOpt:function(){var t,e=this,a={objectName:this.info.objectName,optName:this.curOpt.info.name,paramType:[],paramValue:[]},n=(0,c.Z)(this.curOpt.params);try{for(n.s();!(t=n.n()).done;){var i=t.value;a.paramType.push(i.info.type),a.paramValue.push(i.defaultValue)}}catch(r){n.e(r)}finally{n.f()}console.debug("执行mbean 方法",a),this.returnData.hasReturn=!1,this.returnData.data="",o.Z.ajax(s.Z.jmxInWeb.invokeOpt,a,(function(t){o.Z.showMsg("执行 ".concat(a.optName," 成功")),e.returnData.hasReturn=t.hasReturn,e.returnData.data=t.returnData,e.returnData.show=t.hasReturn,e.$emit("refresh",!1)}))}}},k=N,A=(0,b.Z)(k,M,j,!1,null,null,null),$=A.exports,T={name:"mbean-detail",components:{OptsComp:$,AttrsComp:g},props:{},mounted:function(){var t=this;this.$eventBus.$off(r.Z.eventName.showMBeanDetail),this.$eventBus.$on(r.Z.eventName.showMBeanDetail,(function(e){var a=e.objectName;console.debug("showMBeanDetail 事件: objectName=".concat(a)),t.visible=!0,t.loading=!0,t.objectName=a,t.reload(!1)}))},data:function(){return{visible:!1,showAllCol:!1,loading:!1,attrs:[],sendTime:0,objectName:"",info:{}}},computed:{showAllColText:function(){return this.showAllCol?"隐藏名字列":"显示所有列"}},methods:{reload:function(t){var e=this;console.debug("刷新 mbean:",this.objectName);var a={objectName:this.objectName};o.Z.ajax(s.Z.jmxInWeb.getMBeanInfo,a,(function(a){e.onDateLoad(a.info),e.sendTime=a.sendTime,e.loading=!1,t&&o.Z.showMsg("刷新成功")}))},onDateLoad:function(t){var e,a=(0,c.Z)(t.attrs);try{for(a.s();!(e=a.n()).done;){var n=e.value;this.addEditModeData(n)}}catch(i){a.e(i)}finally{a.f()}this.attrs=t.attrs,this.objectName=t.objectName,this.info=t},addEditModeData:function(t){t.editMode=!1,t._value=t.value}}},Z=T,O=(0,b.Z)(Z,l,u,!1,null,null,null),D=O.exports,B={components:{MbeanView:D},data:function(){return{list:[],loading:!1}},computed:{},activated:function(){this.reloadList()},methods:{reloadList:function(){console.debug("加载MBean列表"),this.loading=!0;var t=this;o.Z.ajax(s.Z.jmxInWeb.getMBeanList,{},(function(e){t.list=e.list,t.loading=!1}))},view:function(t){console.debug("点击查看MBean详情 , objname=".concat(t.objectName)),r.Z.showMBeanDetail(t.objectName)}}},E=B,V=(0,b.Z)(E,n,i,!1,null,null,null),S=V.exports},91038:function(t,e,a){var n=a(82109),i=a(48457),s=a(17072),o=!s((function(t){Array.from(t)}));n({target:"Array",stat:!0,forced:o},{from:i})},77965:function(t,e,a){"use strict";a.d(e,{Z:function(){return s}});a(82526),a(41817),a(41539),a(32165),a(78783),a(33948),a(21703),a(47042),a(68309),a(91038),a(74916),a(77601);function n(t,e){(null==e||e>t.length)&&(e=t.length);for(var a=0,n=new Array(e);a<e;a++)n[a]=t[a];return n}function i(t,e){if(t){if("string"===typeof t)return n(t,e);var a=Object.prototype.toString.call(t).slice(8,-1);return"Object"===a&&t.constructor&&(a=t.constructor.name),"Map"===a||"Set"===a?Array.from(t):"Arguments"===a||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(a)?n(t,e):void 0}}function s(t,e){var a="undefined"!==typeof Symbol&&t[Symbol.iterator]||t["@@iterator"];if(!a){if(Array.isArray(t)||(a=i(t))||e&&t&&"number"===typeof t.length){a&&(t=a);var n=0,s=function(){};return{s:s,n:function(){return n>=t.length?{done:!0}:{done:!1,value:t[n++]}},e:function(t){throw t},f:s}}throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}var o,r=!0,l=!1;return{s:function(){a=a.call(t)},n:function(){var t=a.next();return r=t.done,t},e:function(t){l=!0,o=t},f:function(){try{r||null==a["return"]||a["return"]()}finally{if(l)throw o}}}}}}]);