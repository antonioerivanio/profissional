package br.gov.ce.tce.srh.util;

import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.web.context.WebApplicationContext;


/*
 * Copyright (C) 2010 Matthias We�endorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ScopeMetadataResolver extends AnnotationScopeMetadataResolver {
	/**
	 * A <code>ScopeMetadataResolver</code> implementation which maps the CDI provided scopes to
	 * their Spring counterparts.
	 * <p>
	 * The Spring Framework supports the JSR-330 to declare components, however it does not support
	 * the API, provided by the JSR-299
	 * <p>
	 * This class allows Application developers to use the JSR-299 for scoping annotations. The
	 * benefit is that a Spring bean can be completely defined by JSR-299 and JSR-330, like:
	 * <p>
	 * 
	 * <pre>
	 * javax.inject.Named("mySpringBean")
	 * javax.enterprise.context.RequestScoped
	 * public class MySpringBeanController{...}
	 * </pre>
	 * <p>
	 * To use this class you need to configure it within the Spring Context, like
	 * <p>
	 * 
	 * <pre>
	 * <context:component-scan
	 *    scope-resolver="net.wessendorf.spring.scopes.CdiScopeMetadataResolver"
	 *    base-package="my.packages..." />
	 * </pre>
	 * @author Matthias We�endorf
	 */

	/**
	 * Checks if one of the following CDI scope annoations are used and maps them to their matching
	 * Spring scopes:
	 * <ul>
	 * <li><code>javax.enterprise.context.RequestScoped</code></li>
	 * <li><code>javax.enterprise.context.SessionScoped</code></li>
	 * <li><code>javax.enterprise.context.ApplicationScoped</code></li>
	 * </ul>
	 * If none of them are found it delegates back to the original Spring
	 * <code>AnnotationScopeMetadataResolver</code> class.
	 */
	@Override
	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		ScopeMetadata metadata = new ScopeMetadata();
		if (definition instanceof AnnotatedBeanDefinition) {
			AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
			Set<String> annotationTypes = annDef.getMetadata().getAnnotationTypes();

			if (annotationTypes.contains(RequestScoped.class.getName())) {
				metadata.setScopeName(WebApplicationContext.SCOPE_REQUEST);
			} else if (annotationTypes.contains(SessionScoped.class.getName())) {
				metadata.setScopeName(WebApplicationContext.SCOPE_SESSION);
			} else if (annotationTypes.contains(ApplicationScoped.class.getName())) {
				metadata.setScopeName(WebApplicationContext.SCOPE_APPLICATION);
			} else if (annotationTypes.contains(ViewScoped.class.getName())) {
				metadata.setScopeName(ViewScope.SCOPE_NAME);
			} else {
				// do the regular Spring stuff..
				return super.resolveScopeMetadata(definition);
			}
		}
		return metadata;
	}
}
