import java.util.Comparator;

public class Node implements Comparable<Node>{
    private int row;
    private int col;
    private int g;
    private int h;
    private int func;
    // private boolean visited;
    private Node parent;
    private int number_Node;

    //private long node_Time;
    private boolean cutoff;

    public Node(int row, int col, int from_start, int from_current, Node parent,int number_Node) {
        this.row = row;
        this.col = col;
        this.g = from_start;
        this.h = from_current;
        this.func = from_start + from_current;
        this.parent = parent;
        this.number_Node=0;
        this.cutoff=false;



    }
    @Override
    public String toString() {
        return "("+row +"," + col +")";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getG() {
        return g;
    }
    public void setG(int score) {
        this.g=score;
    }

    public int getH() {
        return h;
    }

    public int getFunc() {
        return func;
    }

    public Node getParent() {
        return parent;
    }

    public int getNumber_Node(){
        return number_Node;
    }

    @Override
    public int compareTo(Node o) {
        return 0;
    }

    public boolean isCutoff() {
        return cutoff;
    }
    public boolean isVisited() {
        return cutoff;
    }
    public void setVisited(boolean cutoff){
        this.cutoff=cutoff;
    }
    public void setCutoff(boolean cutoff){
        this.cutoff=cutoff;
    }

    //change to work via counter("time")
    static class NodeComparatorNew implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            int fCompare=Integer.compare(n1.getFunc(), n2.getFunc());
            if(fCompare==0){
                return Integer.compare((n1.getNumber_Node()),n2.getNumber_Node());
            }
            return fCompare;
        }
    }
    static class NodeComparatorOld implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            int fCompare=Integer.compare(n1.getFunc(), n2.getFunc());
            if(fCompare==0){
                return Integer.compare((n2.getNumber_Node()),n1.getNumber_Node());
            }
            return fCompare;
        }
    }


}