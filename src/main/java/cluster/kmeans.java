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
 * K鍧囧�鑱氱被绠楁硶 
 */  
public class kmeans {  
    private int k;// 鍒嗘垚澶氬皯绨� 
    private int m;// 杩唬娆℃暟  
    private int dataSetLength;// 鏁版嵁闆嗗厓绱犱釜鏁帮紝鍗虫暟鎹泦鐨勯暱搴� 
    private ArrayList<float[]> dataSet;// 鏁版嵁闆嗛摼琛� 
    private ArrayList<float[]> center;// 涓績閾捐〃  
    private ArrayList<ArrayList<float[]>> cluster; // 绨� 
    private ArrayList<Float> jc;// 璇樊骞虫柟鍜岋紝k瓒婃帴杩慸ataSetLength锛岃宸秺灏� 
    private Random random;  
    
    private float threshold = (float) 0.05;
  
    /** 
     * 璁剧疆闇�垎缁勭殑鍘熷鏁版嵁闆�
     *  
     * @param dataSet 
     */  
  
    public void setDataSet(ArrayList<float[]> dataSet) {  
        this.dataSet = dataSet;  
    }  
  
    /** 
     * 鑾峰彇缁撴灉鍒嗙粍 
     *  
     * @return 缁撴灉闆�
     */  
  
    public ArrayList<ArrayList<float[]>> getCluster() {  
        return cluster;  
    }  
  
    /** 
     * 鏋勯�鍑芥暟锛屼紶鍏ラ渶瑕佸垎鎴愮殑绨囨暟閲�
     *  
     * @param k 
     *            绨囨暟閲�鑻<=0鏃讹紝璁剧疆涓�锛岃嫢k澶т簬鏁版嵁婧愮殑闀垮害鏃讹紝缃负鏁版嵁婧愮殑闀垮害 
     */  
    public kmeans(int k) {  
        if (k <= 0) {  
            k = 1;  
        }  
        this.k = k;  
    }  
  
    /** 
     * 鍒濆鍖�
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
     * 濡傛灉璋冪敤鑰呮湭鍒濆鍖栨暟鎹泦锛屽垯閲囩敤鍐呴儴娴嬭瘯鏁版嵁闆�
     */  
    private void initDataSet() {  
        dataSet = new ArrayList<float[]>();  
        // 鍏朵腑{6,3}鏄竴鏍风殑锛屾墍浠ラ暱搴︿负15鐨勬暟鎹泦鍒嗘垚14绨囧拰15绨囩殑璇樊閮戒负0  
        float[][] dataSetArray = new float[][] { { 8, 2 }, { 3, 4 }, { 2, 5 },  
                { 4, 2 }, { 7, 3 }, { 6, 2 }, { 4, 7 }, { 6, 3 }, { 5, 3 },  
                { 6, 3 }, { 6, 9 }, { 1, 6 }, { 3, 9 }, { 4, 1 }, { 8, 6 } };  
  
        for (int i = 0; i < dataSetArray.length; i++) {  
            dataSet.add(dataSetArray[i]);  
        }  
    }  
  
    /** 
     * 鍒濆鍖栦腑蹇冩暟鎹摼琛紝鍒嗘垚澶氬皯绨囧氨鏈夊灏戜釜涓績鐐�
     *  
     * @return 涓績鐐归泦 
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
                // 涓嶆竻妤歠or寰幆瀵艰嚧j鏃犳硶鍔�  
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
  
        // 娴嬭瘯闅忔満鏁扮敓鎴愭儏鍐� 
        // for(int i=0;i<k;i++)  
        // {  
        // System.out.println("test1:randoms["+i+"]="+randoms[i]);  
        // }  
  
        // System.out.println();  
        for (int i = 0; i < k; i++) {  
            center.add(dataSet.get(randoms[i]));// 鐢熸垚鍒濆鍖栦腑蹇冮摼琛� 
        }  
        return center;  
    }  
  
    /** 
     * 鍒濆鍖栫皣闆嗗悎 
     *  
     * @return 涓�釜鍒嗕负k绨囩殑绌烘暟鎹殑绨囬泦鍚�
     */  
    private ArrayList<ArrayList<float[]>> initCluster() {  
        ArrayList<ArrayList<float[]>> cluster = new ArrayList<ArrayList<float[]>>();  
        for (int i = 0; i < k; i++) {  
            cluster.add(new ArrayList<float[]>());  
        }  
  
        return cluster;  
    }  
  
