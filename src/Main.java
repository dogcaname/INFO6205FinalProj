import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static void readfile(String file){
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            boolean isfirst=true;
            RankSystem rankSystem=new RankSystem();
            System.out.println(file);
            while ((line = in.readLine()) != null) {
                String[] unitlist=line.split(",");
                if(isfirst){
                    isfirst=false;
                    continue;
                }
                if(unitlist.length>=7)
                    rankSystem.addcompete(unitlist[2],unitlist[3],unitlist[6],Integer.parseInt(unitlist[4]),Integer.parseInt(unitlist[5]));
            }
            rankSystem.computepagerank();
            rankSystem.getRank();
            String home="Liverpool";
            String away="Tottenham";
            double pxy=rankSystem.compareHomeeAway(home,away);
            if(pxy==-1){
                System.out.println("name of team error!");

            }else{
                System.out.println(home+" vs "+away+" "+pxy);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private static  void readcsv(String path){
        File file=new File(path);
        File[] csvlist=file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile()&&pathname.getName().endsWith(".csv");
            }
        });
        List<File> filelist=new ArrayList<>();
        for(File file1:csvlist){
            filelist.add(file1);
        }
        filelist.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {

                return  o1.getName().compareTo(o2.getName());
            }
        });
        for (File file1 : filelist) {
//            System.out.println(file1.getPath());
            readfile(file1.getPath());
        }
    }

    public static void main(String[] args) {
        // write your code here
        String databasepath="./datasets";
        readcsv(databasepath);
    }
}
