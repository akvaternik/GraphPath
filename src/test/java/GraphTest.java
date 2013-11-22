import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GraphTest {
    private Vertex lille = new Vertex("Lille");
    private Vertex paris = new Vertex("Paris");
    private Vertex reims = new Vertex("Reims");
    private Vertex nancy = new Vertex("Nancy");
    private Vertex lyon = new Vertex("Lyon");
    private Vertex marseille = new Vertex("Marseille");
    private Vertex lemans = new Vertex("Le Mans");
    private Vertex nantes = new Vertex("Nantes");
    private Vertex bordeaux = new Vertex("Bordeaux");
    private Vertex toulouse = new Vertex("Toulouse");
    private Vertex clermont = new Vertex("Clermont Ferrant");
    private Vertex montpellier = new Vertex("Montpellier");

    public static Double infinity = Double.POSITIVE_INFINITY;

    @Before
    public void setup() {
        lille.connectTo(reims, 206);
        lille.connectTo(paris, 222);
        lille.connectTo(nancy, 418);

        reims.connectTo(paris, 144);
        reims.connectTo(nancy, 245);
        reims.connectTo(lyon, 489);

        paris.connectTo(lyon, 465);
        paris.connectTo(lemans, 208);
        paris.connectTo(clermont, 423);

        lyon.connectTo(clermont, 166);
        lyon.connectTo(marseille, 313);
        lyon.connectTo(montpellier, 304);

        lemans.connectTo(nantes, 189);
        lemans.connectTo(bordeaux, 443);

        nantes.connectTo(bordeaux, 347);

        bordeaux.connectTo(toulouse, 243);

        toulouse.connectTo(montpellier, 245);

        montpellier.connectTo(marseille, 169);
        montpellier.connectTo(toulouse, 245);

        marseille.connectTo(montpellier, 169);

        clermont.connectTo(lyon, 166);
        clermont.connectTo(montpellier, 333);
        clermont.connectTo(marseille, 474);
    }

    @Test
    public void getDistanceForTwoAdjacentVertices() {
        System.out.println("");
        System.out.println("%%%%% Test sommets adjacents %%%%%");
        Graph graph = new Graph(paris, lyon);
        assertEquals(465, graph.getDistance("Paris", "Lyon"), 0);
    }

    @Test
    public void useNameToFindVertex() {
        System.out.println("");
        System.out.println("%%%%% Test trouver un sommet par son nom %%%%%");
        Graph graph = new Graph(paris, lyon);
        Vertex vertex = graph.findVertexByName("Lyon");
        assertEquals(vertex.getName(), "Lyon");
    }

    @Test
    public void getDistanceWithOneIntermediaryVertex() {
        System.out.println("");
        System.out.println("%%%%% Test un sommet intermédiaire %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse);
        assertEquals(169+245, graph.getDistance("Marseille", "Toulouse"));
    }

    @Test
    public void getDistanceWhenDepartureEqualsDestination() {
        System.out.println("");
        System.out.println("%%%%% Test départ égal destination %%%%%");
        Graph graph = new Graph(paris, lyon);
        assertEquals(0, graph.getDistance("Paris", "Paris"));
    }

    @Test
    public void getDistanceWhenEmptyGraph() {
        System.out.println("");
        System.out.println("%%%%% Test graphe vide %%%%%");
        Graph graph = new Graph();
        assertEquals(-1, graph.getDistance("Paris", "Lyon"));
    }

    @Test
    public void getDistanceNonExistentDestination() {
        System.out.println("");
        System.out.println("%%%%% Test destination inexistante %%%%%");
        Graph graph = new Graph(paris, lyon);
        assertEquals(-1, graph.getDistance("Paris", "Marseille"));
        assertEquals(-1, graph.getDistance("Paris", "Londres"));
    }

    @Test
    public void getDistanceNonExistentDeparture() {
        System.out.println("");
        System.out.println("%%%%% Test départ inexistant %%%%%");
        Graph graph = new Graph(paris, lyon);
        assertEquals(-1, graph.getDistance("Marseille", "Paris"));
        assertEquals(-1, graph.getDistance("Londres", "Paris"));
    }

    @Test
    public void getDistanceWithTwoIntermediaryVertex() {
        System.out.println("");
        System.out.println("%%%%% Test deux sommets intermédiaires %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse, bordeaux);
        assertEquals(243+245+169, graph.getDistance("Bordeaux", "Marseille"));
    }

    @Test
    public void getDistanceWhenNoPath() {
        System.out.println("");
        System.out.println("%%%%% Test pas de chemin %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse, bordeaux);
        assertEquals(-1, graph.getDistance("Marseille", "Bordeaux"));
    }

    @Test
    public void getNeighborhood() {
        System.out.println("");
        System.out.println("%%%%% Test de voisinage %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse, bordeaux);
        assertEquals(2, graph.neighborhood(montpellier).size());
        assertEquals(1, graph.neighborhood(toulouse).size());
    }

    @Test
    public void getWeight() {
        System.out.println("");
        System.out.println("%%%%% Test de poids %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse, bordeaux);
        assertEquals(243, graph.weight(bordeaux, toulouse));
        assertEquals(-1, graph.weight(toulouse, bordeaux));
    }

    @Test
    public void getVertexMinimum() {
        System.out.println("");
        System.out.println("%%%%% Test de sommet minimal %%%%%");
        Graph graph = new Graph(marseille, montpellier, toulouse, bordeaux);
        List<Vertex> whiteVertices = graph.getVertices();
        whiteVertices.remove(marseille);
        Map<Vertex, Double> distances = new HashMap<Vertex, Double>();
        distances.put(marseille, 0.0);
        distances.put(montpellier, 169.0);
        distances.put(toulouse, infinity);
        distances.put(bordeaux, infinity);
        assertEquals(montpellier, graph.findVertexMinimum(whiteVertices, distances));
    }

    @Test
    public void getDistanceWhenMultiplePaths() {
        System.out.println("");
        System.out.println("%%%%% Test plusieurs chemins %%%%%");
        Graph graph = new Graph(lemans, nantes, bordeaux);
        assertEquals(443, graph.getDistance("Le Mans", "Bordeaux"));
    }

    @Test
    public void getDistanceAvoidingCycles() {
        System.out.println("");
        System.out.println("%%%%% Test en évitant les cycles %%%%%");
        Graph graph = new Graph(bordeaux, toulouse, montpellier, marseille);
        assertEquals(243+245+169, graph.getDistance("Bordeaux", "Marseille"));
    }
}