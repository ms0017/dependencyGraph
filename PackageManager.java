import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Filename: PackageManager.java Project: p4 Authors:
 * 
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json file.
 * 
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * 
 * For example: package A depends upon package B, then package B must be installed before package A.
 * 
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test classes.
 */

public class PackageManager {


  private LinkedList<String> vert[] = new LinkedList[500];
  private String[][] dependencies = new String[500][2];
  private int count = 0;
  private Graph graph;

  /*
   * Package Manager default no-argument constructor.
   */
  public PackageManager() {

  }

  /**
   * Takes in a file path for a json file and builds the package dependency graph from it.
   * 
   * @param jsonFilepath the name of json data file with package dependency information
   * @throws FileNotFoundException if file path is incorrect
   * @throws IOException           if the give file cannot be read
   * @throws ParseException        if the given json cannot be parsed
   */
  public void constructGraph(String jsonFilepath)
      throws FileNotFoundException, IOException, ParseException {
    graph = new Graph();
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(jsonFilepath));
    JSONObject jsonObject = (JSONObject) obj;
    JSONArray jsonArray = (JSONArray) jsonObject.get("packages");


    int in = jsonArray.size();
    for (int i = 0; i < in; i++) {
      JSONObject j0 = (JSONObject) jsonArray.get(i);
      String name = (String) j0.get("name");
      graph.addVertex(name);
      dependencies[count][0] = name;
      dependencies[count][1] = "" + count;
      JSONArray depend = (JSONArray) j0.get("dependencies");
      System.out.println(name + " depends on  " + depend);
      vert[count++] = new LinkedList<String>();
      for (int j = 0; j < depend.size(); j++) {
        graph.addVertex((String) depend.get(j));
        graph.addEdge((String) depend.get(j), name);
        vert[count - 1].add((String) depend.get(j));
      }
    }
    System.out.println("all vertices " + graph.getAllVertices());
  }

  private List<String> depen(String str) {
    List<String> depList = new LinkedList<String>();
    // int index = findIndex(str);
    for (int i = 0; i < 500; i++) {
      try {
        if (dependencies[i][0].equals(str)) {
          for (int j = 0; j < vert[i].size(); j++) {
            depList.add(vert[i].get(j));
          }
          return depList;
        }
      } catch (NullPointerException e) {
        continue;
      }


    }
    return null;
  }


  private int findIndex(String vertex) {
    for (int i = 0; i < dependencies.length; i++) {
      try {
        if (vertex.equals(dependencies[i][0])) {
          return Integer.parseInt(dependencies[i][1]);
        }
      } catch (NullPointerException e) {
        continue;
      }

    }
    return count;
  }



  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return graph.getAllVertices();
  }

  /**
   * Given a package name, returns a list of packages in a valid installation order.
   * 
   * Valid installation order means that each package is listed before any packages that depend upon
   * that package.
   * 
   * @return List<String>, order in which the packages have to be installed
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  installation order for a particular package. Tip: Cycles in
   *                                  some other part of the graph that do not affect the
   *                                  installation order for the specified package, should not throw
   *                                  this exception.
   * 
   * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
   */
  public List<String> getInstallationOrder(String pkg)
      throws CycleException, PackageNotFoundException {
    List<String> orderList = new LinkedList<String>();
    if (!getAllPackages().contains(pkg)) {
      throw new PackageNotFoundException();
    } else {
      // return valid list

      /*
       * List<String> orderList = new LinkedList<String>(); List<String> get = depen(pkg); if(pkg ==
       * null) { return orderList; }else { getInstallationOrder(pkg); }
       */
      Stack<String> stk = new Stack<String>();
      // List<String> orderList = new LinkedList<String>();
      List<String> get = depen(pkg);
      Collections.reverse(get);
      //     (int i = 0; i < get.size(); i++)
      for (int i = get.size()-1; i>=0; i--){
        stk.push(get.get(i));
        
        List<String> get2 = depen(get.get(i));
        try {

          if (depen(get2.get(i)) != null) {
            List<String> get3 = depen(get2.get(i));
            System.out.println("get3333333333 " + get3);
            stk.push(get3.get(i));
          }
          stk.push(get2.get(i));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          continue;
        }
        stk.push(get.get(i));
      }
      for (int j = 0; j < stk.size(); j++) {
        if (!orderList.contains((String) stk.get(j))) {
          orderList.add((String) stk.get(j));
        }
      }
      orderList.add(pkg);
    }
    return orderList;

  }

  /**
   * Given two packages - one to be installed and the other installed, return a List of the packages
   * that need to be newly installed.
   * 
   * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
   * installed and packageB is already installed, return the list ["A", "C"] since D will have been
   * installed when B was previously installed.
   * 
   * @return List<String>, packages that need to be newly installed.
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  dependencies of the given packages. If there is a cycle in
   *                                  some other part of the graph that doesn't affect the parsing
   *                                  of these dependencies, cycle exception should not be thrown.
   * 
   * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
   *                                  graph.
   */
  public List<String> toInstall(String newPkg, String installedPkg)
      throws CycleException, PackageNotFoundException {

    return null;
  }

  /**
   * Return a valid global installation order of all the packages in the dependency graph.
   * 
   * assumes: no package has been installed and you are required to install all the packages
   * 
   * returns a valid installation order that will not violate any dependencies
   * 
   * @return List<String>, order in which all the packages have to be installed
   * @throws CycleException if you encounter a cycle in the graph
   */
  public List<String> getInstallationOrderForAllPackages() throws CycleException {
    return null;
  }

  /**
   * Find and return the name of the package with the maximum number of dependencies.
   * 
   * Tip: it's not just the number of dependencies given in the json file. The number of
   * dependencies includes the dependencies of its dependencies. But, if a package is listed in
   * multiple places, it is only counted once.
   * 
   * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
   * dependencies - B,C and D.
   * 
   * @return String, name of the package with most dependencies.
   * @throws CycleException if you encounter a cycle in the graph
   */
  public String getPackageWithMaxDependencies() throws CycleException {
    return "";
  }



  public static void main(String[] args) {
    PackageManager pK = new PackageManager();
    try {
      pK.constructGraph("shared_dependencies.json");
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    try {
      System.out.println("adjacent to A are " + pK.graph.getAdjacentVerticesOf("A"));
      System.out.println("adjacent to B are " + pK.graph.getAdjacentVerticesOf("B"));
      System.out.println("adjacent to C are " + pK.graph.getAdjacentVerticesOf("C"));
      System.out.println("adjacent to D are " + pK.graph.getAdjacentVerticesOf("D"));
      System.out.println("order:  " + pK.getInstallationOrder("A"));
      System.out.println("dependency list " + pK.depen("A"));
    } catch (CycleException | PackageNotFoundException e) {
      e.printStackTrace();
    }

    /*
     * JSONParser parser = new JSONParser();
     * 
     * try { Object obj = parser.parse(new FileReader("valid.json")); JSONObject jsonObject =
     * (JSONObject) obj; System.out.println("object ---  "+obj); JSONArray jsonArray = (JSONArray)
     * jsonObject.get("packages"); System.out.println("array ---  "+jsonArray);
     * 
     * int in = jsonArray.size(); for(int i = 0; i < in; i++) { JSONObject j0 = (JSONObject)
     * jsonArray.get(i); String name = (String)j0.get("name"); System.out.println(name);
     * 
     * JSONArray depend = (JSONArray) j0.get("dependencies"); System.out.println(depend);
     * 
     * 
     * 
     * for(int j = 0; j < 1; j++) { JSONObject j1 = (JSONObject) jsonArray.get(j); JSONArray dep =
     * (JSONArray) j1.get("dependencies"); String de = (String)j1.get("dependencies");
     * System.out.println(de); }
     * 
     * }
     * 
     * Iterator itr2 = jsonArray.iterator(); // while(itr2.hasNext()) {
     * 
     * 
     * // } } catch (IOException | ParseException e) { e.printStackTrace(); }
     */

  }

}
