import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Filename: Graph.java Project: p4 Authors:
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
  private int numVertices;
  private int numEdges;
  // private String vertex;
  private LinkedList<String> list[] = new LinkedList[500];
  private String[][] id = new String[500][2];

  /*
   * Default no-argument constructor
   */
  public Graph() {

  }


  /**
   * Add new vertex to the graph.
   *
   * If vertex is null or already exists, method ends without adding a vertex or throwing an
   * exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   */
  public void addVertex(String vertex) {
    if (vertex == null || contains(vertex)) {
      return;
    } else {
      // add vertex
      id[numVertices][0] = vertex;
      id[numVertices][1] = "" + numVertices;

      list[numVertices++] = new LinkedList<String>();
    }
  }

  private boolean contains(String vertex) {
    for (int i = 0; i < id.length; i++) {

      try {
        vertex.equals(id[i][0]);
      } catch (NullPointerException e) {
        continue;
      }

      if (vertex.equals(id[i][0])) {
        return true;
      }
    }
    return false;
  }

  /**
   * Remove a vertex and all associated edges from the graph.
   * 
   * If vertex is null or does not exist, method ends without removing a vertex, edges, or throwing
   * an exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   */
  public void removeVertex(String vertex) {
    if (vertex == null || !contains(vertex)) {
      return;
    } else {
      // remove vertex
      int index = findIndex(vertex);
      list[index] = null;
      id[index] = null;
      --numVertices;
    }
  }

  /**
   * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and unweighted) If either
   * vertex does not exist, add vertex, and add edge, no exception is thrown. If the edge exists in
   * the graph, no edge is added and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge is not in the graph
   */
  public void addEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null) {
      return;
    }
    if (!contains(vertex1)) {
      addVertex(vertex1);
    }
    if (!contains(vertex2)) {
      addVertex(vertex2);
    } else if (containEdge(vertex1, vertex2)) {
      return;
    }
    int index = findIndex(vertex1);
    list[index].add(vertex2);
    ++numEdges;
  }

  private boolean containEdge(String vertex1, String vertex2) {
    int index = findIndex(vertex1);
    for (int j = 0; j < list[index].size(); j++) {
      if (vertex2.equals(list[index].get(j))) {
        return true;
      }
    }
    return false;
  }

  private int findIndex(String vertex) {
    for (int i = 0; i < id.length; i++) {
      try {
        if (vertex.equals(id[i][0])) {
          return Integer.parseInt(id[i][1]);
        }
      } catch (NullPointerException e) {
        continue;
      }

    }
    return numEdges;
  }

  /**
   * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed and unweighted) If
   * either vertex does not exist, or if an edge from vertex1 to vertex2 does not exist, no edge is
   * removed and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge from vertex1 to vertex2 is in the graph
   */
  public void removeEdge(String vertex1, String vertex2) {
    // removeVertex(vertex2);
    if (!contains(vertex1) || !contains(vertex2) || vertex1 == null || vertex2 == null) {
      return;
    } else if (!containEdge(vertex1, vertex2)) {
      return;
    } else {
      int index = findIndex(vertex1);
      list[index].remove(vertex2);
      // id[index] = null;
      --numEdges;
    }
  }

  /**
   * Returns a Set that contains all the vertices
   * 
   */
  public Set<String> getAllVertices() {
    Set<String> set = new TreeSet<String>();
    for (int i = 0; i < 500; i++) {
      try {
        set.add(id[i][0]);
      } catch (NullPointerException e) {
        continue;
      }
    }
    return set;
  }

  /**
   * Get all the neighbor (adjacent) vertices of a vertex
   *
   */
  public List<String> getAdjacentVerticesOf(String vertex) {
    int index = findIndex(vertex);
    List<String> verList = new LinkedList<String>();
    for (int i = 0; i < list[index].size(); i++) {
      verList.add(list[index].get(i));
    }
    return verList;
  }

  /**
   * Returns the number of edges in this graph.
   */
  public int size() {
    return numEdges;
  }

  /**
   * Returns the number of vertices in this graph.
   */
  public int order() {
    return numVertices;
  }

  public static void main(String[] args) {
    Graph gh = new Graph();
    gh.addVertex("1");

    for (int i = 0; i < 120; i++) {
      gh.addVertex("" + i);
    }


    for (int j = 0; j < 90; j++) {
      gh.removeVertex("" + j);
      // System.out.println(j); }
    }



    // System.out.println("all vertices: " + gh.getAllVertices());
    // gh.removeVertex("1");
    // not removing edge because "1" is now null
    // System.out.println("adjacent vertex of 1: " + gh.getAdjacentVerticesOf("1"));
    // gh.removeEdge("1", "00");
    // gh.removeEdge("1", "44");
    // System.out.println("adjacent vertex of 1: " + gh.getAdjacentVerticesOf("1"));
    // gh.removeEdge("1", "123");
    // gh.removeEdge("1", "101");

    System.out.println("all vertices: " + gh.getAllVertices());
    System.out.println("number of vertices: " + gh.order());
  }
}
