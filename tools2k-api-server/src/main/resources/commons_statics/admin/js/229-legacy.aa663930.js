"use strict";(self["webpackChunklib_java_html"]=self["webpackChunklib_java_html"]||[]).push([[229],{99229:function(t,e,i){i.r(e),i.d(e,{default:function(){return f}});var s=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"page-upload"},[i("div",[i("div",{staticClass:"my-pannel pic-pannel"},[i("div",{staticClass:"my-header"},[t._v(" 附件列表 ")]),i("div",t._l(t.list,(function(e,s){return i("div",{key:s,staticClass:"attach_div",on:{click:function(i){return t.fileView(e)}}},[e.row.imageFile?[i("div",{staticClass:"imgDiv"},[i("img",{staticClass:"img-responsive center-block",attrs:{alt:e.row.memo,src:e.fullThumbPath}})]),i("p",[t._v(t._s(e.row.key)+"."+t._s(e.row.extName))]),i("p",[t._v(t._s(e.row.imageWidth)+" X "+t._s(e.row.imageHeight))])]:[i("div",{staticClass:"other-file"},[t._v(t._s(e.row.extName))]),i("p",[t._v("名字:"+t._s(e.row.key)+"."+t._s(e.row.extName))])]],2)})),0)]),i("div",{staticClass:"my-pannel edit-pannel"},[i("div",{staticClass:"p-header"},[t._v(" "+t._s(t.panelTitle)+" ")]),i("div",{staticClass:"p-body"},[i("el-form",{attrs:{labelPosition:"top"},nativeOn:{submit:function(e){return e.preventDefault(),t.onSubmit.apply(null,arguments)}}},[i("h4",[t._v("Key: ")]),i("el-input",{attrs:{required:"",placeholder:"请输入key",readonly:t.editMode},model:{value:t.form.key,callback:function(e){t.$set(t.form,"key",e)},expression:"form.key"}}),t.editMode?i("div",{staticClass:"text-wrap"},[i("small",[t._v("文件绝对路径: ")]),i("small",[i("a",{staticClass:"text-wrap",attrs:{target:"_blank",href:t.fullPath}},[t._v(t._s(t.fullPath))])])]):i("div",[i("small",{staticClass:"text-muted"},[t._v("上传后，key就是文件名")])]),i("h4",[t._v("备注")]),i("el-input",{attrs:{type:"textarea",placeholder:"请输入备注"},model:{value:t.form.memo,callback:function(e){t.$set(t.form,"memo",e)},expression:"form.memo"}}),i("hr"),i("h4",[t._v("请选择附件文件")]),i("input",{attrs:{type:"file"},on:{change:function(e){return t.fileSelectd(e.target.files)}}}),i("div",[t.editMode?i("small",{staticClass:"text-muted"},[t._v("如果选择了新的文件，原来的文件将会被替换")]):i("small",{staticClass:"text-muted"},[t._v("必须选择一个文件上传")])]),i("hr"),i("div",[i("el-button",{staticClass:"w100",attrs:{nativeType:"submit",type:"success"}},[t._v("保存 ")]),t.editMode?i("el-button",{staticClass:"w100",attrs:{type:"danger"},on:{click:t.fileDelete}},[t._v("删除 ")]):t._e(),t.editMode?i("el-button",{staticClass:"w100",attrs:{type:"warning"},on:{click:function(e){return t.switchToNewKey(!0)}}},[t._v("新建 ")]):t._e()],1)],1)],1)])])])},a=[],l=i(77959),o=i(98054),n={components:{},props:{},data:function(){return{list:[],panelTitle:"",editMode:!1,fullPath:"",form:{key:"",file:null,memo:""}}},computed:{},activated:function(){this.reload(),this.switchToNewKey(!0)},methods:{reload:function(){var t=this;o.Z.ajax(l.Z.dictAttachments.list,{},(function(e){t.onListData(e)}))},onListData:function(t){this.list=t.list},fileView:function(t){this.switchToNewKey(!1),this.form.key=t.row.key,this.form.memo=t.row.memo,this.fullPath=t.fullPath},onSubmit:function(){var t=this;this.editMode||null!==this.form.file||o.Z.showErrorMsg("请选择要上传的文件！");var e=new FormData;e.append("key",this.form.key),e.append("memo",this.form.memo),null!==this.form.file&&e.append("file",this.form.file),o.Z.ajax(l.Z.dictAttachments.save,e,(function(e){t.onListData(e),o.Z.showMsg("保存成功")}))},fileSelectd:function(t){var e=t.length;this.form.file=e>0?t[0]:null,console.debug("要上传文件发送了变化",this.form.file)},fileDelete:function(){var t=this;if(this.editMode){var e=this.form.key;o.Z.confirm("确认删除<code>".concat(e,"</code>吗?"),(function(){o.Z.ajax(l.Z.dictAttachments["delete"],{key:e},(function(e){t.onListData(e),t.switchToNewKey(!0),o.Z.showMsg("删除成功")}))}))}},switchToNewKey:function(t){this.editMode=!t,t?(this.form.key="",this.form.memo="",this.panelTitle="新建Key"):this.panelTitle="编辑Key"}}},r=n,c=i(1001),m=(0,c.Z)(r,s,a,!1,null,null,null),f=m.exports}}]);