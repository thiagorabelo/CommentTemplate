
import commenttemplate.util.MyHashMap;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.expressions.parser.LazeTokenizer;
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.parser.Semantic;
import commenttemplate.expressions.parser.Tokenizer;
import commenttemplate.expressions.parser.TokensToExp;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.MyStack;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import java.util.ArrayList;
import java.util.List;
import commenttemplate.util.maps.*;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * TESTE
 * @author thiago
 */
public class TestesRelampagos {
	
	public static void main(String[] args) {
		MyStack<Character> stack = new MyStack<>();
		
		stack.push('a');
		stack.push('b');
		stack.push('c');
		stack.push('d');
		stack.push('e');
		stack.push('f');
		
		for (Character c : stack) {
			System.out.print(c);
		}
		
		System.out.println("");
		
		MyStack<Character> stack2 = new MyStack<>(stack);
		
		System.out.println(stack2.size());

		while (!stack2.isEmpty()) {
			System.out.print(stack2.pop());
		}
		
		MyStack<Character> stack3 = new MyStack<>();

		stack3.push('g');
		stack3.push('h');
		stack3.push('i');
		stack3.push('j');
		stack3.push('k');
		stack3.push('l');
		
		stack3.pushAll(stack);

		System.out.println("");
		for (Character c : stack3) {
			System.out.print(c);
		}
	}
	
	public static void teste14(String[] args) {
		String exp = "(70+10**-2)<2*-6/num.val||!(length('olá mundo')!=num.val)";
		List<Tuple<String, Integer>> l = new Tokenizer(exp).tokenList();
		List<Tuple<String, Integer>> tokens = new ArrayList<>();
		
		l.stream().forEach(u -> {
			tokens.add(u);
			System.out.println(u.getA());
		});
		
		System.out.println(exp);
		for (int i = 0, j = 0; i < exp.length(); i++) {
			if (tokens.get(j).getB() == i) {
				System.out.print("^");
				j++;
			} else {
				System.out.print(" ");
			}
		}
		
		System.out.println("\n-----------------");
		
		tokens.clear();
		for (Tuple<String, Integer> t : new LazeTokenizer(exp)) {
			tokens.add(t);
			System.out.println(t.getA()+":"+t.getB());
		}
		
		System.out.println("\n"+exp);
		for (int i = 0, j = 0; i < exp.length(); i++) {
//			System.out.println(tokens.get(j).getB());
			if (tokens.get(j).getB() == i) {
				System.out.print("^");
				j++;
			} else {
				System.out.print(" ");
			}
		}
		System.out.println("");
	}
	
	public static int length(String content, int begin) throws Exception {
		String part = content.substring(begin);
		List<Tuple<String, Integer>> l = new Tokenizer(part).tokenList();
		String open = "${";
		String close = "}";
		String closeOpen = "}${";
		
		for (Tuple<String, Integer> t : l) {
			String x = t.getA();
			if (x.equals(close) || x.equals(closeOpen)) {
				return t.getB();
			}
			
			if (x.equals(open)) {
				return -1;
			}
		}
		
		return -1;
	}
	
	public static void teste13(String[] args) throws Exception  {
		List<Tuple<String, Integer>> tokens = new Tokenizer("append('${', na|me, '}'").tokenList();
		
		tokens.stream().forEach(t -> {
			System.out.println(t.getA());
		});
		
		System.out.println("-----------------------");
		
		String plain = "${'oi'}\n		${append('${', na|me, '}')}${'teste'}\n" +
"\n" +
" vai tomar banho	${append('[', name, ']')}";
		System.out.println(Utils.concat("[",plain,"]"));
		
		Pattern openExpPattern = Pattern.compile("\\$\\{");
		Matcher m = openExpPattern.matcher(plain);
		int lastEnd = 0;

		System.out.println("Encontrando...");
		while (m.find()) {
			if (m.start() >= lastEnd) {
				int e = length(plain, m.end());
				log(plain.substring(lastEnd, m.start()));
				log(plain.substring(m.end(), m.end() + e));
				lastEnd = m.end() + e + 1;
				System.out.println("---------------------------");
			}
		}
	}
	
	public static void teste12(String[] args) {
		Pattern SPLIT_TAG_CLASS = Pattern.compile("((?<tagname>\\w+)\\s*,\\s*)?(?<tagclass>[\\w|\\.]+)");
		Pattern SPLIT_BY_COMMA = Pattern.compile("\\s*,\\s*");
		
		String padrao = "for,commenttemplate.template.tags.builtin.ForTemplateTag,!list,var,step,counter";
		
		Matcher m = SPLIT_TAG_CLASS.matcher(padrao);
		
		if (m.find()) {
			System.out.println(m.group("tagname"));
			System.out.println(m.group("tagclass"));
			
			String p = padrao.substring(m.end()).trim().substring(1);
			System.out.println(p);
			
			String [] params = SPLIT_BY_COMMA.split(p);
			
			Arrays.asList(params).stream().forEach(u -> System.out.println(u));
		}
	}

