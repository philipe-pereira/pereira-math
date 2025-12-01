package br.com.pereiraeng.math;

import java.util.Arrays;

import br.com.pereiraeng.math.IntegrationPoints.Quadrature;

public class Integrador {

	// ------------------------ INTEGRAÇÃO NUMÉRICA ------------------------

	/**
	 * Função que efetua a integração uma {@link Funcao função} com relação à sua
	 * variável t
	 * 
	 * @param f    função a ser integrada
	 * @param x    argumentos x
	 * @param t0t1 intervalo de integração em t
	 * @param vars variáveis intermediárias da função
	 * @return valor da integral
	 */
	public static double integraT(Funcao f, double[] x, double[] t0t1, double... vars) {
		return integraT(f, x, t0t1, vars, IntegrationPoints.Quadrature.SIMPSON, 4);
	}

	/**
	 * Função que efetua a integração em t de uma {@link Funcao função}
	 * 
	 * @param f     função a ser integrada
	 * @param x     argumentos x
	 * @param t0t1  intervalo de integração em t
	 * @param vars  variáveis intermediárias da função
	 * @param quad  regra de quadratura
	 * @param order ordem da integração
	 * @return valor da integral
	 */
	public static double integraT(Funcao f, double[] x, double[] t0t1, double[] vars, Quadrature quad, int order) {
		// metade do intervalo a ser integrado
		double Dt2 = (t0t1[1] - t0t1[0]) / 2;

		double out = 0;

		int points = IntegrationPoints.np(IntegrationPoints.Shape.SEGMENTO, quad, order);
		for (int j = 1; j <= points; j++) {
			double[] ds = IntegrationPoints.xyw(IntegrationPoints.Shape.SEGMENTO, quad, order, j);

			// (b-a)x/2 + (a+b)/2 = a + (b-a)*(x+1)/2
			double t = t0t1[0] + Dt2 * (ds[0] + 1);

			double ft = f.ft(x, t, vars)[0];

			// além de multiplicar pelo peso, deve-se multiplicar por metade do
			// tamanho do intervalo para compensar a mudança de intervalo
			out += Dt2 * ft * ds[2];
		}

		return out;
	}

	// ------------------------ EQUAÇÕES DIFERENCIAIS ------------------------

	/**
	 * Função que efetua a integração numérica da {@link Funcao derivada de uma
	 * função} sobre um passo de tempo por um dos métodos da família Runge-Kutta,
	 * definido pela tabela de Butcher (ver <a href=
	 * "https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta_methods">Runge–Kutta
	 * methods</a>)
	 * 
	 * @param dfdt derivada da função a ser integrada
	 * @param rkc  Butcher tableau, c's coefficients
	 * @param rka  Butcher tableau, a's coefficients
	 * @param rkb1 Butcher tableau, b1's coefficients
	 * @param rkb2 Butcher tableau, b2's coefficients
	 * @param x0   condições iniciais
	 * @param t0   tempo inicial
	 * @param dt   passo de integração
	 * @param vars variáveis intermediárias da função
	 * @return valor da função no próximo instante de tempo
	 */
	public static double[] integraRungeKutta(Funcao dfdt, double[] rkc, double[][] rka, double[] rkb1, double[] rkb2,
			double[] x0, double t0, double dt, double[] vars) {
		double[][] ki = new double[rkc.length][x0.length];

		ki[0] = Vec.mult(dt, dfdt.ft(x0, t0, vars));
		for (int i = 1; i < rkc.length; i++) {
			double[] ks = new double[x0.length];
			for (int j = 0; j < rka[i - 1].length; j++)
				ks = Vec.sum(ks, Vec.mult(rka[i - 1][j], ki[j]));
			ki[i] = Vec.mult(dt, dfdt.ft(Vec.sum(x0, ks), t0 + rkc[i] * dt, vars));
		}

		double[] out = Arrays.copyOf(x0, x0.length);
		for (int i = 0; i < rkb1.length; i++)
			out = Vec.sum(out, Vec.mult(rkb1[i], ki[i]));

//		if (rkb2 != null) { // rkb2, quando não nulo, oferece uma estimativa do erro
//			double[] lo = Arrays.copyOf(x0, x0.length);
//			for (int i = 0; i < rkb2.length; i++)
//				lo = Vec.sum(lo, Vec.mult(rkb2[i], ki[i]));
//			// comparar e ver se precisa adaptar o passo (adaptative step)
//		}
		return out;
	}

