import java.util.*;

public class Graph {
    private List<Vertex> vertices = new ArrayList<Vertex>();

    public static Double infinity = Double.POSITIVE_INFINITY;

    public Graph(Vertex... vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex findVertexByName(String name) {
        Vertex vertexToFind = new Vertex("");
        for (Vertex vertex : vertices) {
            if (vertex.getName() == name){
                vertexToFind = vertex;
            }
        }
        return vertexToFind;
    }

    public List<Vertex> neighborhood(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : vertex.getEdges()) {
            neighbors.add(edge.getTarget());
        }
        return neighbors;
    }

    public int weight(Vertex vertexFrom, Vertex vertexTo) {
        for (Edge edge : vertexFrom.getEdges()) {
            if (edge.getTarget() == vertexTo) {
                return edge.getDistance();
            }
        }
        return -1;
    }

    public Vertex findVertexMinimum(List<Vertex> whiteVertices, Map<Vertex, Double> distances) {
        Double minimum = infinity;
        Vertex vertexMinimum = new Vertex("");
        for (Vertex whiteVertex : whiteVertices) {
            if(distances.get(whiteVertex) <= minimum) {
                minimum = distances.get(whiteVertex);
                vertexMinimum = whiteVertex;
            }
        }
        return  vertexMinimum;
    }

    public int getDistance(String from, String to) {
        if (vertices.size() == 0){
            System.out.println("Le graphe est vide!");
            return -1;
        }
        Vertex vertexFrom = findVertexByName(from);
        Vertex vertexTo = findVertexByName(to);
        if (vertexFrom.getName() == "") {
            System.out.println("Le départ n'existe pas!");
            return -1;
        }
        if (vertexTo.getName() == "") {
            System.out.println("La destination n'existe pas!");
            return -1;
        }
        if (from == to) {
            System.out.println("La destination est déjà atteinte!");
            return 0;
        }
        return dijkstra(vertexFrom,vertexTo);
    }

    public int dijkstra(Vertex vertexFrom, Vertex vertexTo) {
        List<Vertex> whiteVertices = vertices;
        Map<Vertex, Double> distances = new HashMap<Vertex, Double>();
        for (Vertex vertex : vertices) {
            distances.put(vertex, infinity);
        }
        distances.put(vertexFrom, (double) 0);
        while (whiteVertices.size() != 0) {
            Vertex vertexMinimum = findVertexMinimum(whiteVertices, distances);
            whiteVertices.remove(vertexMinimum);
            List<Vertex> neighbors = neighborhood(vertexMinimum);
            for (Vertex neighbor : neighbors) {
                if (whiteVertices.contains(neighbor)) {
                    distances.put(neighbor, Math.min(distances.get(neighbor), distances.get(vertexMinimum) + weight(vertexMinimum, neighbor)));
                }
            }
        }
        Double distance = distances.get(vertexTo);
        if (distance == infinity) {
            System.out.println("Pas de chemin possible!");
            return -1;
        }
        return (int) (double) distance;
    }
}