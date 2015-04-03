package commenttemplate.loader;

import commenttemplate.loader.db.RetieverDB;
import commenttemplate.loader.file.RetrieverFile;

/**
 *
 * @author thiago
 */
public enum LoaderType implements LoaderTypeInterface {
	FILE {
		@Override
		public TemplateRetrieve config(TemplateLoaderConfig conf) {
			return new RetrieverFile(conf.getPaths());
		}
	},

	DB {
		@Override
		public TemplateRetrieve config(TemplateLoaderConfig conf) {
			return new RetieverDB(conf.getDao());
		}
	}
	;


	@Override
	public TemplateRetrieve config(TemplateLoaderConfig conf) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
