import java.util.*;

public class Graph {
    private HashMap<String, ArrayList<String>> adjVertices;
    private boolean cycleFound = false;
    Graph() {
        adjVertices = new HashMap<String, ArrayList<String>>();
    }
    void addVertex(String name) {
        adjVertices.putIfAbsent(new String(name), new ArrayList<String>());
    }

    void addEdge(String name1, String name2) {
        adjVertices.get(name1).add(String.valueOf(name2));
    }

    Set<String> depthFirstTraversal(String root, String vertToPrint) {
        Set<String> visited = new LinkedHashSet<String>();
        Stack<String> stack = new Stack<String>();
        stack.push(root);
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                ArrayList<String> nextVertices = adjVertices.get(vertex);
                if (nextVertices.contains(vertToPrint)){
                    Set<String> path = new LinkedHashSet<>();
                    path.add(root);
                    if (!vertex.equals(root) && stack.size() == 0)
                        path.add(vertex);
                    path.addAll(stack);
                    path.add(vertToPrint);
                    return path;
                }
                for (String v : nextVertices) {
                    stack.push(v);
                }
            }
            else {
                cycleFound = true;
            }
        }
        visited.remove(root);
        return visited;
    }

    boolean ifCyclic(){
        return cycleFound;
    }
}
