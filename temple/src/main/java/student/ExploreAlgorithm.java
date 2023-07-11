package student;

import game.NodeStatus;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;


public abstract class ExploreAlgorithm extends Algorithm {

    public static NodeStatus entrePoint;


    public abstract  NodeA getCurrentNode();

    public static List<NodeA> getNeighborsA(Collection<NodeStatus> neighbours, NodeA current){

        return neighbours.stream().toList().stream()
                .map(n->{
                            int g = current.g() + 1;
                            int f = g + n.distanceToTarget();
                            return new NodeA(n, f, g, current);})
                .collect(Collectors.toList());
    }

    public static boolean is_node_in_list(NodeA m, PriorityQueue<NodeA> list){
        return list.stream().anyMatch(n -> m.nodeStatus().nodeID() == n.nodeStatus().nodeID());
    }
}
