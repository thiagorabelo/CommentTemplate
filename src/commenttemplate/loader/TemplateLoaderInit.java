package commenttemplate.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import commenttemplate.loader.db.TemplateDAO;
import commenttemplate.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author thiago
 */
// Era o que seria usado pelo futurepages
// @TODO: Apagar assim que possível
@Deprecated()
public class TemplateLoaderInit extends TemplateLoaderConfig {
	
	private static final String TEMPLATE_PARAMS_TAG_NAME = "template-params";
	private static final String TEMPLATE_PARAMS_ATTR_TYPE = "type";
	private static final String RESOURCE_FOLDER_TAG_NAME = "resource-folder";
	private static final String RESOURCE_FOLDER_ATTR_PATH = "path";
	private static final String RESOURCE_DAO_TAG_NAME = "resource-dao";
	private static final String RESOURCE_DAO_ATTR_CLASS = "class";
	
	private String appParams;
	
	public TemplateLoaderInit(String context, String confFolder, String paramFile) {
		appParams = concatPath(context, confFolder, paramFile);
	}
	
	private String concatPath(String ...parts) {
		List<String> paths = new ArrayList<String>();
		
		for (String part : parts) {
			if (!Utils.empty(part)) {
				paths.add(part);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(paths.get(0));
		
		for (int i = 1; i < paths.size(); i++) {
			String part = paths.get(i);
			
			if (i != paths.size() - 1) {
				if (paths.get(i - 1).endsWith(File.separator)) {
					if (part.startsWith(File.separator)) {
						part = part.substring(1);
					}
				} else if (!paths.get(i - 1).endsWith(File.separator)) {
					part = File.separator + part;
				}
				
			} else {
				part = File.separator + part;
			}

			sb.append(part);
		}
		
		return sb.toString();
	}

	@Override
	public void init() throws Exception {
		File xmlFile = new File(appParams);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);

		doc.getDocumentElement().normalize();

		beginProccess(doc);
	}
	
	private void beginProccess(Document doc) throws Exception {
		NodeList templateParams = doc.getElementsByTagName(TEMPLATE_PARAMS_TAG_NAME);

		if (templateParams.getLength() > 0) {
			Node params = templateParams.item(templateParams.getLength() - 1);
			LoaderType type = LoaderType.valueOf(params.getAttributes().getNamedItem("type").getNodeValue().toUpperCase());
			setType(type);
			
			if (type == LoaderType.DB) {
				List<Node> resources = filterNodes(params, RESOURCE_DAO_TAG_NAME);
				readDataBaseTypeConfig(resources);
			} else if (type == LoaderType.FILE) {
				List<Node> resources = filterNodes(params, RESOURCE_FOLDER_TAG_NAME);
				readFileTypeConfig(resources);
			}	
		}
	}
	
	public List<Node> filterNodes(Node params, String tagName) {
		NodeList list = params.getChildNodes();
		List<Node> resources = new ArrayList<Node>();
		
		for (int i = 0, len = list.getLength(); i < len; i++) {
			Node res = list.item(i);

			if (tagName.equals(res.getNodeName()) && res.getNodeType() == 1) {
				resources.add(res);
			}
		}
		
		return resources;
	}
	
	public void readFileTypeConfig(List<Node> resources) throws Exception {
		String []paths = new String[resources.size()];
		for (int i = 0, size = resources.size(); i < size; i++) {
			Node res = resources.get(i);
			paths[i] = res.getAttributes().getNamedItem(RESOURCE_FOLDER_ATTR_PATH).getNodeValue();
		}
		setFolderPath(paths);
	}

	public void readDataBaseTypeConfig(List<Node> resources) throws Exception {
		String classPath = resources.get(resources.size() - 1).getAttributes().getNamedItem(RESOURCE_DAO_ATTR_CLASS).getNodeValue();
		Class confClass = Class.forName(classPath);
		TemplateDAO instance = (TemplateDAO)confClass.newInstance();
		setDao(instance);
	}
}
