<div class="navbar-collapse">
	<ul class="nav navbar-nav">
		<#if hasAjax>
		<li ${classAjax}><a href="/_common_/dev/">Ajax Api</a></li>
		</#if>
		<#if hasWs>
		<li ${classWs}><a href="/_common_/dev/ws">WebSocket Api</a></li>
		</#if>
		<li><a href="/_common_/admin" target="_blank">框架后台</a></li>
	</ul>
</div>
