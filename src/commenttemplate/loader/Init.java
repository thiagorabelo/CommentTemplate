/*
 * Copyright (C) 2015 thiago.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package commenttemplate.loader;

import commenttemplate.context.ContextPreprocessor;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author thiago
 */
public class Init extends TemplateLoaderConfig {
	private static final String RETRIEVER = "commenttemplate.retriever_type";
	private static final String RESOURCES = "commenttemplate.resource_folder";
	private static final String PREPROCESSOR = "commenttemplate.preprocessor";
	private static final String CUSTOM_TAG = "commenttemplate.custom_tag";
//	private static final String CONFIG_CLASS = "commenttemplate.config_class";
	
	private static final String filename = "commenttemplate.properties";
	
	private static Boolean configured = false;
	
	public static void config() {
		synchronized (configured) {
			if (!configured) {
				try {
					Init conf = new Init();
					conf.init();
					Loader.initLoader(conf.getType().config(conf));
					configured = true;
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	}
	

	// @TODO: Refatorar para que seja possível usar uma classe customizada para fazer a configuração.
	@Override
	public void init() throws Exception {
		Properties prop = new Properties();
		
		InputStream is = Init.class.getClassLoader().getResourceAsStream(filename);
		
		if (is == null) {
			throw new FileNotFoundException(filename);
		}

		prop.load(is);

		// TODO: Providenciar o uso do métodos DB
		String []res = prop.getAsArray(RESOURCES);

		for (int i = res.length; i-- > 0;) {
			String r = res[i];
			if (!(r.startsWith(".") || r.startsWith("./"))) {
				res[i] = "./" + r;
			}
		}

		setType(LoaderType.valueOf(prop.getProperty(RETRIEVER).toUpperCase()));
		setFolderPath(res);
		
		// @TODO: Load preprocessors
		String []preprocessors = prop.getAsArray(PREPROCESSOR);
		if (preprocessors.length > 0) {
			for (String preprocessor : preprocessors) {
				Class<? extends ContextPreprocessor> preClass = (Class<? extends ContextPreprocessor>)Class.forName(preprocessor);
				ContextPreprocessor.preprocessors.add(preClass);
			}

			if (!ContextPreprocessor.preprocessors.isEmpty()) {
				ContextPreprocessor.preprocessors.trimToSize();
			}
		}
	}
}
