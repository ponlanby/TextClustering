package cluster;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;  
import java.util.Random;  
  
/** 
 * K均值聚类算法 
 */  
public class kmeans {  
    private int k;// 分成多少簇  
    private int m;// 迭代次数  
    private int dataSetLength;// 数据集元素个数，即数据集的长度  
    private ArrayList<float[]> dataSet;// 数据集链表  
    private ArrayList<float[]> center;// 中心链表  
    private ArrayList<ArrayList<float[]>> cluster; // 簇  
    private ArrayList<Float> jc;// 误差平方和，k越接近dataSetLength，误差越小  
    private Random random;  
    
    private float threshold = (float) 0.05;
  
    /** 
     * 设置需分组的原始数据集 
     *  
     * @param dataSet 
     */  
  
    public void setDataSet(ArrayList<float[]> dataSet) {  
        this.dataSet = dataSet;  
    }  
  
    /** 
     * 获取结果分组 
     *  
     * @return 结果集 
     */  
  
    public ArrayList<ArrayList<float[]>> getCluster() {  
        return cluster;  
    }  
  
    /** 
     * 构造函数，传入需要分成的簇数量 
     *  
     * @param k 
     *            簇数量,若k<=0时，设置为1，若k大于数据源的长度时，置为数据源的长度 
     */  
    public kmeans(int k) {  
        if (k <= 0) {  
            k = 1;  
        }  
        this.k = k;  
    }  
  
    /** 
     * 初始化 
     */  
    private void init() {  
        m = 0;  
        random = new Random();  
        if (dataSet == null || dataSet.size() == 0) {  
            initDataSet();  
        }  
        dataSetLength = dataSet.size();  
        if (k > dataSetLength) {  
            k = dataSetLength;  
        }  
        center = initCenters();  
        cluster = initCluster();  
        jc = new ArrayList<Float>();  
    }  
  
    /** 
     * 如果调用者未初始化数据集，则采用内部测试数据集 
     */  
    private void initDataSet() {  
        dataSet = new ArrayList<float[]>();  
        // 其中{6,3}是一样的，所以长度为15的数据集分成14簇和15簇的误差都为0  
        float[][] dataSetArray = new float[][] { { 8, 2 }, { 3, 4 }, { 2, 5 },  
                { 4, 2 }, { 7, 3 }, { 6, 2 }, { 4, 7 }, { 6, 3 }, { 5, 3 },  
                { 6, 3 }, { 6, 9 }, { 1, 6 }, { 3, 9 }, { 4, 1 }, { 8, 6 } };  
  
        for (int i = 0; i < dataSetArray.length; i++) {  
            dataSet.add(dataSetArray[i]);  
        }  
    }  
  
    /** 
     * 初始化中心数据链表，分成多少簇就有多少个中心点 
     *  
     * @return 中心点集 
     */  
    private ArrayList<float[]> initCenters() {  
        ArrayList<float[]> center = new ArrayList<float[]>();  
        int[] randoms = new int[k];  
        boolean flag;  
        int temp = random.nextInt(dataSetLength);  
        randoms[0] = temp;  
        for (int i = 1; i < k; i++) {  
            flag = true;  
            while (flag) {  
                temp = random.nextInt(dataSetLength);  
                int j = 0;  
                // 不清楚for循环导致j无法加1  
                // for(j=0;j<i;++j)  
                // {  
                // if(temp==randoms[j]);  
                // {  
                // break;  
                // }  
                // }  
                while (j < i) {  
                    if (temp == randoms[j]) {  
                        break;  
                    }  
                    j++;  
                }  
                if (j == i) {  
                    flag = false;  
                }  
            }  
            randoms[i] = temp;  
        }  
  
        // 测试随机数生成情况  
        // for(int i=0;i<k;i++)  
        // {  
        // System.out.println("test1:randoms["+i+"]="+randoms[i]);  
        // }  
  
        // System.out.println();  
        for (int i = 0; i < k; i++) {  
            center.add(dataSet.get(randoms[i]));// 生成初始化中心链表  
        }  
        return center;  
    }  
  
