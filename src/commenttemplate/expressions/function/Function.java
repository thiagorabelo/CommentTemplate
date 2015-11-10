package commenttemplate.expressions.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.util.Join;

/**
 * A function is a class that extends this Function class and implements the
 * "eval" method of Exp inteface and has a list of parameters of Exp type.
 * 
 * @author thiago
 */
public abstract class Function implements Exp {

	/**
	 * The list of parameters of the function.
	 */
	private List<Exp> args = new ArrayList<Exp>();

	/**
	 * The default constructor.
	 */
	public Function() {
	}

	/**
	 * Return the list of arguments.
	 * @return The list of arguments.
	 */
	public List<Exp> getArgs() {
		return args;
	}
	
	/**
	 * Set the list of arguments.
	 * 
	 * @param args The new list of arguments.
	 */
	public void setArgs(List<Exp> args) {
		this.args = args;
	}

	/**
	 * Convert a array of Exp in a list of function arguments.
	 * 
	 * @param args The array to be converted to a new list of arguments.
	 */
	public void setArgs(Exp [] args) {
		this.args = new ArrayList<Exp>(Arrays.asList(args));
	}
	
	/**
	 * Append some Exp object in list arguments.
	 * 
	 * @param arg The Exp object to be appended to the list arguments.
	 */
	public void appendArg(Exp arg) {
		if (args == null) {
			args = new ArrayList<Exp>();
		}
		args.add(arg);
	}
	
	/**
	 * Trim the list of arguments to save memory.
	 */
	public void trim() {
		try {
			((ArrayList<Exp>)this.args).trimToSize();
		} catch (ClassCastException ex) {
			this.args = new ArrayList<Exp>(this.args);
		}
	}

	/**
	 * Convert this Function instance in String, putting the result in the
	 * StringBuilder passed as parameter.
	 * 
	 * @param sb A StringBuilder which the String result must be put.
	 */
	@Override
	public void toString(StringBuilder sb) {
		sb.append("(");
		String c = "";

		sb.append(Join.with(", ").these(args));

		sb.append(")");
	}

	/**
	 * Converte esta função em um String.
	 * 
	 * @return A String that represents this function.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
