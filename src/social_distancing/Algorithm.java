package social_distancing;

import social_distancing.data.Instance;
import social_distancing.data.Paths;
import social_distancing.data.Result;
import social_distancing.bfs.Solver;

public class Algorithm {

    public static void main(String[] args) throws Exception {

        // Choose input test case
        String input = "testcases/grid50-9.in";

        // Load instance from input file
        Instance ins = Instance.load(input);

        double t0 = System.nanoTime() / 1e9;

        // Create solver and run alternating BFS
        Solver solver = new Solver(ins.graph, ins.D);
        Result r = solver.solve(ins.sA, ins.sB, ins.tA, ins.tB, ins.T);

        // Reconstruct solution paths
        Paths p = null;
        if (r.k <= ins.T && r.dist != null) {
            p = solver.reconstruct(r, ins);
        }

        double elapsed = (System.nanoTime() / 1e9) - t0;
        
        // Displaying results
        System.out.println(r.k);

        if (p != null) {
            // Path A
            for (int v : p.pathA) System.out.print((v + 1) + " ");
            System.out.println();

            // Path B
            for (int v : p.pathB) System.out.print((v + 1) + " ");
            System.out.println();
        }

        System.out.println(elapsed);
    }
}
