package jflaks.xslt;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.InputSource;

@RestController
public class TransformController {

	private static final String XERCES_TRANSFORMER_FACTORY = "org.apache.xalan.processor.TransformerFactoryImpl";
	private final TransformContext transformContext;
	
	@Autowired
	public TransformController(TransformContext transformContext) {
		this.transformContext = transformContext;
	};
	
	@RequestMapping("/")
	public String index() {
		String output = "";
		try {

	         Map<String, Object> transformationParameters = new HashMap<String, Object>();
	         transformationParameters.put(TransformHelper.XSL_TRANSFORM_CONTEXT, transformContext);

	         String xmlFile = "";
	         String xslFile = "";
	         xslFile = ResourceFile.fromRelativePath(XsltCommentService.class, "commentsToJson.xsl")
	         output = transformXmlToJson(xslFile, sheetSchema, transformationParameters);
	      }
	      catch (Exception e) {
	         throw e;
	      }
		return output;
	}
	
	public String transformXmlToJson(String xslFile, String xmlFile, Map<String, Object> transformParameters) {
	      new ByteArrayOutputStream().withStream { ByteArrayOutputStream os ->
	         read(xslFile) { InputStream xlsFileStream ->
	            Document xmlDoc = createDocumentFromSchema(xmlFile);
	            Transformer transformer = createSchemaTransformer(xlsFileStream);
	            for (TransformParameter)
	            transformParameters.each {
	               transformer.setParameter(it.key, it.value);
	            }
	            transformer.transform(new DOMSource(schemaDoc), new StreamResult(os));
	            new String(os.toByteArray(), Charset.forName("UTF-8"));
	         }
	      }
	   }

	   private Transformer createSchemaTransformer(InputStream xslStream) {
	      TransformerFactory transformerFactory = TransformerFactory.newInstance(XERCES_TRANSFORMER_FACTORY, null);

	      transformerFactory.newTransformer(new StreamSource(xslStream));
	   }

	   private Document createDocumentFromSchema(String schemaLocation) {
	      //DocumentBuilderFactory instances are not thread safe
	      //Schema is the more challenging doc, needing XInclude support
	      //We document build it and make the transform on the schema, though
	      //the root match then processes the layout, itself brought in to xsl as a document

	      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance()
	      documentBuilderFactory.setNamespaceAware(true)
	      documentBuilderFactory.setXIncludeAware(true)
	      documentBuilderFactory.setIgnoringElementContentWhitespace(true)
	      DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder()
	      builder.parse(new InputSource(schemaLocation))
	   }




}
