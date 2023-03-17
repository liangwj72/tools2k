<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="must-revalidate" />
<title>${pageTitle} - WebSocket Api测试</title>
<#include "_css"/>
<link href="/_common_/statics/api_server/dev.css" rel="stylesheet">
</head>

<body>
	<div id="myapp">

		<nav class="navbar navbar-default " role="navigation">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<a class="navbar-brand"> <label class="system-name">${pageTitle}</label></a>
				</div>

				<#include "_nav-link"/>
			</div>
		</nav>

		<div id="leftDiv" v-show="connected">
			<div class="panel-group" role="tablist" aria-multiselectable="true">

				<#list vo.infGroup as group><!-- 一个接口类 -->
				<div class="panel panel-default">
					<a class="my-panel-heading btn-default" data-toggle="collapse"
						href="#coll-${group.infKey}" aria-expanded="false"
						aria-controls="#coll-${group.infKey}">${group.memo}
						${group.infKey} </a>
					<div id="coll-${group.infKey}" class="panel-collapse collapse"
						role="tabpanel">
						<div class="list-group">
							<#list group.methods as m> <a href="#"
								onclick="show('${m.key}Div');return false"
								class="list-group-item"> ${m.methodKey} <!--  
								<span	class="text-muted">${m.memo}</span>
								 --> <#if !m.needLogin> <span
									class="label label-danger pull-right">open</span> <#else>
								<span class="label label-default pull-right">${m.optId}</span></#if>
							</a> </#list>
						</div>
					</div>
				</div>
				</#list>
			</div>
		</div>
		
		<div id="testDiv">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a
					href="#tab-test-result" aria-controls="tab-test-result" role="tab"
					data-toggle="tab">收到的消息</a></li>
				<li role="presentation"><a href="#tab-all-json"
					aria-controls="tab-all-json" role="tab" data-toggle="tab">所有接口</a></li>

			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<!-- 收到的消息 -->
				<div role="tabpanel" class="tab-pane active" id="tab-test-result">
					<div style="margin: 5px">
						<a class='btn btn-default btn-sm' @click="clearLog">清空</a>

				<div class="pull-right">
					{{curUser}}
					<button type="button" @click='onBtnClick' class="btn btn-default">{{connectBtnText}}</button>
				</div>


					</div>
					<div v-for="row in msgList" :class="row.clazz">
						{{row.date}} {{row.title}}
						<pre v-text="row.data" class="log-row"></pre>
					</div>
				</div>
				<!-- /收到的消息 -->

				<!-- 所有接口 -->
				<div role="tabpanel" class="tab-pane" id="tab-all-json">
					<pre>
<#list vo.infGroup as group>
	/** ${group.memo} */
	${group.infKey}: {
<#list group.methods as m> 
		${m.methodKey}: '${vo.apiUrlPrefix}/${m.url}', //  ${m.memo}
</#list>
	},
</#list>
			</pre>
				</div>
				<!-- /所有接口 -->
			</div>
		</div>

		<div id="descDiv">
			<div v-show="connected">
			<#list vo.infGroup as group>
			<#list group.methods as m>
				<div id="${m.key}Div" style="display: none"
					class="panel panel-default">
					<div class="panel-heading">${m.memo} ${m.url}</div>
					<div class="panel-body">
						<h4>
							URL : <span class="text-info">${m.url}</span>
						</h4>
						<p>
							<#if m.needLogin> 登录用户 : <span class="label label-success">
								${m.webUserClasses}</span> ${m.optId} <#else> <span
								class="label label-danger">无需登录</span></#if>
						</p>
						<form v-on:submit.prevent="onMethodFormSubmit" data-url="${m.url}"
							class="js_form">
							<table width="100%" class="table">
								<#list m.paramVoList as pp>
								<tr>
									<td width="120" valign="top"><#if pp.notNull> <span
											class="text-danger">*</span></#if><a data-toggle="tooltip"
										data-placement="right" title="${pp.className}">${pp.name}</a>
										<#if pp.array> []</#if></td>
									<td><#if pp.uploadFile> <input name="${pp.name}"
											type="file" class="js_dropify" data-show-remove="false" /> <#else>
										<#if pp.checkBox> <input name="${pp.name}"
											type="checkbox" value="true" /> <#else>
										<div style="padding-bottom: 2px;">
											<input name="${pp.name}" type="text" value="${pp.value}"
											<#if pp.notNull>required="required"</#if>
											/>
											<#if pp.array> <a
												class="btn btn-xs btn-default js_add_input"> + </a></#if>
										</div></#if></#if> <span class="text-muted">${pp.memo}</span></td>
								</tr>
								</#list>
								<tr>
									<td>&nbsp;</td>
									<td>
										<button class="btn btn-primary w-m100" type="submit">测试</button>
									</td>
								</tr>
							</table>
						</form>

						<hr />
						<h4>返回的结果</h4>
						<span class="text-info">${m.returnClassName}</span>
						<pre>
${m.returnClassDesc}
		  </pre>

					</div>
				</div>
				</#list>
				</#list>
			</div>
		</div>
	</div>
	
<#include "_js"/>

<script src="/_common_/statics/api_server/dev.ws.js"></script>

</body>
</html>