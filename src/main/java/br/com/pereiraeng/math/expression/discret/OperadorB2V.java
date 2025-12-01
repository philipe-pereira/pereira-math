package br.com.pereiraeng.math.expression.discret;

import br.com.pereiraeng.math.expression.continuous.Valor;

/**
 * A ideia aqui seria costurar as expressões contínuas com as discretas
 * 
 * este operador retornaria dois ValorNB diferentes em função do {@link ValorB}
 * que recebesse como parâmetro. No entanto, ValorNB hoje só trabalha com
 * valores constantes (não aceita expressões). Ele ele deveria ser transformado
 * em classe abstrata com dois herdeiros: ConstanteNB (que trabalharia com
 * objeto de diferentes tipos, como por exemplo datas e sequência de caracteres)
 * e {@link Valor} (que trabalharia somente com valores numéricos e obtidos em
 * expressões)
 * 
 * @author Philipe Pereira
 *
 */
public class OperadorB2V {

	private ValorB operando;

	private VariavelNB valor1, valor0;

	public void setOperando(ValorB operando) {
		this.operando = operando;
	}

	public void setValor1(VariavelNB valor1) {
		this.valor1 = valor1;
	}

	public void setValor0(VariavelNB valor0) {
		this.valor0 = valor0;
	}

	public VariavelNB getValor() {
		if (operando.getValue())
			return valor1;
		else
			return valor0;
	}
}
