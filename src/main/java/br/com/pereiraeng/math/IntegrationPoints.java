package br.com.pereiraeng.math;

public class IntegrationPoints {

	/**
	 * Enumeração dos possíveis intervalos de integração
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public enum Shape {
		SEGMENTO("segmento"), TRIANGULO("triângulo"), QUADRADO("quadrado");

		private String name;

		private Shape(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Enumeração das regras de quadratura
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public enum Quadrature {
		GAUSS("Gauss"), SIMPSON("Simpson"), NEWTON_COTES("Newton-Cotes");

		private String name;

		private Quadrature(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final double FPG[][][] = new double[][][] {
			{ { 0., 2. } },
			{ { 0.577350269189626, 1. } },
			{ { 0., 0.888888888888889 },
					{ 0.774596669241483, 0.555555555555556 } },
			{ { 0.339981043584856, 0.652145154862546 },
					{ 0.861136311594053, 0.347854845137454 } },
			{ { 0., 0.568888888888889 },
					{ 0.538469310105683, 0.478628670499366 },
					{ 0.906179845938664, 0.236926885056189 } },
			{ { 0.238619186083197, 0.467913934572691 },
					{ 0.661209386466265, 0.360761573048139 },
					{ 0.932469514203152, 0.171324492379170 } },
			{ { 0., 0.417959183673469 },
					{ 0.405845151377397, 0.381830050505119 },
					{ 0.741531185599394, 0.279705391489277 },
					{ 0.949107912342759, 0.129484966168870 } },
			{ { 0.183434642495650, 0.362683783378362 },
					{ 0.525532409916329, 0.313706645877887 },
					{ 0.796666477413627, 0.222381034453374 },
					{ 0.960289856497536, 0.101228536290376 } },
			{ { 0., 0.330239355001260 },
					{ 0.324253423403809, 0.312347077040003 },
					{ 0.613371432700590, 0.260610696402935 },
					{ 0.836031107326636, 0.180648160694857 },
					{ 0.968160239507626, 0.081274388361574 } },
			{ { 0.148874338981631, 0.295524224714753 },
					{ 0.433395394129247, 0.269266719309996 },
					{ 0.679409568299024, 0.219086362515982 },
					{ 0.865063366688985, 0.149451349150581 },
					{ 0.973906528517172, 0.066671344308688 } } },
			FPGT[][][] = {
					{ { 0., 0., 0.5 } },
					{ { 0.666666666666667, 0., 0.166666666666667 } },
					{ { 0., 0., -0.28125 }, { 0.6, 0., 0.260416666666667 } },
					{ { 0.108103018168070, 0., 0.111690794839006 },
							{ 0.816847572980459, 0., 0.054975871827661 } },
					{ { 0., 0., 0.1125 },
							{ 0.059715871789770, 0., 0.066197076394253 },
							{ 0.797426985353087, 0., 0.062969590272414 } },
					{
							{ 0.501426509658179, 0., 0.058393137863190 },
							{ 0.873821971016996, 0., 0.025422453185104 },
							{ 0.053145049844817, 0.310352451033784,
									0.041425537809187 } },
					{
							{ 0., 0., -0.074785022233841 },
							{ 0.479308067841920, 0., 0.087807628716604 },
							{ 0.869739794195568, 0., 0.026673617804419 },
							{ 0.048690315425316, 0.312865496004874,
									0.038556880445129 } },
					{
							{ 0., 0., 0.072157803838894 },
							{ 0.081414823414554, 0., 0.047545817133643 },
							{ 0.658861384496480, 0., 0.051608685267359 },
							{ 0.898905543365938, 0., 0.016229248811599 },
							{ 0.008394777409958, 0.263112829634638,
									0.013615157087218 } },
					{
							{ 0., 0., 0.048567898141400 },
							{ 0.020634961602525, 0., 0.015667350113570 },
							{ 0.125820817014127, 0., 0.038913770502387 },
							{ 0.623592928761935, 0., 0.039823869463605 },
							{ 0.910540973211095, 0., 0.012788837829349 },
							{ 0.036838412054736, 0.221962989160766,
									0.021641769688645 } },
					{
							{ 0., 0., 0.045408995191377 },
							{ 0.028844733232685, 0., 0.018362978878234 },
							{ 0.781036849029926, 0., 0.022660529717764 },
							{ 0.141707219414880, 0.307939838764121,
									0.036378958422710 },
							{ 0.025003534762686, 0.246672560639903,
									0.014163621265529 },
							{ 0.009540815400299, 0.066803251012200,
									0.004710833481867 } } },
			PNC[][] = new double[][] {
					{ 0.25, 0.75 },
					{ 0.155555555555556, 0.711111111111111, 0.266666666666667 },
					{ 0.131944444444444, 0.520833333333333, 0.347222222222222 },
					{ 0.097619047619048, 0.514285714285714, 0.064285714285714,
							0.647619047619048 },
					{ 0.086921296296296, 0.414004629629630, 0.153125,
							0.345949074074074 },
					{ 0.069770723104057, 0.415379188712522, -0.065467372134039,
							0.740458553791887, -0.320282186948854 },
					{ 0.063772321428572, 0.351361607142857, 0.024107142857143,
							0.431785714285714, 0.128973214285714 },
					{ 0.053668296723852, 0.355071882849661, -0.162087141253808,
							0.909892576559243, -0.870310245310245,
							1.427529260862590 } }, PNCT[][] = new double[][] {
					{ 0.016666666666667, 0.0375, 0.225 },
					{ 0., 0.044444444444445, -0.011111111111111,
							0.088888888888889 },
					{ 0.005456349206349, 0.012400793650794, 0.012400793650794,
							0.099206349206349, 0.012400793650794 },
					{ 0., 0.021428571428572, -0.016071428571429,
							0.042857142857143, 0.038095238095238,
							0.042857142857143, -0.032142857142857 },
					{ 0.002577160493827, 0.005765817901235, 0.006900077160494,
							0.062195216049383, 0.005198688271605,
							-0.013233024691358, 0.086014660493827,
							0.006616512345679 },
					{ 0., 0.012980599647266, -0.016507936507937,
							0.024832451499118, 0.040070546737213,
							0.029347442680776, -0.038201058201058,
							0.023703703703704, -0.051075837742504,
							0.051922398589065 } };

	/**
	 * Função que retorna o número de pontos para os quais deve-se calcular o
	 * valor da função de modo a se proceder com a integração numérica do
	 * intervalo
	 * 
	 * @param shape
	 *            formato do intervalo de integração
	 * @param quad
	 *            regra de quadratura
	 * @param order
	 *            ordem da integração
	 * @return número de pontos a serem calculados
	 */
	public static int np(Shape shape, Quadrature quad, int order) {
		int n = 0;

		int[] fpgt = { 1, 3, 4, 6, 7, 12, 13, 16, 19, 25 };

		switch (quad) {
		case GAUSS:
			switch (shape) {
			case SEGMENTO:
				n = order;
				break;
			case TRIANGULO:
				n = fpgt[order - 1];
				break;
			case QUADRADO:
				n = order * order;
				break;
			default:
				break;
			}
			break;
		case SIMPSON:
			switch (shape) {
			case SEGMENTO:
				n = 2 * order + 1;
				break;
			case TRIANGULO:
				n = 2 * (order * order) + 3 * order + 1;
				break;
			case QUADRADO:
				n = (2 * order + 1) * (2 * order + 1);
				break;
			default:
				break;
			}
			break;
		case NEWTON_COTES:
			switch (shape) {
			case SEGMENTO:
				n = order + 1;
				break;
			case TRIANGULO:
				n = (order + 1) * (order + 2) / 2;
				break;
			case QUADRADO:
				n = (order + 1) * (order + 1);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return n;
	}

	/**
	 * Função que retorna as coordenadas do intervalo onde deve-se calcular o
	 * valor da função a ser integrada e o peso que deve multiplicar o valor
	 * calculado
	 * 
	 * @param shape
	 *            formato do intervalo de integração
	 * @param quad
	 *            regra de quadratura
	 * @param order
	 *            ordem da integração
	 * @param i
	 *            índice do ponto (<strong>este argumento deve ser um número
	 *            entre 1 e o
	 *            {@link IntegrationPoints#np(Shape, Quadrature, int) número de
	 *            pontos}</strong>)
	 * @return vetor contendo nas suas duas primeiras posições as coordendas do
	 *         ponto a ser calculada a função dentro do interavalo normalizado
	 *         [-1;1]x[-1;1], e na terceira posição o peso
	 */
	public static double[] xyw(Shape shape, Quadrature quad, int order, int i) {
		double XPG = 0., YPG = 0., HPG = 0.;

		int H, I = 0, J;

		switch (quad) {
		case GAUSS:
			switch (shape) {
			case SEGMENTO:
				H = (order + 1) / 2;
				I = (i % H);

				XPG = ((2 * (i / (H + 1)) - 1) >= 0 ? 1 : -1)
						* (FPG[order - 1][I][0]);
				YPG = 0.;
				HPG = FPG[order - 1][I][1];
				break;
			case TRIANGULO:
				int N = i;
				H = 0;
				while (N > 0) {
					H++;
					if (FPGT[order - 1][H - 1][0] == 0) {
						N = N - 1;
						I = 0;
					} else if (FPGT[order - 1][H - 1][1] == 0) {
						N = N - 3;
						I = 1;
					} else {
						N = N - 6;
						I = 2;
					}
				}
				N = 1 - N;

				HPG = FPGT[order - 1][H - 1][2];

				if (I == 0) {
					XPG = 0.333333333333333;
					YPG = 0.333333333333333;
				} else if (I == 1) {
					if (N == 1) {
						XPG = FPGT[order - 1][H - 1][0];
						YPG = 0.5 - FPGT[order - 1][H - 1][0] / 2.;
					} else if (N == 2) {
						XPG = 0.5 - FPGT[order - 1][H - 1][0] / 2.;
						YPG = FPGT[order - 1][H - 1][0];
					} else if (N == 3) {
						XPG = 0.5 - FPGT[order - 1][H - 1][0] / 2.;
						YPG = XPG;
					}
				} else if (I == 2) {
					if (N == 1) {
						XPG = FPGT[order - 1][H - 1][0];
						YPG = FPGT[order - 1][H - 1][1];
					} else if (N == 2) {
						XPG = FPGT[order - 1][H - 1][1];
						YPG = FPGT[order - 1][H - 1][0];
					} else if (N == 3) {
						XPG = FPGT[order - 1][H - 1][0];
						YPG = 1. - FPGT[order - 1][H - 1][0]
								- FPGT[order - 1][H - 1][1];
					} else if (N == 4) {
						XPG = 1. - FPGT[order - 1][H - 1][0]
								- FPGT[order - 1][H - 1][1];
						YPG = FPGT[order - 1][H - 1][0];
					} else if (N == 5) {
						XPG = FPGT[order - 1][H - 1][1];
						YPG = 1. - FPGT[order - 1][H - 1][0]
								- FPGT[order - 1][H - 1][1];
					} else if (N == 6) {
						XPG = 1. - FPGT[order - 1][H - 1][0]
								- FPGT[order - 1][H - 1][1];
						YPG = FPGT[order - 1][H - 1][1];
					}
				}
				break;
			case QUADRADO:
				H = (order + 1) / 2;
				J = ((i - 1) % order) + 1;
				I = (J % H);

				XPG = ((2 * (J / (H + 1)) - 1) > 0 ? 1 : -1)
						* FPG[order - 1][I][0];
				HPG = FPG[order - 1][I][1];

				J = (i - 1) / order + 1;
				I = (J % H);

				YPG = ((2 * (J / (H + 1)) - 1) > 0 ? 1 : -1)
						* FPG[order - 1][I][0];
				HPG = HPG * FPG[order - 1][I][1];
				break;
			default:
				break;
			}
			break;
		case SIMPSON:
			switch (shape) {
			case SEGMENTO:
				int N = i - 1;

				XPG = -1. + ((double) N) / order;
				YPG = 0.;

				if ((N == 0) || (N == 2 * order)) {
					HPG = 1.;
				} else {
					if ((N % 2) == 0) {
						HPG = 2.;
					} else {
						HPG = 4.;
					}
				}
				HPG = HPG / (order * 3.);
				break;
			case TRIANGULO:
				H = 0;
				N = i;
				while (N > 0) {
					H = H + 1;
					N = N - H;
				}
				H = H - 1;
				I = -N;
				J = H + N;
				H = 2 * order;

				XPG = 0.5 * I / order;
				YPG = 0.5 * J / order;

				if ((I == 0) || (J == 0) || (I + J == H)) {
					if (I == 0) {
						if ((J == 0) || (J == H)) {
							HPG = 1.;
						} else {
							if ((J % 2) == 0) {
								HPG = 3.;
							} else {
								HPG = 4.;
							}
						}
					} else if (J == 0) {
						if (I == H) {
							HPG = 1.;
						} else {
							if ((I % 2) == 0) {
								HPG = 3.;
							} else {
								HPG = 4.;
							}
						}
					} else {
						if ((J % 2) == 0) {
							HPG = 3.;
						} else {
							HPG = 4.;
						}
					}
				} else {
					if (((I % 2) == 0) && ((J % 2) == 0)) {
						HPG = 6.;
					} else {
						HPG = 8.;
					}
				}
				HPG = HPG / ((order * order) * 30.);
				break;
			case QUADRADO:
				H = 2 * order + 1;
				I = (i - 1) % H;
				J = (i - 1) / H;
				H = H - 1;

				XPG = -1. + ((double) I) / order;
				YPG = -1. + ((double) J) / order;

				if ((I == 0) || (J == 0) || (I == H) || (J == H)) {
					if ((I == 0) || (I == H)) {
						if ((J == 0) || (J == H)) {
							HPG = 1.;
						} else {
							if ((J % 2) == 0) {
								HPG = 2.;
							} else {
								HPG = 4.;
							}
						}
					} else {
						if ((I % 2) == 0) {
							HPG = 2.;
						} else {
							HPG = 4.;
						}
					}
				} else {
					if (((I % 2) == 0) && ((J % 2) == 0)) {
						HPG = 4.;
					} else if (((I % 2) == 1) && ((J % 2) == 1)) {
						HPG = 16.;
					} else {
						HPG = 8.;
					}
				}
				HPG = HPG / ((order * order) * 9.);
				break;
			default:
				break;
			}
			break;
		case NEWTON_COTES:
			switch (shape) {
			case SEGMENTO:
				H = (order + 2) / 2;
				if (i <= H)
					I = i;
				else
					I = order - i + 2;

				XPG = -1. + 2. * (i - 1) / order;
				YPG = 0.;

				HPG = PNC[order - 3][I - 1];
				break;
			case TRIANGULO:
				int N = i;

				H = 0;
				while (N > 0) {
					H++;
					N -= H;
				}
				I = -N;
				J = H - 1 + N;

				XPG = ((double) I) / order;
				YPG = ((double) J) / order;

				H = Math.min(Math.min(I, J), order - I - J);
				I = order - Math.max(Math.max(I, J), order - I - J);

				J = order / 2;
				if (I < J)
					HPG = PNCT[order - 3][((I + 1) / 2) * (I / 2 + 1) + H];
				else
					HPG = PNCT[order - 3][((I + 1) / 2) * (I / 2 + 1) + H
							- (I - (order / 2)) * (I - ((order - 1) / 2))];
				break;
			case QUADRADO:
				H = (order + 2) / 2;

				J = (i - 1) % (order + 1) + 1;

				if (J <= H)
					I = J;
				else
					I = order - J + 2;

				XPG = -1. + 2. * (J - 1) / order;
				HPG = PNC[order - 3][I - 1];

				J = (i - 1) / (order + 1) + 1;
				if (J <= H)
					I = J;
				else
					I = order - J + 2;

				YPG = -1. + 2. * (J - 1) / order;
				HPG = HPG * PNC[order - 3][I - 1];
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return new double[] { XPG, YPG, HPG };
	}
}
