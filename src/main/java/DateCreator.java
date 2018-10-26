import java.io.*;
import java.util.ArrayList;

public class DateCreator {
    private int itemNum = 0;
    private int dataNum = 0;
    private ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

    public int getDataNum() {
        return dataNum;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public ArrayList getData(){
        for (int i=0;i<dataNum;i++){
            ArrayList<Integer> lineList = new ArrayList<Integer>();
            for(int j=0;j<itemNum;j++){
                int random = (int)(Math.random()*itemNum);
                if(random!=0 && !lineList.contains(random)){
                    lineList.add(random);
                }
            }
            result.add(lineList);
        }
        return result;
    }
    public ArrayList readDateFromtxt(String path){
        try{
            File file = new File(path);
            if(!file.isFile()|| !file.exists()){
                System.out.println("read file filed");
            }
            result.clear();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine())!=null){
                String[] list =  line.split(" ");
                ArrayList<Integer> itemList = new ArrayList<Integer>();
                for(String str :list){
                    Integer item = Integer.valueOf(str);
                    itemList.add(item);
                }
                result.add(itemList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void writeIntotxt(String path){
        try{
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            Writer out = new FileWriter(file);
            for(ArrayList<Integer> array : result){
                for (Integer item : array){
                    out.write(String.valueOf(item)+" ");
                }
                out.write("\n");
            }
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
