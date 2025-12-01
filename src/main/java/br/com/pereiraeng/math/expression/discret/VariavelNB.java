package br.com.pereiraeng.math.expression.discret;

import java.util.Date;
import java.util.regex.Pattern;

import br.com.pereiraeng.math.expression.Var;
import br.com.pereiraeng.core.BinaryUtils;
import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.core.TimeUtils;

/**
 * Objeto que representa um dado parâmetro da equação lógica, porém que não é um
 * booleano, podendo ser um número inteiro, um número decimal ou uma sequência
 * de carateres. Este valor será transformado em um número através de um
 * {@link OperadorV2B operador}
 * 
 * @author Philipe Pereira
 *
 */
public class VariavelNB implements Var {

	public enum TypeValue {
		INT, FLOAT, DOUBLE, STRING, DATE, CALENDAR, BIT, BYTES;

		public static TypeValue getType(String sql) {
			if (sql.startsWith("text") || sql.startsWith("varchar"))
				return STRING;
			else if (sql.equals("date"))
				return DATE;
			else if (sql.equals("timestamp"))
				return CALENDAR;
			else if (sql.equals("float"))
				return FLOAT;
			else if (sql.equals("double"))
				return DOUBLE;
			else if (sql.contains("bit") || sql.equals("tinyint(1)"))
				return BIT;
			else if (sql.contains("int") || sql.contains("year"))
				return INT;
			else if (sql.contains("blob"))
				return BYTES;
			// caso não for nenhum dos casos acima, joga para o String que de lá
			// a gente se vira
			return STRING;
		}
	}

	private TypeValue type;
	private Object value;

	/**
	 * Nome da variável
	 */
	private String x;

	/**
	 * Contrutor do valor não booleano para variáveis
	 * 
	 * @param x    nome da variável
	 * @param type tipo de variável
	 */
	public VariavelNB(String x, TypeValue type) {
		this.x = x;
		this.type = type;
	}

	/**
	 * Contrutor do valor não booleano para constantes
	 * 
	 * @param exp expressão que indica o conteúdo e tipo de variável
	 */
	public VariavelNB(String exp) {
		this.setStringValue(exp);
	}

	/**
	 * Função que estabelece o valor do objeto a partir da sua sequência de
	 * caracteres representativa
	 * 
	 * @param exp expressão que indica o valor do objeto
	 */
	public void setStringValue(String exp) {
		if (x == null) {
			// caso seja uma constante que está sendo criada, não se sabe a
			// priori qual é seu tipo

			if (exp.startsWith("'") && exp.endsWith("'")) {
				String s = exp.substring(1, exp.length() - 1);
				// entre aspas pode ser uma sequência de caracteres ou um
				// timestamp

				Object value = null;
				TypeValue tv = null;
				if (s.length() == 19) {
					value = new Date(TimeUtils.string2Calendar(s).getTimeInMillis());
					tv = TypeValue.CALENDAR;
				} else if (s.length() == 10) {
					value = TimeUtils.string2Date(s);
					tv = TypeValue.DATE;
				}
				if (value == null) {
					value = s;
					tv = TypeValue.STRING;
				}
				this.value = value;
				this.type = tv;
			} else if (exp.startsWith("b'") && exp.endsWith("'")) {
				// se for um bit
				this.value = exp.substring(2, exp.length() - 1).charAt(0) == '1';
				this.type = TypeValue.BIT;
			} else {
				Integer i = null;
				try {
					i = Integer.parseInt(exp);
				} catch (NumberFormatException e) {
				}

				if (i != null) {
					// se for um inteiro
					this.value = (int) i;
					this.type = TypeValue.INT;
				} else {
					// testar se é um número decimal (float ou double)
					if (exp.contains("f") || exp.contains("F")) {
						Float d = null;
						try {
							d = Float.parseFloat(exp);
						} catch (NumberFormatException e) {
						}
						if (d != null) {
							// se for um número decimal float
							this.value = (Float) d;
							this.type = TypeValue.FLOAT;
						}
					} else {
						Double d = null;
						try {
							d = Double.parseDouble(exp);
						} catch (NumberFormatException e) {
						}
						if (d != null) {
							// se for um número decimal double
							this.value = (Double) d;
							this.type = TypeValue.DOUBLE;
						}
					}

					if (this.type == null) // TODO data
						throw new IllegalArgumentException("Não sei o que é");
				}
			}
		} else {
			// caso seja uma variável, sabe-se seu tipo, logo basta fazer a
			// conversão da sequência de caracteres para o objeto correspondente

			this.value = getValueFromString(exp, this.type);
		}
	}

