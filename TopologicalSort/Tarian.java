import java.io.*;
import java.util.*;

public class Tarian {

    static class Graph {
        private final int V;
        private final List<List<Integer>> adj;

        public Graph(int V) {
            this.V = V;
            adj = new ArrayList<>(V);
            for (int i = 0; i < V; i++) {
                adj.add(new LinkedList<>());
            }
        }

        public void addEdge(int v, int w) {
            // Преобразуем 1-based индексы в 0-based
            int src = v - 1;
            int dest = w - 1;

            if (src >= 0 && src < V && dest >= 0 && dest < V) {
                adj.get(src).add(dest);
            } else {
                throw new IllegalArgumentException(
                        String.format("Invalid edge: %d->%d (valid range 1-%d)", v, w, V));
            }
        }

        public List<Integer> topologicalSort(Measurement measurement) {
            Stack<Integer> stack = new Stack<>();
            boolean[] visited = new boolean[V];
            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    topologicalSortUtil(i, visited, stack, measurement);
                }
                measurement.incrementIterations();
            }

            while (!stack.isEmpty()) {
                result.add(stack.pop() + 1); // Преобразуем обратно в 1-based
                measurement.incrementIterations();
            }

            return result;
        }

        private void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack, Measurement measurement) {
            visited[v] = true;
            measurement.incrementIterations();

            for (Integer neighbor : adj.get(v)) {
                if (!visited[neighbor]) {
                    topologicalSortUtil(neighbor, visited, stack, measurement);
                }
                measurement.incrementIterations();
            }

            stack.push(v);
            measurement.incrementIterations();
        }
    }

    static class Measurement {
        private long startTime;
        private long endTime;
        private long iterations;

        public void start() {
            startTime = System.nanoTime();
            iterations = 0;
        }

        public void stop() {
            endTime = System.nanoTime();
        }

        public void incrementIterations() {
            iterations++;
        }

        public long getTimeNano() {
            return endTime - startTime;
        }

        public long getIterations() {
            return iterations;
        }
    }

    private static Graph readGraphFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] firstLine = reader.readLine().split(" ");
            int V = Integer.parseInt(firstLine[0]);
            int E = Integer.parseInt(firstLine[1]);

            Graph graph = new Graph(V);

            for (int i = 0; i < E; i++) {
                String[] edge = reader.readLine().split(" ");
                int v = Integer.parseInt(edge[0]);
                int w = Integer.parseInt(edge[1]);
                graph.addEdge(v, w);
            }

            return graph;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <input-file>");
            return;
        }

        try {
            Graph graph = readGraphFromFile(args[0]);
            Measurement measurement = new Measurement();

            measurement.start();
            List<Integer> sortedOrder = graph.topologicalSort(measurement);
            measurement.stop();

            System.out.println("Topological order (Tarjan's algorithm):");
            System.out.println(sortedOrder);
            System.out.println("Execution time (ns): " + measurement.getTimeNano());
            System.out.println("Iterations count: " + measurement.getIterations());

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Graph error: " + e.getMessage());
        }
    }
}