package commenttemplate.expressions.function;

import java.util.List;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;

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
	private Exp []args;

	/**
	 * The name of the function.
	 */
	private String name;

	/**
	 * The default constructor.
	 */
	public Function() {
	}

	/**
	 * Return the array of arguments.
	 * @return The array of arguments.
	 */
	public Exp []getArgs() {
		return args;
	}

	/**
	 * Return the array of arguments.
	 * @return The array of arguments.
	 */
	public Exp []arguments() {
		return args;
	}

	/**
	 * Convert a list of Exp in a array of function arguments.
	 * 
	 * @param args The new list of arguments.
	 */
	public void setArgs(List<Exp> args) {
		Exp [] array = new Exp[args.size()];
		args.toArray(array);
		this.args = array;
	}

	/**
	 * Set the list of arguments.
	 * 
	 * @param args The new list of arguments.
	 */
	public void setArgs(Exp []args) {
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Convert this Function instance in String, putting the result in the
	 * StringBuilder passed as parameter.
	 * 
	 * @param sb A StringBuilder which the String result must be put.
	 */
	@Override
	public void toString(StringBuilder sb) {
		sb.append(Utils.empty(name) ? "[anonymous]" : name)
		  .append("(")
		  .append(Join.with(", ").these(args))
		  .append(")");
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
