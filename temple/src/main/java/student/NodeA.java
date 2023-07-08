package student;

import game.NodeStatus;


public record NodeA (NodeStatus nodeStatus, int f, int g, NodeA parent) implements Comparable<NodeA> {
@Override
    public int compareTo(NodeA n) {
        return Integer.compare(this.f,n.f);
    }
}

