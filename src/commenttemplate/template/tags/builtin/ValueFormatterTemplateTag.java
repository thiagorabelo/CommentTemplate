package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ValueFormatterTemplateTag extends TemplateTag {
	
	public ValueFormatterTemplateTag() {
	}

	@Override
	public int evalParams(Context context, Writer sb) {
//		TemplateBlock actualBlock = (TemplateBlock) block;
//		ValueFormatterParams params = (ValueFormatterParams) actualBlock.getParams();
//		
//		Exp obj = params.getObject();
//		Object object;
//		
//		if (obj != null && (object = obj.eval(context)) != null) {
//			Exp frmttr = params.getFormatter();
//			Object formatter;
//			
//			Exp mx = params.getMax();
//			Object max;
//			
//			if (frmttr != null && (formatter = frmttr.eval(context)) != null) {
//				Formatter f = FormatterManager.getFormatter((String)formatter);
//
//				String result = f.format(object, null);
//
//				if (mx != null && (max = mx.eval(context)) != null) {
//					result = result.length() > (Integer)max ? result.substring(0, ((Integer)max) - 3) + "..." : result;
//					sb.append(result);
//				} else {
//					sb.append(result);
//				}
//			} else if (mx != null && (max = mx.eval(context)) != null) {
//				String result = object.toString();
//				result = result.length() > (Integer)max ? result.substring(0, ((Integer)max) - 3) + "..." : result;
//				sb.append(result);
//			} else {
//				sb.append(object.toString());
//			}
//		} else if (actualBlock.getNextInner() != null) {
//			TemplateWriter tw = new TemplateWriter();
//			evalBody(block, context, tw);
//			
//			sb.append(tw.toString());
//		}

		return SKIP_BODY;
	}
}
