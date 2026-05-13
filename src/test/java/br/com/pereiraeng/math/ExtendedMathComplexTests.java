package br.com.pereiraeng.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExtendedMathComplexTests {

	@Test
	void testTrigonometric() {
		// reference values from WolframAlpha

		Complex c = new Complex(1, 2);

		Complex f = ExtendedMathComplex.cos(c);
		assertEquals(2.0327230070196656, f.getRe());
		assertEquals(-3.0518977991518, f.getIm());

		f = ExtendedMathComplex.sin(c);
		assertEquals(3.165778513216168, f.getRe());
		assertEquals(1.9596010414216058, f.getIm(), 1e-15);

		f = ExtendedMathComplex.tan(c);
		assertEquals(.03381282607989669, f.getRe());
		assertEquals(1.0147936161466335, f.getIm());

		f = ExtendedMathComplex.cosh(c);
		assertEquals(-0.64214812471552, f.getRe(), 1e-15);
		assertEquals(1.0686074213827783, f.getIm());

		f = ExtendedMathComplex.sinh(c);
		assertEquals(-0.4890562590412937, f.getRe());
		assertEquals(1.4031192506220407, f.getIm());

		f = ExtendedMathComplex.tanh(c);
		assertEquals(1.16673625724092, f.getRe());
		assertEquals(-0.24345820118572525, f.getIm(), 1e-15);
	}

	@Test
	void testExponencial() {
		// reference values from WolframAlpha

		Complex c = new Complex(1, 2);

		Complex f = ExtendedMathComplex.exp(c);
		assertEquals(-1.1312043837568135, f.getRe());
		assertEquals(2.4717266720048188, f.getIm());

		f = ExtendedMathComplex.ln(c);
		assertEquals(.8047189562170501, f.getRe(), 1e-15);
		assertEquals(1.1071487177940904, f.getIm());
	}

	@Test
	void testBessel() {
		// reference values from WolframAlpha

		Complex c = new Complex(1, 0.5);

		Complex f = ExtendedMathComplex.besselPol(c, 3);
		assertEquals(22, f.getRe(), 1e-14);
		assertEquals(38.625, f.getIm());

		f = ExtendedMathComplex.revBesselPol(c, 3);
		assertEquals(34.75, f.getRe());
		assertEquals(14.875, f.getIm(), 1e-14);
	}
}
