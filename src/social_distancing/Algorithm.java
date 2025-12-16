package social_distancing;

import social_distancing.data.Instance;
import social_distancing.data.Paths;
import social_distancing.data.Result;
import social_distancing.bfs.Solver;


/**
 * This class loads an instance, calls the solver, and prints the solution.
 * All algorithmic logic is implemented in the Solver class.
 */
public class Algorithm {

    public static void main(String[] args) throws Exception {

        // Load instance
        String input = "testcases/grid35-7.in";
        Instance ins = Instance.load(input);

        double t0 = System.nanoTime() / 1e9;

        // Run alternating BFS
        Solver solver = new Solver(ins.graph, ins.D);
        Result r = solver.solve(ins.sA, ins.sB, ins.tA, ins.tB, ins.T);

        // Reconstruct solution
        Paths p = null;
        if (r.k <= ins.T && r.dist != null) {
            p = solver.reconstruct(r, ins);
        }

        double elapsed = (System.nanoTime() / 1e9) - t0;
        
        // Display results
        System.out.println(r.k);

        if (p != null) {
            for (int v : p.pathA) System.out.print((v + 1) + " ");
            System.out.println();

            for (int v : p.pathB) System.out.print((v + 1) + " ");
            System.out.println();
        }

        System.out.println(elapsed);
    }
}
