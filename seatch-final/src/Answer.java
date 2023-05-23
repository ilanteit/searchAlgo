import java.util.*;

public class Answer {
    private Board board;
    private static int startRow;
    private static int startCol;
    private static int destRow;
    private static int destCol;

    private Stack<Node> path;
    private int nodeCounter;

    private static int[][] clockWise_dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
    private int[][] counterClockWise_dirs = {{0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};

    private String[] clockWise_dirNames = {"R", "RD", "D", "LD", "L", "LU", "U", "RU"};
    private String[] counterClockWise_dirNames = {"R", "RU", "U", "LU", "L", "LD", "D", "RD"};

    public Answer(Board board, int startRow, int startCol, int destRow, int destCol) {
        this.board = board;
        this.startRow = startRow;
        this.startCol = startCol;
        this.destRow = destRow;
        this.destCol = destCol;
        this.path=new Stack<>();
        this.nodeCounter=0;


    }


    public void IDAstar(Board b,int []start,int[]finish ,Boolean is_clockwise,Boolean is_open){

        int N= b.getN();
        Stack<Node> stack=new Stack<>();
        Hashtable<String,Node> H=new Hashtable<>();
        int threshold=heuristic(start[0],start[1],finish[0],finish[1]);
        Node startNode=new Node(startCol,startRow,0,threshold,null,nodeCounter);
        nodeCounter++;
        while(threshold!=Integer.MAX_VALUE){
            int minF=Integer.MAX_VALUE;
            stack.push(startNode);
            H.put(startNode.toString(),startNode);
            while(!stack.isEmpty()) {
                Node current = stack.pop();
                if (current.isVisited()) {
                    current.setVisited(false);
                    H.remove(current.toString());
                } else {
                    stack.push(current);
                    current.setVisited(true);

                    List<Node>neighboors;
                    if(is_clockwise) {

                         neighboors = createNeighboors(current, b.getN(), b, null);
                    }
                    else{
                        neighboors=createNeighboorsCounterClockWise(current,b.getN(),b,null);
                    }

                    for(Node neighboor: neighboors) {
                        if (neighboor.getFunc() > threshold) {
                            minF = Math.min(minF, neighboor.getFunc());
                            continue;
                        }
                        if(H.contains(neighboor.toString())) {
                            Node gTag = H.get(neighboor.toString());
                            if (gTag.isVisited()) continue;
                        }
                        if(H.contains(neighboor.toString())) {
                            Node gTag = H.get(neighboor.toString());
                            if(!gTag.isVisited()){
                                if (gTag.getFunc()>neighboor.getFunc()) {
                                    stack.remove(gTag);
                                    H.remove(gTag.toString());
                                } else continue;
                            }
                        }

                        if (neighboor.getRow() == destRow && neighboor.getCol() == destCol) {
                            printPathClockWise(neighboor);
                            System.out.println();
                            System.out.println("THE FINAL COST IS: " +neighboor.getG());
                            System.out.println("the number of nodes created: "+nodeCounter);
                            return;
                        }



                        stack.push(neighboor);
                        H.put(neighboor.toString(),neighboor);
                    }


                }
                if(is_open){
                    System.out.println(stack);
                }

            }
            threshold=minF;
        }
        System.out.println("No Path was found");
        return ;
    }

    public void DFID(Board b ,int []start,Boolean is_clockwise,Boolean is_open){
        Node startNode=new Node(start[0],start[1],0,0,null,nodeCounter);
        nodeCounter++;
        for (int depth =1;depth<b.getN();depth++){
            HashSet<Node> hashTable=new HashSet<>();
            Node result=limited_dfs(b,startNode,depth,hashTable,is_clockwise,is_open);
            if(result!=null && !result.isCutoff()){
                System.out.println("THE COUNTER IS: " +nodeCounter);
                System.out.println("THE FINAL COST IS: " +result.getG());
                printPathClockWise(result);
                return;
            }

        }
        System.out.println("Path not found");

    }
//where to print if is_open?
    public Node limited_dfs(Board b,Node current, int depth,HashSet<Node>hashTable,Boolean is_clockwise,Boolean is_open){
        int N=b.getN();
        if (depth == 0 && current.getRow() ==destRow  && current.getCol() == destCol) {
            path.push(current);
            return current;

        }else if(depth==0) {
            current.setCutoff(true);
            return current;
        }
        else if (depth > 0) {
            hashTable.add(current);
            boolean isCutoff=false;


            List<Node>neighboors;
            if(is_clockwise) {
                neighboors = createNeighboors(current, b.getN(), b, null);
            }
            else{
                neighboors=createNeighboorsCounterClockWise(current,b.getN(),b,null);
            }
            for(Node neighbor: neighboors){
                if(hashTable.contains(neighbor)) continue;
                Node result=limited_dfs(b,neighbor,depth-1,hashTable,is_clockwise,is_open);
                if(result.isCutoff()){
                    isCutoff=true;
                }
                else if(result!=null){
                    return result;
                }
            }

            hashTable.remove(current);
            if(isCutoff){
                current.setCutoff(true);
                return current;
            }
            else{
                return null;
            }
        }
        return null;
    }


    public void AStar(Board b,Boolean is_clockwise,Boolean is_new,Boolean is_open) {
        int N = b.getN();
        PriorityQueue<Node> openList;
        if(is_new){
             openList = new PriorityQueue<>(new Node.NodeComparatorNew());

        }
        else{
             openList = new PriorityQueue<>(new Node.NodeComparatorOld());
        }

        HashSet<Node> closedSet = new HashSet<>();
        Node startNode = new Node(startRow, startCol, 0,0, null,nodeCounter);
        nodeCounter++;
        openList.add(startNode);
        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.getRow() == destRow && current.getCol() == destCol) {
                //System.out.println("the cost is :"+current.getG());
                //List<Node> path = new ArrayList<>();
                Node node = current;
                printPathClockWise(node);
                System.out.println();
                System.out.println("THE FINAL COST IS: " +node.getG());
                System.out.println("Number of nodes created: "+ nodeCounter);
                //printPathCounterClock(node);
                return;


            }
            closedSet.add(current);
            List<Node>neighboors=createNeighboors(current,b.getN(),b,closedSet);
            //List<Node>neighboors=createNeighboorsCounterClockWise(currNode,b.getN(),b,closedSet);
            for (Node neighboor : neighboors){
               // int newCost = current.getG() + b.getCost(current.getRow(), current.getCol(), newRow,newCol);
                //Node next = new Node(neighboor.getRow(), neighboor.getCol(), newCost, heuristic[newRow][newCol], current,nodeCounter);
                //nodeCounter++;
                if(closedSet.contains(neighboor)) continue;
                openList.add(neighboor);
            }

            if(is_open){
                System.out.println(openList);
            }

        }
        System.out.println("No path found.");

    }