	public static void teste11(String[] args) {
		Pattern p = Pattern.compile("<\\!\\-\\-(\\w+)\\s*(.*?)\\s*\\-\\->");
		Pattern param = Pattern.compile("(\\w+)\\=\"([^\"]*)\"");
		String tag = " <!--tag foo=\"param1\" bar=\"param2\" --> ";
		Matcher m = p.matcher(tag);
		
		while (m.find()) {
			System.out.println("TagName: [" + m.group(1) + "]");
			Matcher pr = param.matcher(m.group(2));
			
			while (pr.find()) {
				System.out.println("[" + pr.group(1) + "] => [" + pr.group(2) + "]");
			}
		}
	}


	public static void teste10(String[] args) throws Exception {
		String exp = "a + length(text).floatValue * 2";
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("a", 10.0);
		map.put("text", "Hellor, World");
		
		List<Tuple<String, Integer>> tokens = new Tokenizer(exp).tokenList();
		List<Exp> exprs = new TokensToExp(tokens, exp).convert();
		new Semantic(exprs, exp, tokens).analise();
		Parser p = new Parser();
		MyStack<Exp> stack = p.buildStack(exprs);
		Exp tree = p.buildTree(stack);
		
		System.out.println(tree.eval(new Context(map)));
	}
	
	public static void teste9(String[] args) throws Exception {
		//                   0         10        20        30        40        50
		//                   0123456789012345678901234567890123456789012345678901234567890
		String expression = "a + b.c + d.e + length('my.eggs').toInt.value + z.y";
//		String expression = "a + b.c + d.e + empty('my.eggs') + z.y";
		//                                          ^ - Tá apontando que começa no 23, mas é 22
		List<Tuple<String, Integer>> tokens = new Tokenizer(expression).tokenList();
		
		for (Tuple<String, Integer> token : tokens) {
			System.out.println(String.format("%s::%d", token.getA(), token.getB()));
		}
		
		List<Exp> exprs = new TokensToExp(tokens, expression).convert();
		System.out.println("----------------\n");
		for (Exp exp : exprs) {
			System.out.println(Utils.concat("[", exp, "]"));
		}
		
		new Semantic(exprs, expression, tokens).analise();
		
		Parser p = new Parser();
		
		MyStack<Exp> stack = p.buildStack(exprs);
		
		System.out.println("----------------\n");
		for (Exp e : stack) {
			System.out.println(Utils.concat("[", e, ":", e.getClass().getSimpleName(), "]"));
		}
		
		Exp tree = p.buildTree(stack);
		
		StringBuilder sb = new StringBuilder();
		tree.toString(sb);
		
		System.out.println(sb);
	}
	
	public static void teste8(String[] args) {
		MyHashMap<String, Object> map = new MyHashMap<>();

		map.put("Duis", "Curabitur");
		map.put("leo", "blandit");
		map.put("Aenean", "mollis");
		map.put("eros", "Phasellus");
		map.put("nisl", "ipsum");
		map.put("sagittis", "rutrum");
		map.put("vestibulum", "nunc");

		System.out.println("HashMap size: " + map.size());
		map.print();

		Context context = new Context(map);
		
		System.out.println("Context size: " + context.size());
		for (String key : context.keySet()) {
			System.out.println(Utils.concat("Entry: ", key, " => ", context.get(key)));
		}
		
		System.out.println("-----------------------\n");
		
		context.push();
		
		context.put("ligula", "Nam");
		context.put("mattis", "adipiscing");
		context.put("placerat", "Sed");
		context.put("Pellentesque", "augue");
		context.put("ut", "ipsum");
		context.put("neque", "egestas");
		
		System.out.println("Context size: " + context.size());
		for (String key : context.keySet()) {
			System.out.println(Utils.concat("Entry: ", key, " => ", context.get(key)));
		}
		
		System.out.println("-----------------------\n");

		map.clear();
		map.print();
		
		map.put("nec", "Quisque");
		map.put("vestibulum", "malesuada");
		map.put("et", "placerat");
		map.put("malesuada", "nisl");
		map.put("adipiscing", "Sed");
		map.put("dui", "aliquam");

		context.push(map);

		System.out.println("Context size: " + context.size());
		for (String key : context.keySet()) {
			System.out.println(Utils.concat("Entry: ", key, " => ", context.get(key)));
		}
		
		System.out.println("\n------- MAP -------\nSize: " + map.size());
		map.print();
		
		map = (MyHashMap<String, Object>)map.clone();
		System.out.println("\n------ CLONE ------\nSize: " + map.size());
		map.print();
	}
	
	public static void teste7(String[] args) {
		HashMap<String, String> _map = new HashMap<>();
		int max = 15;

		// 01100100
		for (int i = 0; i < max; i++) {
//			System.out.println("(" + i + ")");
			_map.put(String.valueOf(i), completacom("0", 8, i));
		}

		MyHashMap<String, String> map = new MyHashMap<>(_map);

		map.print();

		Set<String> keys = map.keySet();
		String c = "";
		System.out.println("Tem " + keys.size() + " chaves");

		for (String key : keys) {
			System.out.print(c + key + ":" + map.get(key));
			c = ", ";
		}
		System.out.println("");

		String []rem = {"0", "-15", "6", "3", "7", "10", "14", "12"};
		map.print();

		for (String remove : rem) {
			System.out.println("Removendo: " + remove);
			map.remove(remove);
			map.print();
		}

		map.put("14", completacom("0", 8, 14));
		map.print();


//		for (int i = 0; i < max; i++) {
//			System.out.println(Integer.valueOf(map.get(String.valueOf(i)), 2));
//		}
	}
	
