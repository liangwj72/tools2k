"use strict";(self["webpackChunklib_java_html"]=self["webpackChunklib_java_html"]||[]).push([[303],{33303:function(e,t,o){o.r(t),o.d(t,{default:function(){return p}});var a=function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("el-container",{staticClass:"main-frame"},[o("el-header",{staticClass:"main-frame-header tc-header",attrs:{height:"60px"}},[o("div",{staticClass:"tc-title"},[o("my-key",{attrs:{vkey:"system.name"}})],1)]),o("el-container",[o("el-main",[o("el-row",{attrs:{gutter:20}},[o("el-col",{attrs:{span:10}},[o("el-card",[o("div",{attrs:{slot:"header"},slot:"header"},[o("span",[e._v("请登录")])]),o("el-form",{attrs:{"label-width":"100px",size:"mini"},nativeOn:{submit:function(t){return t.preventDefault(),e.onSubmit.apply(null,arguments)}}},[o("el-form-item",{attrs:{label:"账号",prop:"checkPass"}},[o("el-input",{attrs:{type:"text",required:"",placeholder:"请输入账号"},model:{value:e.form.account,callback:function(t){e.$set(e.form,"account","string"===typeof t?t.trim():t)},expression:"form.account"}},[e._v(' auto-complete="on"> ')])],1),o("el-form-item",{attrs:{label:"密码"}},[o("el-input",{attrs:{type:"password",required:"",placeholder:"请输入密码","auto-complete":"off"},model:{value:e.form.password,callback:function(t){e.$set(e.form,"password","string"===typeof t?t.trim():t)},expression:"form.password"}})],1),o("el-form-item",[o("el-checkbox",{attrs:{label:"保持登录"},model:{value:e.form.rememberMe,callback:function(t){e.$set(e.form,"rememberMe",t)},expression:"form.rememberMe"}})],1),o("el-form-item",[o("el-button",{attrs:{type:"primary",nativeType:"submit"}},[e._v("登录")])],1)],1)],1)],1),o("el-col",{attrs:{span:14}},[e.adminInProp?o("div",{staticClass:"article"},[o("div",[o("h4",[e._v("账号配置:")]),o("p",{staticClass:"color-text-secondary"},[e._v(" 账号和密码存储在spring boot的配置文件中， ")]),o("ul",[o("li",[e._v("login-check.admin.account=管理员账号")]),o("li",[e._v("login-check.admin.password=密码")])]),o("p",{staticClass:"text-muted"},[e._v(" 如果不想把账号放在配置文件中，也可以用数据库管理， 只需要一个实现了"),o("span",{staticClass:"color-info"},[e._v("IWebUserProvider<CommonAdminWebUser>")]),e._v("接口的类 ")])])]):o("div",[e._v(" 请使用外部账号登陆 ")])])],1)],1)],1)],1)},s=[],r=o(98054),n=o(25787),i=o(42817),l=o(64173),c={data(){return{form:{account:"",password:"",rememberMe:!0},form1:{password:""},doEncode:{called:!1,plainPwd:"",encodedPwd:""},adminInProp:i.Z.serverInfo.adminInProp}},computed:{},mounted(){},activated(){i.Z.logined?this.redirect():(window.defaultLogin?(this.form.account=window.defaultLogin.account,this.form.password=window.defaultLogin.password):(this.form.account="",this.form.password=""),this.doEncode.called=!1)},methods:{onSubmit(){console.debug("form",this.form);const e=this;r.Z.ajax(n.Z.commonPublic.login,this.form,(function(t){i.Z.onLogin(t),e.redirect()}))},createPassword(){const e={password:this.form1.password};this.doEncode.encodedPwd="",this.doEncode.plainPwd="",r.Z.ajax(n.Z.commonPublic.passwordDemo,e,(e=>{this.doEncode.called=!0,this.doEncode.encodedPwd=e.message,this.doEncode.plainPwd=this.form1.password}))},redirect(){const e=l.Z.getRoutePath("status/Summary");console.debug("登录成功，准备跳转到",e),this.$router.push(e)}}},d=c,m=o(1001),u=(0,m.Z)(d,a,s,!1,null,null,null),p=u.exports}}]);