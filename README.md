Java URL Resolver
==================

This library provides a simple filter to handle pretty url withing a servlet context

Installation
-------------

Add the listener and filter to web.xml

	<listener>
		<listener-class>ar.sgt.resolver.listener.ResolverListener
		</listener-class>
	</listener>

	<filter>
		<filter-name>ResolverFilter</filter-name>
		<filter-class>ar.sgt.resolver.filter.ResolverFilter</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>ResolverFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	

Create the file WEB-INF/urlresolver.xml

	<?xml version="1.0" encoding="UTF-8"?>
	<config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="urlresolver.xsd">
	  <processor class="a_ar.sgt.resolver.processor.ResponseProcessor_Class_Impl" redirect="/page/playerinfo.jsp">
	    <rule name="playerinfo" pattern="^/detail/($key[\w]+)/$"/>
	  </processor>
	  <processor class="another_ar.sgt.resolver.processor.ResponseProcessor_Class_Impl" redirect="/page/search.jsp">
	    <rule pattern="^/$"/>
	    <rule name="search" pattern="^/search/($query.+)/$"/>
	    <rule name="banlist" pattern="^/banlist/$">
	        <arg name="type" value="ban"/>
	    </rule>
	    <rule name="serverfilter" pattern="^/server/($query[\w]+)/$">
	        <arg name="type" value="server"/>
	    </rule>    
	  </processor>
	  <processor class="other_ar.sgt.resolver.processor.ResponseProcessor_Class_Impl" redirect="/json/alias.jsp">
	    <rule name="alias" pattern="^/alias/($key[\w]+)/$"/>
	    <rule name="alias-ip" pattern="^/alias/($key[\w]+)/ip/$">
	        <arg name="ip" value="ip"/>
	    </rule>
	  </processor>
	  <forward-processor redirect="/page/faq.jsp">
	    <rule name="faq" pattern="^/faq/$"/>
	  </forward-processor>  
	</config>

The example is quite self explanatory. Regexp use named groups (quite similar to python regexp but with simpler syntax).

`ResolverContext` will contains this named parameters in a Map

### Filter Parameters

* append_backslash: if true, automatically add `/` at the end of the url (default true)
