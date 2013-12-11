package jflaks.xslt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RestController
public class TransformController {

	private static final String XERCES_TRANSFORMER_FACTORY = "org.apache.xalan.processor.TransformerFactoryImpl";
	private final TransformContext transformContext;

	@Autowired
	public TransformController(TransformContext transformContext) {
		this.transformContext = transformContext;
	};

	@RequestMapping("/")
	public String index() throws Exception {
		Map<String, Object> transformationParameters = new HashMap<String, Object>();
		transformationParameters.put(TransformHelper.XSL_TRANSFORM_CONTEXT, transformContext);
		String xmlFileName = "town.xml";
		String xslFileName = "dealWithZombies_v4.xsl";
		String output = transformXmlToJson(xslFileName, xmlFileName, transformationParameters);
		return output;
	}

	public String transformXmlToJson(String xslFileName, String xmlFileName, Map<String, Object> transformParameters)
			throws TransformerException, ParserConfigurationException, SAXException, IOException {

		InputStream xslFileStream = TransformController.class.getResourceAsStream(xslFileName);
		Document xmlDoc = createDocumentFromXML(xmlFileName);
		Transformer transformer = createSchemaTransformer(xslFileStream);
		for (String tpKey : transformParameters.keySet()) {
			transformer.setParameter(tpKey, transformParameters.get(tpKey));
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(xmlDoc), new StreamResult(os));
		return new String(os.toByteArray(), Charset.forName("UTF-8"));
	}

	private Transformer createSchemaTransformer(InputStream xslStream) throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance(XERCES_TRANSFORMER_FACTORY, null);
		return transformerFactory.newTransformer(new StreamSource(xslStream));
	}

	private Document createDocumentFromXML(String XMLLocation) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setXIncludeAware(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		InputStream is = TransformController.class.getResourceAsStream(XMLLocation);
		return builder.parse(is);
	}

}
