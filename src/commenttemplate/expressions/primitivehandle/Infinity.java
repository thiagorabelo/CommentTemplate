package commenttemplate.expressions.primitivehandle;

/**
 *
 * @author thiago
 */
public class Infinity extends Number implements Const {
	private static final long serialVersionUID = 4099056339188032043L;
	
	static {
		infinity = new Infinity(false);
		_infinity = new Infinity(true);
	}
	
	protected static final Infinity infinity;
	protected static final Infinity _infinity;
	
	private final boolean negative;
	
	private Infinity(boolean negative) {
		this.negative = negative;
	}

	@Override
	public int intValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long longValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float floatValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double doubleValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public boolean isNegative() {
		return negative;
	}
	
	@Override
	public String toString() {
		return negative ? "-Infinity" : "Infinity";
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Infinity) && (((Infinity)other).negative == negative);
	}
}
