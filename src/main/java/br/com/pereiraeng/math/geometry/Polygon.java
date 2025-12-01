package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Classe do objeto que representa um polígono ou uma polilinha
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Polygon {

	private boolean close;

	public boolean isClose() {
		return close;
	}

	public abstract List<? extends Point2D> getPoints();

	public abstract int size();

	public abstract Point2D get(int index);

	public static class Float extends Polygon {

		private LinkedList<Point2D.Float> points;

		public Float(List<Point2D.Float> vertexesList) {
			points = new LinkedList<>(vertexesList);
			super.close = points.getFirst().equals(points.getLast());
			if (super.close)
				points.removeLast();
		}

		public Float(Collection<Point2D.Float[]> edgeList) {
			points = new LinkedList<>();
			super.close = edges2vertexesF(points, edgeList);
			if (super.close)
				points.removeLast();
		}

		public Float(boolean close, Point2D.Float... ps) {
			this.points = new LinkedList<>();
			super.close = close;
			for (int i = 0; i < ps.length; i++)
				this.points.add(ps[i]);
		}

		@Override
		public List<? extends Point2D> getPoints() {
			return points;
		}

		@Override
		public int size() {
			return points.size();
		}

		@Override
		public Point2D get(int index) {
			return points.get(index);
		}
	}

	public static class Double extends Polygon {

		private LinkedList<Point2D.Double> points;

		public Double(List<Point2D.Double> vertexesList) {
			points = new LinkedList<>(vertexesList);
			super.close = points.getFirst().equals(points.getLast());
			if (super.close)
				points.removeLast();
		}

		public Double(Collection<Point2D.Double[]> edgeList) {
			points = new LinkedList<>();
			super.close = edges2vertexes(points, edgeList);
			if (super.close)
				points.removeLast();
		}

		public Double(boolean close, Point2D.Double... ps) {
			points = new LinkedList<>();
			super.close = close;
			for (int i = 0; i < ps.length; i++)
				this.points.add(ps[i]);
		}

		@Override
		public List<? extends Point2D> getPoints() {
			return points;
		}

		@Override
		public int size() {
			return points.size();
		}

		@Override
		public Point2D get(int index) {
			return points.get(index);
		}
	}

	// ---------------------------------------------------------------------

	/**
	 * Função que converte uma relação de arestas em um polígono
	 * 
	 * @param edges relação de arestas
	 * @return
	 */
	public static List<List<? super Point2D.Double>> edges2vertexes(Collection<? extends Point2D.Double[]> edges) {
		List<List<? super Point2D.Double>> out = new LinkedList<>();
		while (edges.size() > 0) {
			List<? super Point2D.Double> vls = new LinkedList<>();
			edges2vertexes(vls, edges);
			out.add(vls);
		}
		return out;
	}

	/**
	 * Função que converte uma relação de arestas em um polígono
	 * 
	 * @param out   lista ordenada de vértices que formam um polígono
	 * @param edges relação de arestas
	 * @return <code>true</code> se a lista de vértices forma um polígono fechado,
	 *         <code>false</code> se a lista de vértices não fecha, sendo assim uma
	 *         polilinha
	 */
	public static boolean edges2vertexes(List<? super Point2D.Double> out,
			Collection<? extends Point2D.Double[]> edges) {
		Iterator<? extends Point2D.Double[]> it = edges.iterator();

		Point2D.Double[] ps = it.next();
		it.remove();
		Point2D.Double previous = ps[0], next = ps[1];
		out.add(previous);
		out.add(next);

		int size = edges.size();
		while (true) {
			while (it.hasNext()) { // para cada aresta...
				ps = it.next();
				// verificar se o primeiro vértice da aresta já está no polígono...
				boolean flag = previous.equals(ps[0]);
				if (flag || next.equals(ps[0])) {
					if (flag)
						out.add(0, previous = ps[1]);
					else
						out.add(out.size(), next = ps[1]);
					it.remove();
				} else {
					// verificar se o segundo vértice da aresta já está no polígono...
					flag = previous.equals(ps[1]);
					if (flag || next.equals(ps[1])) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
						it.remove();
					}
				}
			}

			if (edges.size() > 0)
				it = edges.iterator();
			else
				break;

			if (size == edges.size()) {
				// se ninguém foi removido, tem algo errado...
				Point2D.Double[] edge = edges.iterator().next();
				boolean flag = edge[0].getX() == previous.getX() || edge[0].getY() == previous.getY();
				if (flag || edge[0].getX() == next.getX() || edge[0].getY() == next.getY()) {
					if (flag)
						out.add(0, previous = ps[0]);
					else
						out.add(out.size(), next = ps[0]);
				} else {
					flag = edge[1].getX() == previous.getX() || edge[1].getY() == previous.getY();
					if (flag || edge[1].getX() == next.getX() || edge[1].getY() == next.getY()) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
					} else // Há arestas que não estão conectadas com as demais
						break;
				}
			} else
				size = edges.size();
		}
		return previous.equals(next);
	}

	// ---------------------------------------------------------------------

	public static List<List<Point2D.Float>> edges2vertexesF(Collection<? extends Point2D.Float[]> edges) {
		List<List<Point2D.Float>> out = new LinkedList<>();
		while (edges.size() > 0) {
			List<Point2D.Float> vls = new LinkedList<>();
			edges2vertexesF(vls, edges);
			out.add(vls);
		}
		return out;
	}

	/**
	 * Função que converte uma relação de arestas em um polígono
	 * 
	 * @param out   lista ordenada de vértices que formam um polígono
	 * @param edges relação de arestas
	 * @return <code>true</code> se a lista de vértices forma um polígono fechado,
	 *         <code>false</code> se a lista de vértices não fecha, sendo assim uma
	 *         polilinha
	 */
	public static boolean edges2vertexesF(List<? super Point2D.Float> out,
			Collection<? extends Point2D.Float[]> edges) {
		Iterator<? extends Point2D.Float[]> it = edges.iterator();

		Point2D.Float[] ps = it.next();
		it.remove();
		Point2D.Float previous = ps[0], next = ps[1];
		out.add(previous);
		out.add(next);

		int size = edges.size();
		while (true) {
			while (it.hasNext()) { // para cada aresta...
				ps = it.next();
				// verificar se o primeiro vértice da aresta já está no polígono...
				boolean flag = previous.equals(ps[0]);
				if (flag || next.equals(ps[0])) {
					if (flag)
						out.add(0, previous = ps[1]);
					else
						out.add(out.size(), next = ps[1]);
					it.remove();
				} else {
					// verificar se o segundo vértice da aresta já está no polígono...
					flag = previous.equals(ps[1]);
					if (flag || next.equals(ps[1])) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
						it.remove();
					}
				}
			}

			if (edges.size() > 0)
				it = edges.iterator();
			else
				break;

			if (size == edges.size()) {
				// se ninguém foi removido, tem algo errado...
				Point2D.Float[] edge = edges.iterator().next();
				boolean flag = edge[0].getX() == previous.getX() || edge[0].getY() == previous.getY();
				if (flag || edge[0].getX() == next.getX() || edge[0].getY() == next.getY()) {
					if (flag)
						out.add(0, previous = ps[0]);
					else
						out.add(out.size(), next = ps[0]);
				} else {
					flag = edge[1].getX() == previous.getX() || edge[1].getY() == previous.getY();
					if (flag || edge[1].getX() == next.getX() || edge[1].getY() == next.getY()) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
					} else // Há arestas que não estão conectadas com as demais
						break;
				}
			} else
				size = edges.size();
		}
		return previous.equals(next);
	}

	// ---------------------------------------------------------------------

	public static <K> List<List<? super K>> edges2vertexesK(Collection<K[]> edges) {
		List<List<? super K>> out = new LinkedList<>();
		while (edges.size() > 0) {
			List<? super K> vls = new LinkedList<>();
			edges2vertexesK(vls, edges);
			out.add(vls);
		}
		return out;
	}

	/**
	 * Função que converte uma relação de arestas em um polígono
	 * 
	 * @param <K>   classe do objeto que representa um vértice
	 * @param out   lista ordenada de vértices que formam um polígono
	 * @param edges relação de arestas
	 * @return <code>true</code> se a lista de vértices forma um polígono fechado,
	 *         <code>false</code> se a lista de vértices não fecha, sendo assim uma
	 *         polilinha
	 */
	public static <K> boolean edges2vertexesK(List<? super K> out, Collection<K[]> edges) {
		Iterator<? extends K[]> it = edges.iterator();

		K[] ps = it.next();
		it.remove();
		K previous = ps[0], next = ps[1];
		out.add(previous);
		out.add(next);

		int size = edges.size();
		while (true) {
			while (it.hasNext()) { // para cada aresta...
				ps = it.next();
				// verificar se o primeiro vértice da aresta já está no polígono...
				boolean flag = previous.equals(ps[0]);
				if (flag || next.equals(ps[0])) {
					if (flag)
						out.add(0, previous = ps[1]);
					else
						out.add(out.size(), next = ps[1]);
					it.remove();
				} else {
					// verificar se o segundo vértice da aresta já está no polígono...
					flag = previous.equals(ps[1]);
					if (flag || next.equals(ps[1])) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
						it.remove();
					}
				}
			}

			if (edges.size() > 0)
				it = edges.iterator();
			else
				break;

			if (size == edges.size()) {
				// se ninguém foi removido, tem algo errado...
				K[] edge = edges.iterator().next();
				boolean flag = edge[0].equals(previous);
				if (flag || edge[0].equals(next)) {
					if (flag)
						out.add(0, previous = ps[0]);
					else
						out.add(out.size(), next = ps[0]);
				} else {
					flag = edge[1].equals(previous);
					if (flag || edge[1].equals(next)) {
						if (flag)
							out.add(0, previous = ps[0]);
						else
							out.add(out.size(), next = ps[0]);
					} else // Há arestas que não estão conectadas com as demais
						break;
				}
			} else
				size = edges.size();
		}
		return previous.equals(next);
	}

	/**
	 * Função que converte uma relação de arestas em uma polilinha. As arestas foram
	 * uma grafo conexo e a procura é feita por BFS
	 * 
	 * @param <K>   classe do objeto que representa um vértice
	 * @param out   lista ordenada de vértices que formam uma polilinha
	 * @param edges relação de arestas
	 * @param part  vértice de partida
	 * @param dest  vértice de chegada
	 */
	public static <K> void edges2vertexesK(List<? super K> out, Collection<K[]> edges, K part, K dest) {

		Set<K> discovered = new HashSet<>();
		discovered.add(part);

		LinkedList<K> gp = new LinkedList<>();
		gp.add(part);

		Queue<LinkedList<K>> queue = new ArrayDeque<>();
		queue.add(gp);

		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			LinkedList<K> gp0 = queue.poll();
			K v0 = gp0.getLast();

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Set<K[]> eds = getEdges(v0, edges);
			for (K[] e : eds) {

				K v1 = e[0].equals(v0) ? e[1] : e[0];

				if (v1.equals(dest)) {
					gp0.add(v1);
					out.addAll(gp0);
					return;
				}

				// Else, continue to do BFS
				if (!discovered.contains(v1)) {
					discovered.add(v1);

					LinkedList<K> gp1 = new LinkedList<>();
					gp1.addAll(gp0);
					gp1.add(v1);
					queue.add(gp1);
				}
			}
		}
	}

	private static <K> Set<K[]> getEdges(K k, Collection<K[]> edges) {
		Set<K[]> out = new HashSet<>();
		for (K[] ks : edges)
			if (k.equals(ks[0]) || k.equals(ks[1]))
				out.add(ks);
		return out;
	}

	public static <K> void merge(List<? extends List<K>> out) {
		Iterator<? extends List<K>> it0 = out.iterator();
		int i = 0;
		while (it0.hasNext()) {
			List<K> l0 = it0.next();
			K pF0 = l0.get(0);
			K pL0 = l0.get(l0.size() - 1);

			Iterator<? extends List<K>> it1 = out.iterator();
			int j = 0;
			boolean r = false;
			while (it1.hasNext()) {
				List<K> l1 = it1.next();
				if (j > i) {
					K pF1 = l1.get(0);
					K pL1 = l1.get(l1.size() - 1);

					boolean m = false;
					if (pF0.equals(pF1)) {
						l0.remove(0);
						for (int k = 0; k < l0.size(); k++)
							l1.add(0, l0.get(k));
						m = true;
					} else if (pF0.equals(pL1)) {
						l0.remove(0);
						l1.addAll(l0);
						m = true;
					} else if (pL0.equals(pF1)) {
						l0.remove(l0.size() - 1);
						l1.addAll(0, l0);
						m = true;
					} else if (pL0.equals(pL1)) {
						l0.remove(l0.size() - 1);
						for (int k = l0.size() - 1; k >= 0; k--)
							l1.add(l0.get(k));
						m = true;
					}
					if (m) {
						r = true;
						it0.remove();
						break;
					}
				}
				j++;
			}
			if (!r)
				i++;
		}
	}

	// ---------------------------------------------------------------------

	/**
	 * Função que indica se um ponto está dentro ou fora de um polígono. É
	 * necessário informar, um ponto que já esteja dentro do polígono
	 * 
	 * @param polygon lista de coordenadas dos vértices
	 * @param inside  ponto que está dentro do polígono
	 * @param p       ponto que se deseja saber se está, ou não, dentro do polígono
	 * @return <code>true</code> se estiver dentro, <code>false</code> se não está.
	 */
	public static boolean inside(Collection<? extends Point2D.Float> polygon, Point2D.Float inside, Point2D.Float p) {
		final Line l = new Line(inside, p);
		int i = 0;

		Iterator<? extends Point2D.Float> it = polygon.iterator();
		Point2D.Float first = it.next();
		Point2D.Float last = first;
		Line l0 = null;
		while (it.hasNext()) {
			Point2D.Float p0 = it.next();
			l0 = new Line(last, p0);
			if (l0.intersectionB(l))
				i++;
			last = p0;
		}
		l0 = new Line(last, first);
		if (l0.intersectionB(l))
			i++;

		return i % 2 == 0;
	}

	/**
	 * Função que calcula as coordenadas do centro geométrico de um conjunto de
	 * polígonos
	 * 
	 * @param polygons lista de polígonos, cada um deles descrito pela lista de
	 *                 coordenadas dos vértices
	 * @return coordenadas do centro geométrico
	 */
	public static Point2D.Double getCentroid(List<? extends List<? extends Point2D>> polygons) {
		Point2D.Double out = new Point2D.Double();
		double total = 0.;
		for (List<? extends Point2D> polygon : polygons) {
			Point2D.Double c = new Point2D.Double();
			double w = getCentroid(polygon, c);
			if (w != 0.) {
				out.setLocation(out.x + c.x * w, out.y + c.y * w);
				total += w;
			}
		}
		if (total != 0.)
			out.setLocation(out.x / total, out.y / total);
		return out;
	}

	/**
	 * Função que calcula as coordenadas do centro geométrico de um polígono,
	 * descrito pela lista de coordenadas dos seus vértices.
	 * <a href="https://en.wikipedia.org/wiki/Centroid#Of_a_polygon">Ver artigo</a>
	 * 
	 * @param polygon lista de coordenadas dos vértices
	 * @param c       coordenadas do centro geométrico
	 * @return área do polígono (sub-produto do cálculo, que pode ser útil em outra
	 *         função)
	 */
	public static double getCentroid(List<? extends Point2D> polygon, Point2D.Double c) {
		if (polygon.size() == 2) {
			c.setLocation(Line.getMidPoint(polygon.get(0), polygon.get(1)));
			return 0.;
		}
		double cx = 0., cy = 0., a = 0.;
		for (int i = 0; i < polygon.size(); i++) {
			Point2D pi = polygon.get(i);
			Point2D pi1 = polygon.get((i + 1) % polygon.size());

			double shoelace = pi.getX() * pi1.getY() - pi1.getX() * pi.getY();

			cx += (pi.getX() + pi1.getX()) * shoelace;
			cy += (pi.getY() + pi1.getY()) * shoelace;
			a += shoelace;
		}

		a /= 2.;
		c.setLocation(cx / (6 * a), cy / (6 * a));
		return a;
	}

	/**
	 * Função que calcula a área de um polígono.
	 * <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Ver artigo</a>
	 * 
	 * @param polygon lista de coordenadas dos vértices
	 * @return área do polígono
	 */
	public static double area(List<? extends Point2D> polygon) {
		double a = 0.;
		for (int i = 0; i < polygon.size(); i++) {
			Point2D pi = polygon.get(i);
			int j = i + 1;
			if (j == polygon.size())
				j = 0;
			Point2D pi1 = polygon.get(j);
			double shoelace = pi.getX() * pi1.getY() - pi1.getX() * pi.getY();
			a += shoelace;
		}
		return a / 2.;
	}

	/**
	 * Função que indica se os pontos que delimitam um polígono estão dispostos no
	 * sentido horário ou anti-horário
	 * 
	 * @param ps lista de coordenadas dos vértices
	 * @return 1 para horário, menos -1 para anti-horário
	 */
	public static double clockwise(Point2D.Double... ps) {
		double out = 0.;
		for (int i = 0; i < ps.length; i++) {
			int j = i + 1;
			if (j == ps.length)
				j = 0;
			Point2D.Double p2 = ps[j];
			Point2D.Double p1 = ps[i];
			out += (p1.x - p2.x) * (p2.y + p1.y);
		}
		return Math.signum(out);
	}
}
