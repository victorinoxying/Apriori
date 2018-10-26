import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class main {
    public static void main(String argv[]){
        /*Apriori apriori = new Apriori();
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        ArrayList<Integer> c = new ArrayList<Integer>();
        ArrayList<Integer> d = new ArrayList<Integer>();
        a.add(1);a.add(3);a.add(4);
        b.add(2);b.add(3);b.add(5);
        c.add(1); c.add(2); c.add(3); c.add(5);
        d.add(2);d.add(5);
        ArrayList<ArrayList<Integer>> testDatabase = new ArrayList<ArrayList<Integer>>();
        testDatabase.add(a);testDatabase.add(b);testDatabase.add(c);testDatabase.add(d);
        Integer support = 2;
        apriori.setDatabase(testDatabase);
        apriori.setMinSupportNum(support);
        HashMap<ArrayList<Integer>,Integer> result = apriori.getMaxItemList_Support_Map();
        System.out.println(result);*/


        DateCreator dateCreator = new DateCreator();
        Apriori apriori = new Apriori();
//        dateCreator.setDataNum(10000);
//        dateCreator.setItemNum(10);
//        ArrayList<ArrayList<Integer>> arrayList = dateCreator.getData();
//        dateCreator.writeIntotxt("test.txt");
        ArrayList<ArrayList<Integer>> arrayList = dateCreator.getData();
        dateCreator.readDateFromtxt("test.txt");
        apriori.setMinSupportNum(20);
        apriori.setMinSupportRate(0.4);
        apriori.setMinConfidenceRate(0.0);
        apriori.setDatabase(arrayList);
        System.out.println(apriori.getAssociation_rules_Num());
        for (Pair p : apriori.resultList){
            System.out.println(p);
        }



    }
}
