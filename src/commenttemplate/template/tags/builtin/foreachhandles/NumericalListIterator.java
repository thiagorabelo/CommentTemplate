package commenttemplate.template.tags.builtin.foreachhandles;

import commenttemplate.template.tags.builtin.foreachhandles.NumericalList;
import commenttemplate.util.Utils;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
public class NumericalListIterator implements Iterator<Integer> {

	private NumericalList numList;
	private int current;
	private boolean started;
	private boolean upward; // ascendente
	
	public NumericalListIterator(NumericalList numList) {
		this.numList = numList;
		current = numList.getStart();
		started = false;

		upward = numList.getLength() >= numList.getStart();
	}
	
	@Override
	public boolean hasNext() {
		return (numList.size() > 0) && (!started || (upward ? current < (numList.getLength() - 1) : current > (numList.getLength() + 1)));
	}

	@Override
	public Integer next() {
		if (hasNext()) {
			if (started) {
				current += numList.getStep();
			} else {
				started = true;
			}

			return current;
		}

		throw new RuntimeException(Utils.concat("Iterating over the limit of list [", numList.getStart(), "..", numList.getLength(), "]"));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
