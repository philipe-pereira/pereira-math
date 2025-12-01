package br.com.pereiraeng.math;

import java.util.Locale;

import br.com.pereiraeng.core.ExtendedMath;

public class Angle {

	/**
	 * Função que converte os valores de um ângulo em graus, minutos e segundos para
	 * um número decimal
	 * 
	 * @param degrees graus, expressos por um número inteiro
	 * @param minutes minutos, expressos por um número inteiro
	 * @param seconds segundos, expressos por um número decimal
	 * @return valor do ângulo na forma decimal
	 */
	public static float toFloatDegrees(int degrees, int minutes, float seconds) {
		if (degrees != 0)
			return ExtendedMath
					.circularDegree(Math.signum(degrees) * (Math.abs(degrees) + minutes / 60.0f + seconds / 3600.0f));
		else if (minutes != 0)
			return ExtendedMath.circularDegree(Math.signum(minutes) * (Math.abs(minutes) / 60.0f + seconds / 3600.0f));
		else if (seconds != 0)
			return ExtendedMath.circularDegree(seconds / 3600.0f);
		else
			return .0f;
	}

	/**
	 * Função que calcula quantos graus tem um ângulo expresso na forma decimal
	 * 
	 * @param angle valor do ângulo expresso na forma decimal
	 * @return valor dos graus do ângulo
	 */
	public static int getDegrees(double angle) {
		return (int) angle;
	}

	/**
	 * Função que calcula quantos minutos tem um ângulo expresso na forma decimal
	 * 
	 * @param angle valor do ângulo expresso na forma decimal
	 * @return valor dos minutos do ângulo
	 */
	public static int getMinutes(double angle) {
		return Math.abs(((int) (angle * 60)) % 60);
	}

	/**
	 * Função que calcula quantos segundos tem um ângulo expresso na forma decimal
	 * 
	 * @param angle valor do ângulo expresso na forma decimal
	 * @return valor dos segundos do ângulo
	 */
	public static double getSeconds(double angle) {
		return Math.abs(((angle * 3600) % 3600) % 60);
	}

	public static final char DEGREE = '\u00B0';
	
	/**
	 * Função que retorna uma sequência de caracteres representativa de um ângulo
	 * expresso na forma de graus, minutos e segundos
	 * 
	 * @param degrees graus, expressos por um número inteiro
	 * @param minutes minutos, expressos por um número inteiro
	 * @param seconds segundos, expressos por um número decimal
	 * @return <code>String</code> representando o ângulo
	 */
	public static String toStringAngle(int degrees, int minutes, double seconds) {
		boolean sec = 60 - seconds < 0.5f;
		int m = minutes + (sec ? 1 : 0);

		String out = String.format(Locale.US, "%d%c%02d'%04.1f''", degrees + (m == 60 ? 1 : 0), DEGREE, (m == 60 ? 0 : m),
				sec ? 0 : seconds);

		if (seconds < 0.5f || sec) { // truncar, caso for nulo
			out = out.substring(0, out.length() - 4);
			if (m == 0 || m == 60 || m == -60)
				out = out.substring(0, out.length() - 3);
		}
		return out;
	}
}