	public static void teste6(String[] args) {
		
		int n = 8; // a mascara só funciona em múltipos de 2
		System.out.println(completacom("0", 8, n));
		
		for (int i = 0; i < 32; i++) {
			System.out.println(Join.with("\n").join(
				completacom("0", 8, i),
				completacom("0", 8, n - 1),
				completacom("0", 8, (n - 1) & i),
				"--------").toString()
			);
		}
		
	}
	
	private static String completacom(String complemento, int num) {
		return completacom(complemento, 32, num);
	}

	private static String completacom(String complemento, int total, int num) {
		String snum = Integer.toBinaryString(num);
		int tem = snum.length(), falta = total - tem;
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < falta; i++) {
			sb.append(complemento);
		}
		
		return sb.append(snum).toString();
	}
	
	public static void teste5(String[] args) {
		HashMap<Object, Object> mapa;
		System.out.println("     " + completacom("0", 1));
		System.out.println("     " + completacom("0", 1 << 4));
		System.out.println("     " + 0x7fffffff);
		System.out.println("     " + Integer.MAX_VALUE);
		System.out.println("--");
		System.out.println("     " + completacom("1", -6));
		System.out.println("     " + completacom("0", -6 & Integer.MAX_VALUE));
		System.out.println("--");
		
		System.out.println("     " + completacom("0", 0x7fffffff));	
		System.out.println("     " + completacom("0", Integer.MAX_VALUE));
		
		
		for (int i = 6; i-- > -7;) {
			System.out.println((i < 0 ? "" : " ") + i + " - " + completacom(i < 0 ? "1" : "0", i));
		}
		
		int x = 1;
		
		for (int i = 0; i < 10; i++) {
			System.out.println(x);
			x <<= 1;
		}
	}
		
	public static void teste4(String[] args) throws Exception {
		Context c = new Context().push();
		c.put("nome", "thiago");
		c.put("sobrenome", "rabelo");
		c.put("idade", 12);
		
		c.push();
		c.put("nome", "gustavo");
		c.put("sobrenome", "costa");
		
		System.out.println("Context size: " + c.size());
		
		ContextWriterMap m = new ContextWriterMap(c);
		
		m.setMode(ContextWriterMap.Mode.STORE);
		
		Writer w = m.getWriter("block1");
		
		w.append("meu nome é ").append(m.get("nome")).append(" ").append(m.get("sobrenome"));
		
		m.pop();
		
		w = m.getWriter("block2");
		
		w.append("meu nome é ").append(m.get("nome")).append(" ").append(m.get("sobrenome")).append(" e minha idade é ").append(m.get("idade"));
		
		m.setMode(ContextWriterMap.Mode.RENDER);
		
		System.out.println(m.getWriter("block1").toString());
		System.out.println(m.getWriter("block2").toString());
		
		HashMap test;
	}
	
	
	
	
	
	
	public static class TesteAutoClose implements AutoCloseable {
		
		private String nome;
		
		public TesteAutoClose(String s) {
			nome = s;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}		

		@Override
		public void close() {
			System.out.println("Fechando, saporra! :: " + nome);
		}
	}

	public static void teste3() {
		System.out.println(Integer.toBinaryString(1001));

		// "come" os bits mais a direita
		System.out.println(Integer.toBinaryString(1001 >>> 1));
		
		// move os bits para direita (acrescenta zeros à esquerda)
		System.out.println(Integer.toBinaryString(1001 << 1));
		
		// move os bits para a esquerda (como os bits mais a direita)
		System.out.println(Integer.toBinaryString(1001 >> 1));	
	}
	
	public static void teste2() {
		try (TesteAutoClose t = new TesteAutoClose("Thiago")) {
			System.out.println("Olá, eu sou o " + t.getNome());
		}
	}

	public static void teste1() {
//	public static void main(String []args) {
		List<String> list = new ArrayList();
		
		list.add(null);
		list.add("/a/");
		list.add("/b/");
		list.add(null);
		list.add("/c/");
		list.add(null);
		
		String [] array = new String[] {null, "d", "e", null, "f", null};
		
		System.out.println(Join.with(",").join("a", "b", "c", new Object[]{"d", "e", new Object[]{"f", "g"}}));
		
		System.out.println(Join.path().skipNulls().join(list.iterator()).toString());

		System.out.println(Join.path().skipNulls().join(array).toString());

		System.out.println(Join.path().skipNulls().join(list, array, "g", null, "h", "i", null).join("j", "k", "l"));
		
		System.out.println(Utils.concat("olá", "mundo", "cruel"));
	}
	
	public static void log(String ...str) {
		System.out.println(Join.with("").join("[", str, "]"));
	}
}