    /** 
     * 初始化簇集合 
     *  
     * @return 一个分为k簇的空数据的簇集合 
     */  
    private ArrayList<ArrayList<float[]>> initCluster() {  
        ArrayList<ArrayList<float[]>> cluster = new ArrayList<ArrayList<float[]>>();  
        for (int i = 0; i < k; i++) {  
            cluster.add(new ArrayList<float[]>());  
        }  
  
        return cluster;  
    }  
  
    /** 
     * 计算两个点之间的距离 
     *  
     * @param element 
     *            点1 
     * @param center 
     *            点2 
     * @return 距离 
     */  
    private float distance(float[] element, float[] center) {  
        float distance = 0.0f;  
        float x = element[0] - center[0];  
        float y = element[1] - center[1];  
        float z = x * x + y * y;  
        distance = (float) Math.sqrt(z);
  
        return distance;  
    }  
  
    /** 
     * 获取距离集合中最小距离的位置 
     *  
     * @param distance 
     *            距离数组 
     * @return 最小距离在距离数组中的位置 
     */  
    private int minDistance(float[] distance) {  
        float minDistance = distance[0];  
        int minLocation = 0;  
        for (int i = 1; i < distance.length; i++) {  
            if (distance[i] < minDistance) {  
                minDistance = distance[i];  
                minLocation = i;  
            } else if (distance[i] == minDistance) // 如果相等，随机返回一个位置  
            {  
                if (random.nextInt(10) < 5) {  
                    minLocation = i;  
                }  
            }  
        }

        if(minDistance>threshold){
        	minLocation = k+1;
        }
  
        return minLocation;  
    }  
  
    /** 
     * 核心，将当前元素放到最小距离中心相关的簇中 
     */  
    private void clusterSet() {  
        float[] distance = new float[k];  
        for (int i = 0; i < dataSetLength; i++) {  
            for (int j = 0; j < k; j++) {  
                distance[j] = distance(dataSet.get(i), center.get(j));  
                // System.out.println("test2:"+"dataSet["+i+"],center["+j+"],distance="+distance[j]);  
  
            }  
            int minLocation = minDistance(distance);
            // System.out.println("test3:"+"dataSet["+i+"],minLocation="+minLocation);  
            // System.out.println();  
  
            if(minLocation>=0 && minLocation<k)
            cluster.get(minLocation).add(dataSet.get(i));// 核心，将当前元素放到最小距离中心相关的簇中  
  
        }  
    }  
  
    /** 
     * 求两点误差平方的方法 
     *  
     * @param element 
     *            点1 
     * @param center 
     *            点2 
     * @return 误差平方 
     */  
    private float errorSquare(float[] element, float[] center) {  
        float x = element[0] - center[0];  
        float y = element[1] - center[1];  
  
        float errSquare = x * x + y * y;  
  
        return errSquare;  
    }  
  
    /** 
     * 计算误差平方和准则函数方法 
     */  
    private void countRule() {  
        float jcF = 0;  
        for (int i = 0; i < cluster.size(); i++) {  
            for (int j = 0; j < cluster.get(i).size(); j++) {  
                jcF += errorSquare(cluster.get(i).get(j), center.get(i));  
  
            }  
        }  
        jc.add(jcF);  
    }  
  
    /** 
     * 设置新的簇中心方法 
     */  
    private void setNewCenter() {  
        for (int i = 0; i < k; i++) {  
            int n = cluster.get(i).size();  
            if (n != 0) {  
                float[] newCenter = { 0, 0 };  
                for (int j = 0; j < n; j++) {  
                    newCenter[0] += cluster.get(i).get(j)[0];  
                    newCenter[1] += cluster.get(i).get(j)[1];  
                }  
                // 设置一个平均值  
                newCenter[0] = newCenter[0] / n;  
                newCenter[1] = newCenter[1] / n;  
                center.set(i, newCenter);  
            }  
        }  
    }  
  
