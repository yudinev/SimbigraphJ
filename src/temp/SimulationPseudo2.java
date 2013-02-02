package temp;

import java.awt.Color;
import java.util.Iterator;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.delaune.DelanuneyAgent;
import simbigraph.engine.SimState;
import edu.uci.ics.jung.graph.Graph;

public class SimulationPseudo2 extends Simulation {

	public class InfAgent extends DelanuneyAgent {
		int n_SIRstate, n_t;

		int SIRstate = 0;
		int t1 = 2, t2 = 6, t = 0;

		public Color getColor() {
			Color color = Color.BLACK;
			if (SIRstate == 0)
				color = Color.BLUE;
			else if (SIRstate == 1)
				color = Color.RED;
			else if (SIRstate == 2)
				color = Color.GRAY;
			return color;
		}

		public InfAgent(double x, double y) {
			super(x,y);

			if (Math.random() < 0.1) {

				SIRstate = 1;
				t = 1;
			}
		}

		public void step() {
			Graph graph = Context.getGraph();
			if (t == 0)
				n_SIRstate = 0;
			else if (t > 0 && t <= t1)
				n_SIRstate = 1;
			else if (t > t1 && t <= t2)
				n_SIRstate = 2;
			else if (t > t2) {
				n_SIRstate = 0;
				n_t = 0;
				t=0;
			}
			if (n_SIRstate == 0) {
				Iterator<Object> it = graph.getNeighbors(this).iterator();
				int k = 0, k_inf = 0;
				while (it.hasNext()) {
					k++;
					InfAgent n = (InfAgent) it.next();
					if (n.SIRstate == 1) {
						k_inf++;
					}
				}
				if (Math.random()  < 0.15*k_inf)
					n_t = 1;
			} else
				n_t = t + 1;
		}

		public void post_step() {
			t = n_t;
			SIRstate = n_SIRstate;
		}
	}

	public void step(SimState state) {
		Graph graph = Context.getGraph();
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			((InfAgent) iterator.next()).step();
		}

		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			((InfAgent) iterator.next()).post_step();
		}

	}

	public void start() {
		super.start();
		schedule.scheduleRepeating(this);
	}


	@Override
	public Color getAgentColor(Object obj) {
		Color col = ((InfAgent) obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
	}

	@Override
	public Factory getAgentFactory() {
		return new Factory<InfAgent>() {
			@Override
			public InfAgent create() {
				return new InfAgent(0,0);
			}
		};
	}

}