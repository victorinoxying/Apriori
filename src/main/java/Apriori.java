import javafx.util.Pair;

import javax.swing.*;
import java.util.*;

public class Apriori {
    private int MinSupportNum = 0;
    private boolean IsMaxItemSet = false;
    private int association_rules_Num = 0;
    private ArrayList<ArrayList<Integer>> database = new ArrayList<ArrayList<Integer>>();
    private HashMap<ArrayList<Integer>,Integer> oldItem_support_Map = new HashMap<ArrayList<Integer>, Integer>();
    private HashMap<ArrayList<Integer>,Integer> AllItem_support_Map = new HashMap<ArrayList<Integer>, Integer>();
    private double MinSupportRate = 0;
    private double MinConfidenceRate = 0;
    public ArrayList<Pair<Double,Double>> resultList = new ArrayList<Pair<Double, Double>>();

    public double getMinConfidenceRate() {
        return MinConfidenceRate;
    }

    public double getMinSupportRate() {
        return MinSupportRate;
    }

    public void setMinConfidenceRate(double minConfidenceRate) {
        MinConfidenceRate = minConfidenceRate;
    }

    public void setMinSupportRate(double minSupportRate) {
        MinSupportRate = minSupportRate;
    }

    public void setMinSupportNum(int minSupportNum) {
        MinSupportNum = minSupportNum;
    }
    public int getMinSupportNum(){
        return MinSupportNum;
    }

    public ArrayList getDatabase() {
        return database;
    }

    public void setDatabase(ArrayList database) {
        this.database = database;
    }


    //得到下一层支持数量项集
    private void getNewItemSet_support_Map(){
        //第一次取出单项集
        if(oldItem_support_Map.isEmpty()){
            for (ArrayList<Integer> lineList : database ){
                for (Integer item : lineList){
                    ArrayList items = new ArrayList<Integer>();
                    items.add(item);
                    if (!oldItem_support_Map.containsKey(items)){
                        oldItem_support_Map.put(items,1);
                    }else {
                        Integer value = oldItem_support_Map.get(items);
                        value++;
                        oldItem_support_Map.put(items,value);
                    }
                }
            }
            //移除支持度小于最小支持度的项
            for (Iterator<Map.Entry<ArrayList<Integer>, Integer>> iterator = oldItem_support_Map.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<ArrayList<Integer>, Integer> item = iterator.next();
                Integer support = item.getValue();
                if (support < MinSupportNum){
                    iterator.remove();
                }
            }
        }
        //取出多项集，并剪枝
        else {
            Set<ArrayList<Integer>> oldItemSet = oldItem_support_Map.keySet();  //上一项集支持度图中的项集
            HashMap<ArrayList<Integer>,Integer> newItem_support_Map = new HashMap<ArrayList<Integer>, Integer>(); //新项集
            for(ArrayList<Integer> itemList : oldItemSet){
                HashSet<ArrayList<Integer>> otherLists_Set  = new  HashSet<ArrayList<Integer>>();
                otherLists_Set.addAll(oldItemSet);
                otherLists_Set.remove(itemList);
                for (ArrayList<Integer> anotherList : otherLists_Set){
                    //如果差集为一个商品
                    if(IsMergable(itemList, anotherList)){
                        //合并两个相集得到新商品列表
                        ArrayList<Integer> mergedList = MergeList(itemList,anotherList);
                        //如果重复加过就跳过
                        if (IsList_Existed(newItem_support_Map.keySet(),mergedList)){
                            continue;
                        }
                        //得到合并商品列表的子集,用于判断新商品列表是否可以加入新项集
                        HashSet<ArrayList<Integer>> mergedLsit_childs_set = getChildLists(mergedList);
                        boolean IsList_useful = true;
                        for(ArrayList<Integer> childList : mergedLsit_childs_set){
                            if (!oldItemSet.contains(childList)){
                                IsList_useful =false;
                                break;
                            }
                        }
                        if (IsList_useful){
                            newItem_support_Map.put(mergedList,0);
                        }
                    }
                }
            }
            //判断新支持度项集图是否为空
            if(newItem_support_Map.isEmpty()){
                IsMaxItemSet = true;
            }
            else {
                //根据数据库得到新项集的支持度
                for (Iterator<Map.Entry<ArrayList<Integer>, Integer>> iterator = newItem_support_Map.entrySet().iterator(); iterator.hasNext();){
                    Map.Entry<ArrayList<Integer>, Integer> item = iterator.next();
                    ArrayList<Integer> itemList = item.getKey();
                    for (ArrayList<Integer> lineList : database ){
                        if(lineList.containsAll(itemList)){
                            Integer support = item.getValue();
                            support++;
                            item.setValue(support);
                        }
                    }
                    //移除支持度小于最小支持度的项
                    if(item.getValue()<MinSupportNum){
                        iterator.remove();
                    }
                }
                //更新旧的支持度项集图
                oldItem_support_Map.clear();
                oldItem_support_Map.putAll(newItem_support_Map);
            }
        }
    }


