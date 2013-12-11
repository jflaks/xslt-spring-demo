package jflaks.xslt;

import org.apache.xalan.extensions.ExpressionContext;
import org.springframework.stereotype.Service;

@Service
public class StateTrooper {
	public String checkHealth(ExpressionContext context, String firstName,
			String response) {
		String verdict = "";
		if ("arrrrg".equals(response)) {
			verdict = "I'm sorry - this thing isn't " + firstName + " anymore";
		} else {
			verdict = "Fine";
		}
		
		return verdict;
	}
}