	public static Object getValueFromString(String exp, TypeValue type) {
		Object out = null;

		switch (type) {
		case STRING:
			out = exp.substring(1, exp.length() - 1);
			break;
		case FLOAT:
			out = Float.parseFloat(exp);
			break;
		case DOUBLE:
			out = Double.parseDouble(exp);
			break;
		case INT:
			out = Integer.parseInt(exp);
			break;
		case DATE:
			out = TimeUtils.string2Date(exp.substring(1, exp.length() - 1));
			break;
		case CALENDAR:
			out = TimeUtils.string2Calendar(exp.substring(1, exp.length() - 1));
			break;
		case BIT:
			if (exp.startsWith("b'"))
				out = BinaryUtils.toBitArray(exp.substring(2, exp.length() - 1));
			else
				out = new boolean[] { exp.charAt(0) == '1' };
			break;
		case BYTES:
			out = StringUtils.fromHex(exp.substring(2));
			break;
		}

		return out;
	}

	@Override
	public void setValue(Object value) {
		if (x != null)
			this.value = value;
	}

	/**
	 * Função que retorna o valor contido no objeto
	 * 
	 * @return objeto
	 */
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (x != null)
			return x;
		else
			return value.toString();
	}

	// *** FUNÇÕES QUE PODEM SER APLICADAS E QUE TEM COMO SAÍDA BOOLEANOS ***
	// - igualdade;
	// - desigualdade;
	// - pertinência;
	// - padrão.

	/**
	 * Função que avalia se o valor deste objeto é igual ao de outro
	 * 
	 * @param v objeto a ser comparado
	 * @return <code>true</code> se for igual, <code>false</code> senão
	 */
	public boolean equals(VariavelNB v) {
		switch (type) {
		case INT:
			int l1 = (int) value;
			int l2 = (int) v.getValue();
			return l1 == l2;
		case FLOAT:
			float f1 = (float) value;
			float f2 = (float) v.getValue();
			return f1 == f2;
		case DOUBLE:
			double d1 = (double) value;
			double d2 = (double) v.getValue();
			return d1 == d2;
		case STRING:
			String s1 = (String) value;
			String s2 = (String) v.getValue();
			return s1.compareTo(s2) == 0;
		case BIT:
			boolean b1 = ((boolean[]) value)[0];
			boolean b2 = false;
			Object obj = v.getValue();
			if (obj instanceof String)
				b2 = ((String) obj).charAt(0) == '1';
			else if (obj instanceof Boolean)
				b2 = (Boolean) obj;
			return !(b1 ^ b2);
		case DATE:
		case CALENDAR:
			Date c1 = (Date) value;
			Date c2 = (Date) v.getValue();
			return c1.equals(c2);
		default:
			return false;
		}
	}

	/**
	 * Função que avalia se o valor deste objeto é maior que outro
	 * 
	 * @param v objeto a ser comparado
	 * @return <code>true</code> se for maior, <code>false</code> senão
	 */
	public boolean gt(VariavelNB v) {
		switch (type) {
		case INT:
			int l1 = (int) value;
			int l2 = (int) v.getValue();
			return l1 > l2;
		case FLOAT:
			float f1 = (float) value;
			float f2 = (float) v.getValue();
			return f1 > f2;
		case DOUBLE:
			double d1 = (double) value;
			double d2 = (double) v.getValue();
			return d1 > d2;
		case STRING:
			String s1 = (String) value;
			String s2 = (String) v.getValue();
			return s1.compareTo(s2) > 0;
		case DATE:
		case CALENDAR:
			Date c1 = (Date) value;
			Date c2 = (Date) v.getValue();
			return c1.after(c2);
		default:
			return false;
		}
	}

	/**
	 * Função que avalia se o valor deste objeto é maior ou igual a outro
	 * 
	 * @param v objeto a ser comparado
	 * @return <code>true</code> se for maior ou igual, <code>false</code> senão
	 */
	public boolean geq(VariavelNB v) {
		switch (type) {
		case INT:
			int l1 = (int) value;
			int l2 = (int) v.getValue();
			return l1 >= l2;
		case FLOAT:
			float f1 = (float) value;
			float f2 = (float) v.getValue();
			return f1 >= f2;
		case DOUBLE:
			double d1 = (double) value;
			double d2 = (double) v.getValue();
			return d1 >= d2;
		case STRING:
			String s1 = (String) value;
			String s2 = (String) v.getValue();
			return s1.compareTo(s2) >= 0;
		case DATE:
		case CALENDAR:
			Date c1 = (Date) value;
			Date c2 = (Date) v.getValue();
			return !c1.before(c2);
		default:
			return false;
		}
	}

	/**
	 * Função que avalia se o valor deste objeto está dentro de um intervalo
	 * 
	 * @param interval descrição do intervalo a ser avaliado
	 * @return <code>true</code> se o elemento estiver dentro do intervalo,
	 *         <code>false</code> senão
	 */
	public boolean between(String interval) {
		String[] borders = interval.split(" / ");

		switch (type) {
		case INT:
			int l = (int) value;

			int inf = Integer.parseInt(borders[0]), sup = Integer.parseInt(borders[1]);

			return l >= inf && l <= sup;
		case FLOAT:
			float f = (float) value;

			float infF = Float.parseFloat(borders[0]), supF = Float.parseFloat(borders[1]);

			return f >= infF && f <= supF;
		case DOUBLE:
			double d = (double) value;

			double infD = Float.parseFloat(borders[0]), supD = Float.parseFloat(borders[1]);

			return d >= infD && d <= supD;
		case STRING:
			String s = (String) value;

			String infS = borders[0].substring(1, borders[0].length() - 1),
					supS = borders[1].substring(1, borders[1].length() - 1);

			return s.compareTo(infS) >= 0 && s.compareTo(supS) <= 0;
		case DATE:
		case CALENDAR:
			Date c = (Date) value;

			Date infC = (Date) getValueFromString(borders[0], type), supC = (Date) getValueFromString(borders[1], type);

			return c.after(infC) && c.before(supC);
		default:
			return false;
		}
	}

	/**
	 * Função que avalia se o valor deste objeto segue um determinado padrão
	 * 
	 * @param pattern descrição do padrão a ser avaliado (entre aspas simples)
	 * @param reg
	 * @return <code>true</code> se o elemento atender ao padrão, <code>false</code>
	 *         senão
	 */
	public boolean pattern(String pattern, boolean reg) {
		pattern = pattern.substring(1, pattern.length() - 1);
		if (reg) {
			pattern = pattern.replace("%", "\\p{Alnum}*?").replace("_", "\\p{Alnum}");
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher((String) value).find();
		} else
			return pattern.equals(value);
	}

	/**
	 * Função que avalia se o valor deste objeto pertence a uma dada lista de
	 * objetos
	 * 
	 * @param set descrição da lista de objetos
	 * @return <code>true</code> se o elemento estiver na lista, <code>false</code>
	 *         senão
	 */
	public boolean in(String set) {
		String[] ss = set.substring(1, set.length() - 1).split(",");
		for (String s : ss)
			if (s.equals((String) value))
				return true;
		return false;
	}
}
