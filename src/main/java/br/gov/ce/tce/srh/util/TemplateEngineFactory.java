package br.gov.ce.tce.srh.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

public class TemplateEngineFactory {

	public static VelocityEngine velocityEngine() throws VelocityException, IOException {
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine velocityEngine = new VelocityEngine(props);
		return velocityEngine;
	}
}
