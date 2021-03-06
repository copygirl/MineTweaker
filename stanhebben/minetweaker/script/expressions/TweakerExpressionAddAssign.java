package stanhebben.minetweaker.script.expressions;

import stanhebben.minetweaker.api.TweakerExecuteException;
import stanhebben.minetweaker.api.TweakerNameSpace;
import stanhebben.minetweaker.api.value.TweakerValue;
import stanhebben.minetweaker.script.TweakerFile;

public class TweakerExpressionAddAssign extends TweakerExpression {
	private TweakerExpression left;
	private TweakerExpression right;
	
	public TweakerExpressionAddAssign(TweakerFile file, int line, int offset, TweakerExpression left, TweakerExpression right) {
		super(file, line, offset);
		
		this.left = left;
		this.right = right;
	}

	@Override
	public TweakerValue executeInner(TweakerNameSpace namespace) {
		TweakerValue leftValue = left.execute(namespace);
		if (leftValue == null) throw new TweakerExecuteException("Cannot add a value to a null value");
		TweakerValue result = leftValue.addAssign(right.execute(namespace));
		left.assign(namespace, result);
		return result;
	}
}
