package student;

import game.ExplorationState;
import game.NodeStatus;

import java.util.Collection;

public class NodeAS /*implements Comparable<NodeA>*/ {

    private long id;

    // Parent in the path
    private NodeA parent = null;

    private Collection<NodeStatus> neighbors;

    // Evaluation functions
    private int f = 0;
    private int g = 0;
    // Hardcoded heuristic
    private int h;

    NodeAS(NodeStatus nodeStatus){
        this.h = nodeStatus.distanceToTarget();
        this.id = nodeStatus.nodeID();
        this.neighbors = null;
    }

    //@Override
    //public int compareTo(NodeA n) {
        //return Double.compare(this.f, n.f);
    //}

    public int getF() {
        return f;
    }

    public void setF(int g) {
        this.f = g +this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getH() {
        return h;
    }
    public void setG(int g) {
        this.g = g;
    }

    public NodeA getParent() {
        return parent;
    }

    public void setParent(NodeA parent) {
        this.parent = parent;
    }

    public Collection<NodeStatus> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ExplorationState state) {
        this.neighbors = state.getNeighbours();
    }

    public int getG() {
        return g;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[id=" + id + " f=" + f + "]";
    }

}