//    public void AStarOldFirst(Board b,int []start,int []finish) {
//        int N = b.getN();
//        //int[][] heuristic = new int[N][N];
//        //fill in the heuristic table for each block
//
//        PriorityQueue<Node> openList = new PriorityQueue<>(new Node.NodeComparatorOld());
//        HashSet<Node> closedSet = new HashSet<>();
//        Node startNode = new Node(start[0], start[1], 0, 0, null,nodeCounter);
//        nodeCounter++;
//        openList.add(startNode);
//        while (!openList.isEmpty()) {
//            Node current = openList.poll();
//            if (current.getRow() == destRow && current.getCol() == destCol) {
//                //System.out.println("FOUND A GOAL");
//                System.out.println("the cost is :"+current.getG());
//                System.out.println("Number of nodes created: "+ nodeCounter);
//                List<Node> path = new ArrayList<>();
//                Node node = current;
//                printPathClockWise(node);
//                System.out.println();
//                return;
//            }
//            closedSet.add(current);
//            closedSet.add(current);
//            List<Node>neighboors=createNeighboorsCounterClockWise(current,b.getN(),b,closedSet);
//            for (Node neighboor : neighboors){
//                if(closedSet.contains(neighboor)) continue;
//                openList.add(neighboor);
//            }
//
//        }
//        System.out.println("No path found.");
//
//    }
    public static int heuristic(int row, int col, int goalRow, int goalCol) {

        return Math.abs(row-goalRow)+Math.abs(col-goalCol);
    }
    public void BFS_ClockWise(Board b,int []start,int []finish,Boolean is_clockwise,Boolean is_open) {
        Queue<Node> queue = new LinkedList<>();
        Map<String, Node> openList = new HashMap<>();
        Set<Node> closedSet = new HashSet<>();
        Node startNode = new Node(start[0], start[1], 0, 0, null,nodeCounter);
        nodeCounter++;
        queue.add(startNode);
        openList.put(startNode.toString(),startNode);

        while (!openList.isEmpty()) {
            Node currNode=queue.poll();
             openList.remove(currNode.toString());



            if (b.isGoal(currNode.getRow(),currNode.getCol())) {
                System.out.println("THE COUNTER IS: " +nodeCounter);
                System.out.println("THE FINAL COST IS: " +currNode.getG());

                printPathClockWise(currNode);
                System.out.println();
                return;
            }
            closedSet.add(currNode);
            List<Node>neighboors;

            if(is_clockwise){
                neighboors=createNeighboorsBFS(currNode,b.getN(),b,closedSet,openList);
            }
           else{
                neighboors=createNeighboorsBFSCounter(currNode,b.getN(),b,closedSet,openList);
            }



                for(Node neighbor: neighboors) {
                    if (!closedSet.contains(neighbor) && !openList.containsKey(neighbor) ) {
                        queue.add(neighbor);
                        openList.put(neighbor.toString(),neighbor);
                    }
                }

            if(is_open) {
                System.out.println(queue);
            }




        }
        System.out.println("No path found.");
    }




    public void DFBnB(Board b ,int[]start,int[]finish,Boolean is_clockwise,Boolean is_new,Boolean is_open){

        Stack<Node> L=new Stack<>();
        Hashtable<String,Node>H=new Hashtable<>();
        Node startNode=new Node(start[0],start[1],0,0,null,nodeCounter);
        nodeCounter++;
        List<Node>result=null;
        int t=Integer.MAX_VALUE;
//        int []result;
        L.push(startNode);
        //H.put(startNode,true);
        while(!L.isEmpty()){
            Node current=L.pop();
            if(current.isVisited()){
                current.setVisited(false);
                H.remove(current.toString());
            }
            else{
                current.setVisited(true);
                //H.put(current,true);
                L.push(current);
        List<Node>neighboors;
        if(is_clockwise){
            neighboors=createNeighboors(current,b.getN(),b,null);
        }
        else{
            neighboors=createNeighboorsCounterClockWise(current,b.getN(),b,null);
        }

                if(is_new){
                Collections.sort(neighboors,Comparator.comparingInt(Node::getFunc).thenComparing(Node::getNumber_Node).reversed());
                }


        else {
                    Collections.sort(neighboors,Comparator.comparingInt(Node::getFunc).thenComparing(Node::getNumber_Node));
        }

                Iterator<Node> iterator = neighboors.iterator();
                while (iterator.hasNext()) {
                    Node g = iterator.next();
                    int i = neighboors.indexOf(g);

                    if (neighboors.get(i).getFunc() >= t) {
                        iterator.remove();
                        while(iterator.hasNext()){
                            iterator.next();
                            iterator.remove();
                        }

                    } else if (H.containsKey(g.toString())) {
                        Node gTag = H.get(g.toString());
                        if (gTag.isVisited()) {
                            continue;
                        }
                        if (gTag.getFunc() <= g.getFunc()) {
                            iterator.remove();
                        } else {
                            L.remove(gTag);
                            H.remove(gTag.toString());
                        }
                    } else if (g.getRow() == finish[0] && g.getCol() == finish[1]) {
                        t = g.getFunc();
                        System.out.println("the cost is: "+ g.getG());
                        System.out.println("the number of nodes is: "+nodeCounter);
                        printPathClockWise(g);
                        result = path(g);
                        iterator.remove();
                        return;
                    }
                }


                Collections.reverse(neighboors);
                L.addAll(neighboors);
                for(Node node:neighboors){
                    H.put(node.toString(),node);
                }

            }if(is_open) {
                System.out.println(L);
            }
        }
        System.out.println("path not found");

        return;
    }
    public static List<Node>path(Node node){
        List<Node>path=new ArrayList<>();
        while(node!=null){
            path.add(node);
            node=node.getParent();
        }
        return path;
    }
    public List<Node> createNeighboorsBFS(Node current,int N,Board b,Set<Node>closedSet,Map<String, Node> openList){

        List<Node>neighboors=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Boolean flag=false;
            int newRow = current.getRow() + clockWise_dirs[i][0];
            int newCol = current.getCol() + clockWise_dirs[i][1];

            if(current.getParent()!=null && newRow== current.getParent().getRow()&& newCol== current.getParent().getCol())continue;
            if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;
            if (b.getCostSimple(newRow, newCol) == -1) continue;
            if(openList.containsKey("("+newRow+","+newCol+")")) {
                //nodeCounter++;
                continue;
            }


            for(Node node :closedSet){
                if(node.getRow()==newRow && node.getCol()==newCol) {
                    flag=true;
                    //nodeCounter++;
                    break;
                }
            }
            if(flag)continue;
                //System.out.println("("+newRow+","+newCol+")");
                int newCost = current.getG() + b.getCost(current.getRow(), current.getCol(), newRow, newCol);
                Node next = new Node(newRow, newCol, newCost, heuristic(newRow, newCol, destRow, destCol), current, nodeCounter);
                nodeCounter++;
                neighboors.add(next);
                //openList.add(next);


        }



        return neighboors;
    }
    public List<Node> createNeighboorsBFSCounter(Node current,int N,Board b,Set<Node>closedSet,Map<String, Node> openList){

        List<Node>neighboors=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Boolean flag=false;
            int newRow = current.getRow() + counterClockWise_dirs[i][0];
            int newCol = current.getCol() + counterClockWise_dirs[i][1];

            if(current.getParent()!=null && newRow== current.getParent().getRow()&& newCol== current.getParent().getCol())continue;
            if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;
            if (b.getCostSimple(newRow, newCol) == -1) continue;
            if(openList.containsKey("("+newRow+","+newCol+")")) continue;

            for(Node node :closedSet){
                if(node.getRow()==newRow && node.getCol()==newCol) {
                    flag=true;
                    nodeCounter++;
                    break;
                }
            }
            if(flag)continue;
            System.out.println("("+newRow+","+newCol+")");
            int newCost = current.getG() + b.getCost(current.getRow(), current.getCol(), newRow, newCol);
            Node next = new Node(newRow, newCol, newCost, heuristic(newRow, newCol, destRow, destCol), current, nodeCounter);
            nodeCounter++;
            neighboors.add(next);
            //openList.add(next);


        }



        return neighboors;
    }

    public List<Node> createNeighboors(Node current,int N,Board b,Set<Node>closedSet){


        List<Node>neighboors=new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Boolean flag=false;
            int newRow = current.getRow() + clockWise_dirs[i][0];
            int newCol = current.getCol() + clockWise_dirs[i][1];
            if(current.getParent()!=null && newRow== current.getParent().getRow()&& newCol== current.getParent().getCol())continue;
            if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;
            if (b.getCostSimple(newRow, newCol) == -1) continue;

            if(closedSet!=null){
                for(Node node : closedSet) {
                    if (node.getRow() == newRow && node.getCol() == newCol) {
                        flag = true;
                        break;
                    }
                }
                }
            if(flag)continue;
            int newCost = current.getG() + b.getCost(current.getRow(), current.getCol(), newRow, newCol);
                    Node next = new Node(newRow, newCol, newCost, heuristic(newRow,newCol,destRow,destCol), current, nodeCounter);
                    nodeCounter++;
                    neighboors.add(next);


        }
        return neighboors;
    }
    public List<Node> createNeighboorsCounterClockWise(Node current,int N,Board b,Set<Node>closedSet){
        Boolean flag=false;
        List<Node>neighboors=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int newRow = current.getRow() + counterClockWise_dirs[i][0];
            int newCol = current.getCol() + counterClockWise_dirs[i][1];
            if(current.getParent()!=null && newRow== current.getParent().getRow()&& newCol== current.getParent().getCol())continue;


            if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;
            if (b.getCostSimple(newRow, newCol) == -1) continue;

            //  closedSet.contains()
            if(closedSet!=null) {
                for (Node node : closedSet) {
                    if (node.getRow() == newRow && node.getCol() == newCol) {
                        flag = true;
                        break;
                    }
                }
            }
            if(flag)continue;

            int newCost = current.getG() + b.getCost(current.getRow(), current.getCol(), newRow, newCol);
            Node next = new Node(newRow, newCol, newCost, heuristic(newRow,newCol,destRow,destCol), current, nodeCounter);
            nodeCounter++;

            neighboors.add(next);


        }
        return neighboors;
    }
    public static String getOppositeDirection(String direction) {
        switch(direction) {
            case "R":
                return "L";
            case "RD":
                return "LU";
            case "D":
                return "U";
            case "LD":
                return "RU";
            case "L":
                return "R";
            case "LU":
                return "RD";
            case "U":
                return "D";
            case "RU":
                return "LD";
            default:
                return "";
        }
    }
    public void printPathClockWise(Node node) {
        List<String> path = new ArrayList<>();
        while (node.getParent() != null) {
            int row = node.getRow();
            int col = node.getCol();
            int parentRow = node.getParent().getRow();
            int parentCol = node.getParent().getCol();

            for (int i = 0; i < clockWise_dirs.length; i++) {
                int dirRow = row + clockWise_dirs[i][0];
                int dirCol = col + clockWise_dirs[i][1];
                if (dirRow == parentRow && dirCol == parentCol) {
                    path.add(0, clockWise_dirNames[i]);
                    break;
                }
            }
            node = node.getParent();
        }


        for (int i = 0; i < path.size() - 1; i++) {
            System.out.print(getOppositeDirection(path.get(i)) + "-");
        }
        System.out.print(getOppositeDirection(path.get(path.size() - 1)));

    }
//    public void printPathCounterClock(Node node) {
//        List<String> path = new ArrayList<>();
//        while (node.getParent() != null) {
//            int row = node.getRow();
//            int col = node.getCol();
//            int parentRow = node.getParent().getRow();
//            int parentCol = node.getParent().getCol();
//
//            for (int i = 0; i < counterClockWise_dirs.length; i++) {
//                int dirRow = row + counterClockWise_dirs[i][0];
//                int dirCol = col + counterClockWise_dirs[i][1];
//                if (dirRow == parentRow && dirCol == parentCol) {
//                    path.add(0, counterClockWise_dirNames[i]);
//                    break;
//                }
//            }
//            node = node.getParent();
//        }
//        for (int i = 0; i < path.size() - 1; i++) {
//            System.out.print(getOppositeDirection(path.get(i)) + "-");
//        }
//        System.out.print(getOppositeDirection(path.get(path.size() - 1)));
//
//
//    }
}
