package commenttemplate.expressions.function;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * @author thiago
 */
public class Get extends Function {

	protected Object get(Object collection, Object index) {
		if (collection instanceof List) {
			if (index instanceof Integer) {
				return ((List)collection).get((Integer)index);
			} else if (index instanceof Long) {
				return ((List)collection).get(((Long)index).intValue());
			}
		} else if (collection instanceof Map) {
			return ((Map)collection).get(index);
		} else if (collection.getClass().isArray()) {
			if (index instanceof Integer) {
				return Array.get(collection, (Integer)index);
			} else if (index instanceof Long) {
				return Array.get(collection, ((Long)index).intValue());
			}
		}

		return null;
	}

	@Override
	public Object eval(Context context) {
		List<Exp> args = getArgs();

		if (args.size() >= 2) {
			Object collection = args.get(0).eval(context);
			Object index = args.get(1).eval(context);

			return get(collection, index);
		}

		return null;
	}
}