    /** 
     * 打印数据，测试用 
     *  
     * @param dataArray 
     *            数据集 
     * @param dataArrayName 
     *            数据集名称 
     */  
    public void printDataArray(ArrayList<float[]> dataArray,  
            String dataArrayName) {  
        for (int i = 0; i < dataArray.size(); i++) {  
            System.out.println("print:" + dataArrayName + "[" + i + "]={"  
                    + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "," + dataArray.get(i)[2] + "}");  
        }  
        System.out.println("===================================");  
    }  
  
    /** 
     * Kmeans算法核心过程方法 
     */  
    private void kmeans() {  
        init();  
        // printDataArray(dataSet,"initDataSet");  
        // printDataArray(center,"initCenter");  
  
        // 循环分组，直到误差不变为止  
        while (true) {  
            clusterSet();  
            // for(int i=0;i<cluster.size();i++)  
            // {  
            // printDataArray(cluster.get(i),"cluster["+i+"]");  
            // }  
  
            countRule();  
  
            // System.out.println("count:"+"jc["+m+"]="+jc.get(m));  
  
            // System.out.println();  
            // 误差不变了，分组完成  
            if (m != 0) {  
                if (jc.get(m) - jc.get(m - 1) == 0) {  
                    break;  
                }  
            }  
  
            setNewCenter();  
            // printDataArray(center,"newCenter");  
            m++;  
            cluster.clear();  
            cluster = initCluster();  
        }  
  
        // System.out.println("note:the times of repeat:m="+m);//输出迭代次数  
    }  
  
    /** 
     * 执行算法 
     */  
    public void execute() {  
        long startTime = System.currentTimeMillis();  
        System.out.println("kmeans begins");  
        kmeans();  
        long endTime = System.currentTimeMillis();  
        System.out.println("kmeans running time=" + (endTime - startTime)  
                + "ms");  
        System.out.println("kmeans ends");  
        System.out.println();  
    }  
    
    public  static void main(String[] args) throws IOException, FileNotFoundException  
    {  
        //初始化一个Kmean对象，将k置为10  
        kmeans k=new kmeans(4);
        
        InputStreamReader isr = new InputStreamReader(new FileInputStream("E:\\TD_IDF\\TD_IDF2.txt"), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
//		
        ArrayList<float[]> dataSet=new ArrayList<float[]>();  
		while(br.ready()){
			String str = br.readLine();
			String[] vector = str.split(",");
			float[] tdidf = new float[vector.length];
			for(int i=0; i<vector.length; i++){
				tdidf[i] = Float.parseFloat(vector[i]);
			}
			dataSet.add(tdidf);
		}
		System.out.println("load finished");
          
//        dataSet.add(new float[]{1,2,5});  
//        dataSet.add(new float[]{3,3,5});  
//        dataSet.add(new float[]{3,4,5});  
//        dataSet.add(new float[]{5,6,5});  
//        dataSet.add(new float[]{8,9,5});  
//        dataSet.add(new float[]{4,5,5});  
//        dataSet.add(new float[]{6,4,5});  
//        dataSet.add(new float[]{3,9,5});  
//        dataSet.add(new float[]{5,9,5});  
//        dataSet.add(new float[]{4,2,5});  
//        dataSet.add(new float[]{1,9,5});  
//        dataSet.add(new float[]{7,8,5});  
        //设置原始数据集  
        k.setDataSet(dataSet);
        //执行算法  
        k.execute();  
        //得到聚类结果  
        ArrayList<ArrayList<float[]>> cluster=k.getCluster();  
        //查看结果  
        for(int i=0;i<cluster.size();i++)  
        {  
            k.printDataArray(cluster.get(i), "cluster["+i+"]");  
        }  
          
    }  
}  