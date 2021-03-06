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

import commenttemplate.annotations.ComponentName;
import commenttemplate.annotations.Params;
import commenttemplate.context.ContextProcessor;
import commenttemplate.context.processor.ContextProcessorCache;
import commenttemplate.expressions.function.Function;
import commenttemplate.expressions.function.FunctionsRegister;
import commenttemplate.template.tags.factory.TagFactory;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.tags.MappableTag;
import commenttemplate.template.tags.TagInitializer;
import commenttemplate.template.tags.factory.MappableTagFactory;
import commenttemplate.util.Join;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import commenttemplate.util.Wrap;
import commenttemplate.util.reflection.annotations.Annotations;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author thiago
 */
public class Init extends TemplateLoaderConfig {
//	private static final String RETRIEVER_CLASS = "commenttemplate.retriever_class";
	private static final String RETRIEVER = "commenttemplate.retriever_type";
	private static final String RESOURCES = "commenttemplate.resource_folder";
	private static final String CONTEXTPROCESSOR = "commenttemplate.contextprocessors";
	private static final String CUSTOM_TAG = "commenttemplate.tag";
	private static final String CUSTOM_TAG_CLASSES = "commenttemplate.tags";
	private static final String CUSTOM_TAG_PARAMS = "params";
	private static final String CUSTOM_FUNCTION = "commenttemplate.function";
//	private static final String CONFIG_CLASS = "commenttemplate.config_class";
	
	private static final String FILE_NAME = "commenttemplate.properties";
	
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
		
		InputStream is = Init.class.getClassLoader().getResourceAsStream(FILE_NAME);
		
		if (is == null) {
			throw new FileNotFoundException(FILE_NAME);
		}

		prop.load(is);

		// @TODO: Providenciar o uso do métodos DB
		String []res = prop.getPropertyAsArray(RESOURCES);

		for (int i = res.length; i-- > 0;) {
			String r = res[i];
			if (!(r.startsWith(".") || r.startsWith("./"))) {
				res[i] = "./" + r;
			}
		}

		setType(LoaderType.valueOf(prop.getProperty(RETRIEVER).toUpperCase()));
		setFolderPath(res);
		
		String []preprocessors = prop.getPropertyAsArray(CONTEXTPROCESSOR);
		if (preprocessors.length > 0) {
			for (String preprocessor : preprocessors) {
				Class<? extends ContextProcessor> preClass = (Class<? extends ContextProcessor>)Class.forName(preprocessor);
				ContextProcessorCache.instance().add(preClass.newInstance());
			}
		}
		
		customTags(prop);
		customTags2(prop);
		customFunctions(prop);
	}

	protected List<String> startsWith(String property, Properties prop) {
		ArrayList<String> list = new ArrayList<String>(prop.size());

		for (Map.Entry<Object, Object> e : prop.entrySet()) {
			String key = e.getKey().toString();
			String remainder = null;

			if (key.startsWith(property) &&
					!(remainder = key.substring(property.length())).isEmpty()
					&& remainder.startsWith(".")) {

				list.add(key);
			}
		}

		return list;
	}

	protected void customFunctions(Properties prop) {
		List<String> functions = startsWith(CUSTOM_FUNCTION, prop);
		
		for (String funcDef : functions) {
			int lastDot = funcDef.lastIndexOf(".");
			String functionName = funcDef.substring(lastDot + 1).trim();
			String className = prop.getProperty(funcDef);
			
			try {
				// @TODO: Lançar exceção
				if (!Utils.empty(functionName) && !Utils.empty(className)) {
					Class<? extends Function> fclass = (Class<? extends Function>)Class.forName(className);
					FunctionsRegister.instance().addFunction(functionName, fclass);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	protected Tuple<String, String[]> getAnnotedTagsParams(String className, Wrap<Class> tagClass) {
		try {
			tagClass.setValue(Class.forName(className));

			Annotations ann = new Annotations(tagClass.getValue());
			String tagName = (String)ann.annotationParam(ComponentName.class, "value");

			tagName = Utils.empty(tagName) ? Utils.uncapitalize(tagClass.getValue().getSimpleName()) : tagName;

			Set<Object> rawTagParamsSet = ann.annotationParamsInHierarchy(Params.class, "values");
			Set<String> tagParamsSet = new HashSet<String>();

			for (Object arr : rawTagParamsSet) {
				Object []arrayObject = (Object[])arr;

				for (Object p : arrayObject) {
					tagParamsSet.add((String)p);
				}
			}

			String []tagParams = new String[tagParamsSet.size()];
			tagParamsSet.toArray(tagParams);

			return new Tuple<String, String[]>(tagName, tagParams);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	// @TODO: Ver se há erros por falta de anotações.
	protected void customTags2(Properties prop) {
		String []classNames = prop.getPropertyAsArray(CUSTOM_TAG_CLASSES);

		for (String className : classNames) {
			Wrap<Class> tagClass = new Wrap<Class>(null);
			Tuple<String, String[]> config = getAnnotedTagsParams(className, tagClass);

			TagFactory factory;

			if (!tagClass.getValue().isAssignableFrom(MappableTag.class)) {
				factory = new TagFactory(config.getA(), tagClass.getValue(), config.getB());
			} else {
				factory = new MappableTagFactory(config.getA(), tagClass.getValue(), config.getB());
			}

			TagInitializer.instance().addTag(factory);
		}
	}

	protected void customTags(Properties prop) {
		List<String> tagsParams = startsWith(CUSTOM_TAG, prop);

		for (String tagDef : tagsParams) {
			int lastDot = tagDef.lastIndexOf(".");
			String tagName = tagDef.substring(lastDot + 1).trim();
			String className = prop.getProperty(tagDef).trim();
			String []tagParams = prop.getPropertyAsArray(Join.path(".").these(tagDef, CUSTOM_TAG_PARAMS).s());
		
			// @TODO: Lançar Exceção
			if (!Utils.empty(tagName) && !Utils.empty(className)) {
				try {
					Class<? extends BasicTag> cls = (Class<? extends BasicTag>)Class.forName(className);
					TagFactory factory;

					if (!cls.isAssignableFrom(MappableTag.class)) {
						factory = new TagFactory(tagName, cls, tagParams);
					} else {
						factory = new MappableTagFactory(tagName, cls, tagParams);
					}
					
					TagInitializer.instance().addTag(factory);
				} catch (ClassNotFoundException ex) {
					// @TODO: Melhorar esta exceção
					throw new RuntimeException(ex);
				}
			}
		}
	}
}
