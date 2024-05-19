package spider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import spider.*;

public class Graph {
    public static void createDotGraph(String dotFormat,String fileName)
    {
        GraphViz gv=new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File(fileName+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }

    public static void main(String[] args) throws Exception {
        String dotFormat="1 -> 2;";
        createDotGraph(dotFormat, "DotGraph");
    }
}

