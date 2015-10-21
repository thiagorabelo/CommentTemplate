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

import commenttemplate.context.ContextProcessor;
import commenttemplate.context.processor.ContextProcessorCache;
import commenttemplate.expressions.function.Function;
import commenttemplate.expressions.function.FunctionsRegister;
import commenttemplate.template.tags.TagComponent;
import commenttemplate.template.tags.Tag;
import commenttemplate.template.tags.TagInitializer;
import commenttemplate.util.Utils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thiago
 */
public class Init extends TemplateLoaderConfig {
//	private static final String RETRIEVER_CLASS = "commenttemplate.retriever_class";
	private static final String RETRIEVER = "commenttemplate.retriever_type";
	private static final String RESOURCES = "commenttemplate.resource_folder";
	private static final String CONTEXTPROCESSOR = "commenttemplate.contextprocessor";
	private static final String CUSTOM_TAG = "commenttemplate.custom_tag";
	private static final String CUSTOM_FUNCTION = "commenttemplate.custom_function";
//	private static final String CONFIG_CLASS = "commenttemplate.config_class";
	
	private static final String filename = "commenttemplate.properties";
	
	// TODO: Fonte de Bug! Se a classe não estiver em um pacote (padrão: a.b.Class), esta regex não
	//       vai conseguir identificar o padrão.
	private static final Pattern SPLIT_NAME_AND_CLASS = Pattern.compile("((?<name>\\w+)\\s*,\\s*)?(?<class>[\\w|\\.]+)");
	private static final Pattern SPLIT_BY_COMMA = Pattern.compile("\\s*,\\s*");
	
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
	// @TODO: Falta carregar Funções customizadas.
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
		
		String []preprocessors = prop.getAsArray(CONTEXTPROCESSOR);
		if (preprocessors.length > 0) {
			for (String preprocessor : preprocessors) {
				Class<? extends ContextProcessor> preClass = (Class<? extends ContextProcessor>)Class.forName(preprocessor);
				ContextProcessorCache.instance().add(preClass.newInstance());
			}
		}
		
		customTags(prop);
		customFunctions(prop);
	}
	
	protected void customFunctions(Properties prop) {
		String []functions = prop.getAsArray(CUSTOM_FUNCTION);
		
		Arrays.asList(functions).stream().forEach(u -> {
			Matcher m = SPLIT_NAME_AND_CLASS.matcher(u);
			
			if (m.find()) {
				try {
					String functionName = m.group("name");
					String className = m.group("class");

					if (!Utils.empty(functionName) && !Utils.empty(className)) {
						Class<? extends Function> fclass = (Class<? extends Function>)Class.forName(className);
						FunctionsRegister.instance().addFunction(functionName, fclass);
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	}
	
	protected void customTags(Properties prop) {
		String []tagsParams = prop.getAsArray(CUSTOM_TAG);
		
		Arrays.asList(tagsParams).stream().forEach(u -> {
			Matcher m = SPLIT_NAME_AND_CLASS.matcher(u);
			
			if (m.find()) {
				try {
					String tagName = m.group("name");
					String className = m.group("class");

					if (Utils.empty(tagName)) {
						Class<? extends TagComponent> cls = (Class<? extends TagComponent>)Class.forName(className);
						TagComponent component = cls.newInstance();
						TagInitializer.getInstance().addTag(component);
					} else {
						Class<? extends Tag> cls = (Class<? extends Tag>)Class.forName(className);
						String p = u.substring(m.end()).trim().substring(1);
						String [] params = SPLIT_BY_COMMA.split(p);
						
						TagComponent component = new TagComponent(tagName, cls, params);
						TagInitializer.instance().addTag(component);
					}
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
					// TODO: Melhorar esta exceção
					throw new RuntimeException(ex);
				}
			} else {
				// TODO: Melhorar esta exceção
				throw new RuntimeException("ERRO AO CARREGAR CUSTOM TAG: " + u);
			}
		});
	}
}
