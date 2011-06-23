Java URL Resolver
==================

This library provides a simple servlet to handle pretty url withing a servlet context

Installation
-------------

Add the listener and servlet to web.xml

	<listener>
		<listener-class>ar.sgt.resolver.listener.ResolverListener</listener-class>
	</listener>	

	<servlet>
		<display-name>Resolver</display-name>
		<servlet-name>Resolver</servlet-name>
		<servlet-class>ar.sgt.resolver.servlet.ResolverServlet</servlet-class>
	</servlet>

Create the file WEB-INF/urlresolver.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="urlresolver.xsd">
      <processor class="ajava.class.TestProcessor1">
        <rule name="rule1" uri="^(?P(slug)[-\w]+)/detail/$"/>
      </processor>
      <processor class="ajava.class.TestProcessor1">
        <rule uri="^detail/$"/>
      </processor>  
    </config>

In this case `?P(slug)` will automatically parsed as a parameter.

Your TestProcessor must extend ar.sgt.resolver.processor.Processor
ResolverContext contains the parameters maps (if apply), in the example `slug`

### Servlet Parameters

* append_backslash: automatically add `/` at the end of the url
* debug: if true, will throw ServletException if no matching fule is found, 404 otherwise.
