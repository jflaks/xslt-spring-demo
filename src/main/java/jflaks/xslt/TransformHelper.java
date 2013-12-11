package jflaks.xslt;

import org.apache.xalan.extensions.ExpressionContext;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

public class TransformHelper {

	public static String XSL_TRANSFORM_CONTEXT = "TRANSFORM CONTEXT";
	
	public String checkHealth(ExpressionContext expContext, String firstName,
			String response) {
		TransformContext context = getTransformContext(expContext);
		return context.checkHealth(expContext, firstName, response);
	}

	private TransformContext getTransformContext(ExpressionContext expContext) {
		Transformer originalTransformer = null;
		try {
			originalTransformer = (Transformer) expContext.getXPathContext()
					.getOwnerObject();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		TransformContext context = (TransformContext) originalTransformer.getParameter(XSL_TRANSFORM_CONTEXT);
		return context;
	}

}