    /* 判断两个List是否差集为1*/
    private boolean IsMergable(ArrayList<Integer> a,ArrayList<Integer> b){
        ArrayList<Integer> acopy = new ArrayList<Integer>();
        acopy.addAll(a);
        acopy.removeAll(b);
        if (acopy.size()==1){
            return true;
        }
        return false;
    }

    /*合并两个list，取并集*/
    private ArrayList<Integer> MergeList(ArrayList<Integer>a, ArrayList<Integer> b){
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        resultList.addAll(a);
        resultList.removeAll(b);
        resultList.addAll(b);
        return resultList;
    }

    /* 取子集,返回hashset*/
    private HashSet<ArrayList<Integer>> getChildLists(ArrayList<Integer> list){
        HashSet<ArrayList<Integer>> resultSet = new HashSet<ArrayList<Integer>>();
        for (Integer item : list){
            ArrayList<Integer> newChildList = new ArrayList<Integer>();
            newChildList.addAll(list);
            newChildList.remove(item);
            resultSet.add(newChildList);
        }
        return resultSet;
    }

    private boolean IsList_Existed(Set<ArrayList<Integer>> set, ArrayList<Integer> arrayList){
        for (ArrayList<Integer> itemList : set){
            ArrayList<Integer> copy = new ArrayList<Integer>();
            copy.addAll(arrayList);
            copy.removeAll(itemList);
            if (copy.isEmpty()){
                return true;
            }
        }
        return false;
    }


    private void getAllItemList_Support_Map(){
        while (!IsMaxItemSet){
            getNewItemSet_support_Map();
            AllItem_support_Map.putAll(oldItem_support_Map);
        }
    }

    private int getSupportNumFromAllItem_support_Map(ArrayList<Integer> list){
        for (Iterator<Map.Entry<ArrayList<Integer>, Integer>> iterator = AllItem_support_Map.entrySet().iterator(); iterator.hasNext();){
            Map.Entry<ArrayList<Integer>, Integer> item = iterator.next();
            Integer supportNum = item.getValue();
            ArrayList<Integer> itemList = item.getKey();
            if (list.containsAll(itemList) && list.size() == itemList.size()){
                return supportNum;
            }
        }
        return 0;
    }

    public int getAssociation_rules_Num(){
        getAllItemList_Support_Map();
        for (Iterator<Map.Entry<ArrayList<Integer>, Integer>> iterator = AllItem_support_Map.entrySet().iterator(); iterator.hasNext();){
            Map.Entry<ArrayList<Integer>, Integer> item = iterator.next();
            Integer supportNum = item.getValue();
            ArrayList<Integer> itemList = item.getKey();
            if (itemList.size()==1){
                continue;
            }
            HashSet<ArrayList<Integer>> childSet = getChildLists(itemList);
            for(ArrayList<Integer> childList : childSet){
                double supportRate = supportNum/(double)database.size();
                int childSupportNum = getSupportNumFromAllItem_support_Map(childList);
                if (childSupportNum==0)
                    continue;
                double confidenceRate = supportNum/ (double)childSupportNum;
                if (supportRate>=MinSupportRate && confidenceRate>=MinConfidenceRate){
                    association_rules_Num++;
                    Pair<Double,Double> newpair = new Pair<Double, Double>(supportRate,confidenceRate);
                    resultList.add(newpair);
                }
            }
        }
        return association_rules_Num;
    }


}
