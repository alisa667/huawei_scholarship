import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Parse {
    private Graph libsGraph;
    private ArrayList<String> knownLibraries;
    private Set<String> visited;
    String[] dependencies;
    String libraryToCheck;
    Parse(String file1, String file2) throws IOException {
        libsGraph = new Graph();
        knownLibraries = new ArrayList<>();
        readFileAllDependencies(file1);
        readFileOneLibrary(file2);
        test();
    }

    private void test(){
        System.out.println("Missing dependencies:");
        Set <String> missing = getMissingDependencies();
        for (String str: missing){
            System.out.println(str);
        }
        System.out.println("Excess dependencies:");
        Set <String> excess = getExcessDependencies();
        for (String str: excess){
            System.out.println(str);
        }
        System.out.println("Dependency tree:\n" + getDependencyTree("joda-time"));
        System.out.println("Cyclic: " + isCyclic());
    }
    public void readFileAllDependencies(String fileAllDependenciesName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileAllDependenciesName));
        String strCurrentLine;
        while ((strCurrentLine = reader.readLine()) != null) {
            putStringToGraph(strCurrentLine);
        }
    }
    private void putStringToGraph(String str){
        String libraryName = getLibrary(str);
        libsGraph.addVertex(libraryName);
        knownLibraries.add(libraryName);
        String[] dependencies = getDependencies(str);
        for (String dependency : dependencies) {
            if (!dependency.equals("")) {
                libsGraph.addEdge(libraryName, dependency);
            }
        }
    }

    String getLibrary(String str){
        int libEndIndex = str.indexOf(' ');
        return str.substring(0, libEndIndex);
    }

    String[] getDependencies(String str){
        int rightBraceIndex = str.indexOf('[');
        int leftBraceIndex = str.indexOf(']');
        String libs = str.substring(rightBraceIndex + 1, leftBraceIndex);
        return libs.split(", ");
    }

    void readFileOneLibrary(String oneLibraryFileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(oneLibraryFileName));
        String strCurrentLine = reader.readLine();
        libraryToCheck = getLibrary(strCurrentLine);
        if (!knownLibraries.contains(libraryToCheck)){
            System.out.println("Unknown library");
            return;
        }
        dependencies = getDependencies(strCurrentLine);
        visited = libsGraph.depthFirstTraversal(libraryToCheck, null);
    }

    Set<String> getMissingDependencies(){
        Set<String> missing = new LinkedHashSet<>();
        for (String lib : visited){
            if (!Arrays.asList(dependencies).contains(lib)){
                Set<String> pathToMissing = libsGraph.depthFirstTraversal(libraryToCheck, lib);
                StringBuilder pathWithArrows = new StringBuilder();
                for (String str : pathToMissing){
                    pathWithArrows.append(str);
                    if (!str.equals(lib))
                        pathWithArrows.append(" -> ");
                }
                missing.add(String.valueOf(pathWithArrows));
            }
        }
        return missing;
    }

    Set<String> getExcessDependencies(){
        Set<String> excess = new LinkedHashSet<>();
        for (String lib : dependencies){
            if (!visited.contains(lib)){
                excess.add(lib);
            }
        }
        return excess;
    }

    String getDependencyTree(String lib){
        Set<String> path = libsGraph.depthFirstTraversal(libraryToCheck, lib);
        StringBuilder pathWithArrows = new StringBuilder();
        for (String str : path){
            pathWithArrows.append(str);
            if (!str.equals(lib))
                pathWithArrows.append(" -> ");
        }
        return String.valueOf(pathWithArrows);
    }

    boolean isCyclic(){
        return libsGraph.ifCyclic();
    }
}