	// ------------------------ RK4 - Runge-Kutta 4 ------------------------

	private static final double[] RK4_C = { 0, 0.5, 0.5, 1 }, RK4_A[] = { { 0.5 }, { 0, 0.5 }, { 0, 0, 1 } },
			RK4_B = { .16666667, .33333333, .33333333, .16666667 };

	/**
	 * Função que efetua a integração numérica da {@link Funcao derivada de uma
	 * função} sobre um passo de tempo pelo método RK4
	 * 
	 * @param dfdt derivada da função a ser integrada
	 * @param x0   condições iniciais
	 * @param t0   tempo inicial
	 * @param dt   passo de integração
	 * @param vars variáveis intermediárias da função
	 * @return valor da função no próximo instante de tempo
	 */
	public static double[] integraRungeKutta4(Funcao dfdt, double[] x0, double t0, double dt, double[] vars) {
		return integraRungeKutta(dfdt, RK4_C, RK4_A, RK4_B, null, x0, t0, dt, vars);
	}

	/**
	 * Função que efetua a integração numérica da {@link Funcao derivada de uma
	 * função} sobre um passo de tempo pelo método RK4
	 * 
	 * @param dfdt derivada da função a ser integrada
	 * @param x0   condições iniciais
	 * @param t0   tempo inicial
	 * @param dt   passo de integração
	 * @param vars variáveis intermediárias da função
	 * @return valor da função no próximo instante de tempo
	 */
	public static double[] integraRungeKutta4fast(Funcao dfdt, double[] x0, double t0, double dt, double[] vars) {
		double[][] ki = new double[4][x0.length];
		ki[0] = Vec.mult(dt, dfdt.ft(x0, t0, vars));
		ki[1] = Vec.mult(dt, dfdt.ft(Vec.sum(x0, Vec.mult(0.5, ki[0])), t0 + 0.5 * dt, vars));
		ki[2] = Vec.mult(dt, dfdt.ft(Vec.sum(x0, Vec.mult(0.5, ki[1])), t0 + 0.5 * dt, vars));
		ki[3] = Vec.mult(dt, dfdt.ft(Vec.sum(x0, ki[2]), t0 + dt, vars));
		return Vec.sum(x0, Vec.mult(0.16666667, ki[0]), Vec.mult(0.33333333, ki[1]), Vec.mult(0.33333333, ki[2]),
				Vec.mult(0.16666667, ki[3]));
	}

	// ------------------------ ode45 - Dormand-Prince ------------------------

	private static final double[] DP_C = { 0, .2, .3, .8, .88888888888889, 1., 1. }, DP_A[] = { { .2 }, { .075, .225 },
			{ .9777777777777778, -3.733333333333333, 3.555555555555556 },
			{ 2.952598689224204, -11.59579332418839, 9.822892851699436, -.2908093278463649 },
			{ 2.846275252525253, -10.75757575757576, 8.906422717743472, .2784090909090909, -0.2735313036020583 },
			{ 0.0911458333333333, 0, .4492362982929021, .6510416666666667, -0.322376179245283, 0.130952380952381 } },
			DP_B1 = Arrays.copyOf(DP_A[5], DP_A[5].length + 1), DP_B2 = { 0.0899131944444444, 0, 0.4534890685834082,
					0.6140625, -0.2715123820754717, 0.089047619047619, 0.025 };

	/**
	 * Função que efetua a integração numérica da {@link Funcao derivada de uma
	 * função} sobre um passo de tempo pelo método RKDP (ode45)
	 * 
	 * @param dfdt derivada da função a ser integrada
	 * @param x0   condições iniciais
	 * @param t0   tempo inicial
	 * @param dt   passo de integração
	 * @param vars variáveis intermediárias da função
	 * @return valor da função no próximo instante de tempo
	 */
	public static double[] integraDormandPrince(Funcao dfdt, double[] x0, double t0, double dt, double[] vars) {
		return integraRungeKutta(dfdt, DP_C, DP_A, DP_B1, DP_B2, x0, t0, dt, vars);
	}

}
