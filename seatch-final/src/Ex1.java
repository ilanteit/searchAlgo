// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.*;
import java.util.*;





public class Ex1 {
    private long time_in_start;
    private long time_in_finish;
    public static String[][] createBoard(String input,int board_size) {
        String[] rows = input.split(",");
        int numRows = rows.length;
        int numCols = rows[0].length();
        String[][] matrix = new String[board_size+1][board_size+1];
        for (int i = 0; i < board_size+1; i++) {
            matrix[0][i]="X";
            matrix[i][0]="X";
        }

        for (int i = 1; i < board_size+1; i++) {
            for (int j = 1; j < board_size+1; j++) {

                matrix[i][j] = String.valueOf(rows[i-1].charAt(j-1));
            }


        }

        return matrix;
    }
    public static void saveData(String data) {
        String filePath = "src/output.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
           // System.out.println("Data saved successfully to " + filePath);
        } catch (IOException e) {
           // System.out.println("An error occurred while saving the data: " + e.getMessage());
        }
    }



    public static void main(String[] args) {


        String filePath="src/input.txt";
        long startTime,endTime,elapsedTime=0;
        String line="";
        String allTheBoard="";
        Boolean is_time=false;
        Boolean is_clockwise=false;
        Boolean new_or_old=false;
        Boolean is_open=false;
        String node_movment="";
        String time="";
        String open="";
        String[] start_finish;
        int board_size=0;
        try(BufferedReader reader=new BufferedReader(new FileReader(filePath))) {

            String algorithem=reader.readLine();
            if(algorithem.equals("A*") || algorithem.equals("DFBnB")) {
                node_movment = reader.readLine();
                String []temp=node_movment.split(" ");
                if(temp[0].equals("clockwise ")){
                    is_clockwise=true;
                }
                if(temp[1].equals("new-first")){
                    new_or_old=true;
                }

                time=reader.readLine();
                if(time.equals("with time")){
                    is_time=true;
                }
                open=reader.readLine();
                if(open.equals("with open")){
                    is_open=true   ;
                }

                board_size= Integer.parseInt(reader.readLine());
                start_finish = reader.readLine().split("\\),\\(");
                String[] pair1 = start_finish[0].replace("(", "").split(",");
                int startRow = Integer.parseInt(pair1[0]);
                int startCol = Integer.parseInt(pair1[1]);
                String[] pair2 = start_finish[1].replace(")", "").split(",");
                int endRow = Integer.parseInt(pair2[0]);
                int endCol = Integer.parseInt(pair2[1]);
                while((line= reader.readLine())!=null){
                    allTheBoard+=line+",";

                }

                String [][]current_board=createBoard(allTheBoard,board_size);
                Board board= new Board(current_board,board_size+1);
                int[] start = {startRow, startCol}; // start position
                int[] finish = {endRow, endCol};
                Answer ans1=new Answer(board,start[0],start[1],finish[0],finish[1]);
                if(algorithem.equals("A*")){
                    startTime=System.currentTimeMillis();
                    ans1.AStar(board,is_clockwise,new_or_old,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }

                if(algorithem.equals("DFBnB")){
                    startTime=System.currentTimeMillis();
                    ans1.DFBnB(board, start, finish,is_clockwise,new_or_old,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;


                }
            }
            else {
                node_movment = reader.readLine();


                if (node_movment.equals("clockwise")) {
                    is_clockwise = true;
                }
                time = reader.readLine();
                if (time.equals("with time")) {
                    is_time = true;
                }
                open=reader.readLine();
                if(open.equals("with open")){
                    is_open=true   ;
                }
                board_size = Integer.parseInt(reader.readLine());
                start_finish = reader.readLine().split("\\),\\(");
                String[] pair1 = start_finish[0].replace("(", "").split(",");
                int startRow = Integer.parseInt(pair1[0]);
                int startCol = Integer.parseInt(pair1[1]);
                String[] pair2 = start_finish[1].replace(")", "").split(",");
                int endRow = Integer.parseInt(pair2[0]);
                int endCol = Integer.parseInt(pair2[1]);
                while((line= reader.readLine())!=null){
                    allTheBoard+=line+",";

                }

                String [][]current_board=createBoard(allTheBoard,board_size);


                Board board= new Board(current_board,board_size+1);
                int[] start = {startRow, startCol}; // start position
                int[] finish = {endRow, endCol};
                Answer ans1=new Answer(board,start[0],start[1],finish[0],finish[1]);

                if(algorithem.equals("BFS")){
                    startTime=System.currentTimeMillis();
                    ans1.BFS_ClockWise(board, start, finish,is_clockwise,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }

                if(algorithem.equals("A*")){
                    startTime=System.currentTimeMillis();
                    ans1.AStar(board,is_clockwise,new_or_old,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                if(algorithem.equals("IDA*")){
                    startTime=System.currentTimeMillis();
                    ans1.IDAstar(board,start,finish,is_clockwise,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                if(algorithem.equals("DFID")){
                    startTime=System.currentTimeMillis();
                    ans1.DFID(board,start,is_clockwise,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }


                if(algorithem.equals("DFBnB")){
                    startTime=System.currentTimeMillis();
                    ans1.DFBnB(board, start, finish,is_clockwise,new_or_old,is_open);
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;



                }




            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }








    }
}