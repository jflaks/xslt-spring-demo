package jflaks.xslt;

import org.apache.xalan.extensions.ExpressionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformContext {
	private final StateTrooper stateTrooper;

	@Autowired
	public TransformContext(StateTrooper stateTrooper) {
		this.stateTrooper = stateTrooper;
	}

	public String checkHealth(ExpressionContext context, String firstName,
			String response) {
		return stateTrooper.checkHealth(context, firstName, response);
	}

}
