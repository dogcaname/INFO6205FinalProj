import java.text.DecimalFormat;
import java.util.*;

public class RankSystem {
    private Map<String,Integer> competeresult=new HashMap<>();
    private Map<String,Double> prteam=new HashMap<>();
    private int iter=3;
    public RankSystem() {

    }
    /**
     *min and max    x=(x-min)/(max-min)
     *         :return:
     *
     * */
    private void minmax(){
        double minval = -1;
        double max = -1;
        for(Double x:prteam.values()){
            if (minval==-1 || minval>x){
                minval=x;
            }
            if(max==-1|| max<x){
                max=x;
            }
        }
        double distance=max-minval;
        if (distance>0){
            for(String x :prteam.keySet()){
                prteam.put(x,(double)(prteam.get(x)-minval)/distance);
            }
        }else{
            for(String x:prteam.keySet()){
                prteam.put(x,1.0);
            }
        }
    }
    /**
     * :param ftr resut  enum(H,A,D)
     *         :param fthg: home team score
     *         :param ftg:  away team score
     *         :return:  value of home team , value of away team
     *
     * */
    private int[] computevalue(String ftr,int fthg,int ftg){
        int[] score=new int[2];
        if(fthg>=0 || fthg>=0){
            score[0]=fthg;
            score[1]=ftg;
            return score;
        }else{
            if("H".equals(ftr)){
                score[0]=4;
                score[1]=0;

            }else if("A".equals(fthg)){
                score[0]=0;
                score[1]=4;

            }else if("D".equals(fthg)){
                score[0]=1;
                score[1]=1;
            }else{
                score[0]=0;
                score[1]=0;
            }
        }
        return score;

    }

    /**
     * join new compete data
     *         :param home: home team
     *         :param away:  away team
     *         :param ftr:
     *         :param fthg:
     *         :param ftg:
     *         :return:  true: success false:fail to insert data
     *
     * */
    public boolean addcompete(String home,String away,String ftr,int fthg,int ftg){
        try {
            int[] score = computevalue(ftr, fthg, ftg);
            int hval = score[0];
            int aval = score[1];
            String h_key = home + "_" + away;
            if (competeresult.containsKey(h_key)) {
                int val = competeresult.get(h_key);
                competeresult.put(h_key, val + hval);
            } else {
                competeresult.put(h_key, hval);
            }
            String a_key = away + "_" + home;
            if (competeresult.containsKey(a_key)) {
                int val = competeresult.get(a_key);
                competeresult.put(a_key, val + aval);
            } else {
                competeresult.put(a_key, aval);
            }
            if (!prteam.containsKey(home)) {
                prteam.put(home, 1.0);
            }
            if (!prteam.containsKey(away)) {
                prteam.put(away, 1.0);
            }
            return true;
        }catch (Exception e){
            System.out.println("error"+e);
            return false;
        }

    }
    /**
     *
     * output resut
     * */
    public void getRank(){
        System.out.println("size of football is "+prteam.size());
//        Collections.sort();
        List<Map.Entry<String,Double>> list=new ArrayList<Map.Entry<String,Double>>(prteam.entrySet());

        list.sort( new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
//                reverse
                return (o1.getValue().compareTo(o2.getValue()))*-1;
            }
        });
        List<String> result=new ArrayList<>();
        for (Map.Entry<String, Double> x : list) {
            result.add(x.getKey());
        }
        for (String home : result) {
            for (String away : result) {
                String key=home+"_"+away;
                if(competeresult.containsKey(key)){
                    System.out.print("\t"+competeresult.get(key));
                }else{
                    System.out.print("\t-1");
                }
            }

            DecimalFormat df  = new DecimalFormat("###.0000");
            System.out.println("\t"+home+"\t"+df.format(prteam.get(home)));
        }
        System.out.println(result);

    }
    /**
     *
     *
     * */
    public void computepagerank(){
        Map<String,Double> teampr=new HashMap<>();
        //copy value
        for(String x :prteam.keySet()){
            teampr.put(x,prteam.get(x));
        }
        if (iter==0){
            minmax();
            return;
        }
        for(String home :prteam.keySet()){
            int valsum=0;
            int count=0;
            for(String away : prteam.keySet()){
                String key=home+"_"+away;
                if(competeresult.containsKey(key)){
                    double val=competeresult.get(key);
                    if(val>-1){
                        count++;
                        valsum+=prteam.get(away)*val;
                    }
                }
            }
            if(count>0){
                teampr.put(home,valsum*1.0/count);
            }
        }
        iter--;
        prteam=teampr;
        computepagerank();

    }
    public double compareHomeeAway(String home,String away){
        if(prteam.containsKey(home)&&prteam.containsKey(away)){
            double prh=prteam.get(home);
            double pra=prteam.get(away);
            if(Math.abs(prh-pra)<0.01){
                return 0;
            }
            return prh-pra;
        }
        System.out.println("no found the team");
        return -1;

    }

}
