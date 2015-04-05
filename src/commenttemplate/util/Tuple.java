package commenttemplate.util;

import java.util.Map;

/**
 *
 * @author thiago
 * @param <A>
 * @param <B>
 */
// TODO: Map.Entry é usado para fins de depuração, pois as IDEs mostram o toString().
public class Tuple<A, B> /*implements Map.Entry<A, B>*/ {
	
	private A a;
	private B b;

	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Tuple) && (a.equals(((Tuple)other).a) && b.equals(((Tuple)other).b));
	}
	
	@Override
	public String toString() {
		return ""+a.toString() + "=>" + b.toString();
	}

	/*
	@Override
	public A getKey() {
		return a;
	}

	@Override
	public B getValue() {
		return b;
	}

	@Override
	public B setValue(B value) {
		B old = b;
		b = value;
		return b;
	}
	*/
}