    /** 
     * 璁＄畻涓や釜鐐逛箣闂寸殑璺濈 
     *  
     * @param element 
     *            鐐� 
     * @param center 
     *            鐐� 
     * @return 璺濈 
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
     * 鑾峰彇璺濈闆嗗悎涓渶灏忚窛绂荤殑浣嶇疆 
     *  
     * @param distance 
     *            璺濈鏁扮粍 
     * @return 鏈�皬璺濈鍦ㄨ窛绂绘暟缁勪腑鐨勪綅缃�
     */  
    private int minDistance(float[] distance) {  
        float minDistance = distance[0];  
        int minLocation = 0;  
        for (int i = 1; i < distance.length; i++) {  
            if (distance[i] < minDistance) {  
                minDistance = distance[i];  
                minLocation = i;  
            } else if (distance[i] == minDistance) // 濡傛灉鐩哥瓑锛岄殢鏈鸿繑鍥炰竴涓綅缃� 
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
     * 鏍稿績锛屽皢褰撳墠鍏冪礌鏀惧埌鏈�皬璺濈涓績鐩稿叧鐨勭皣涓�
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
            cluster.get(minLocation).add(dataSet.get(i));// 鏍稿績锛屽皢褰撳墠鍏冪礌鏀惧埌鏈�皬璺濈涓績鐩稿叧鐨勭皣涓� 
  
        }  
    }  
  
    /** 
     * 姹備袱鐐硅宸钩鏂圭殑鏂规硶 
     *  
     * @param element 
     *            鐐� 
     * @param center 
     *            鐐� 
     * @return 璇樊骞虫柟 
     */  
    private float errorSquare(float[] element, float[] center) {  
        float x = element[0] - center[0];  
        float y = element[1] - center[1];  
  
        float errSquare = x * x + y * y;  
  
        return errSquare;  
    }  
  
    /** 
     * 璁＄畻璇樊骞虫柟鍜屽噯鍒欏嚱鏁版柟娉�
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
     * 璁剧疆鏂扮殑绨囦腑蹇冩柟娉�
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
                // 璁剧疆涓�釜骞冲潎鍊� 
                newCenter[0] = newCenter[0] / n;  
                newCenter[1] = newCenter[1] / n;  
                center.set(i, newCenter);  
            }  
        }  
    }  
  
    /** 
     * 鎵撳嵃鏁版嵁锛屾祴璇曠敤 
     *  
     * @param dataArray 
     *            鏁版嵁闆�
     * @param dataArrayName 
     *            鏁版嵁闆嗗悕绉�
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
     * Kmeans绠楁硶鏍稿績杩囩▼鏂规硶 
     */  
    private void kmeans() {  
        init();  
        // printDataArray(dataSet,"initDataSet");  
        // printDataArray(center,"initCenter");  
  
        // 寰幆鍒嗙粍锛岀洿鍒拌宸笉鍙樹负姝� 
        while (true) {  
            clusterSet();  
            // for(int i=0;i<cluster.size();i++)  
            // {  
            // printDataArray(cluster.get(i),"cluster["+i+"]");  
            // }  
  
            countRule();  
  
            // System.out.println("count:"+"jc["+m+"]="+jc.get(m));  
  
            // System.out.println();  
            // 璇樊涓嶅彉浜嗭紝鍒嗙粍瀹屾垚  
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
  
        // System.out.println("note:the times of repeat:m="+m);//杈撳嚭杩唬娆℃暟  
    }  
  
    /** 
     * 鎵ц绠楁硶 
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
        //鍒濆鍖栦竴涓狵mean瀵硅薄锛屽皢k缃负10  
        kmeans k=new kmeans(4);
        
        InputStreamReader isr = new InputStreamReader(new FileInputStream("C:\\Users\\Administrator\\Desktop\\TD_IDF\\TD_IDF_new.txt"), "UTF-8");
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
        //璁剧疆鍘熷鏁版嵁闆� 
        k.setDataSet(dataSet);
        //鎵ц绠楁硶  
        k.execute();  
        //寰楀埌鑱氱被缁撴灉  
        ArrayList<ArrayList<float[]>> cluster=k.getCluster();  
        //鏌ョ湅缁撴灉  
        for(int i=0;i<cluster.size();i++)  
        {  
            k.printDataArray(cluster.get(i), "cluster["+i+"]");  
        }  
          
    }  
}  