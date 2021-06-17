package sunyard;

public class Demo {
    public static void main(String[] args) {

        double[] a1={5,10,6,8,7,5};
        double[] a2={5,5,5,5,5,5};
        double[] a3={121,11,6,7,8,5};
        double y=7;

        System.out.println("方差:");
        System.out.println(fangcha(a1, y));
        System.out.println(fangcha(a2, y));
        System.out.println(fangcha(a3, y));

        System.out.println("标准差:");
        System.out.println(StandardDiviation1(a1, y));
        System.out.println(StandardDiviation1(a2, y));
        System.out.println(StandardDiviation1(a3, y));
        System.out.println("与y的离散差:");
        System.out.println(StandardDiviation(a1, y));
        System.out.println(StandardDiviation(a2, y));
        System.out.println(StandardDiviation(a3, y));
    }


    /**
     * 计算一组数据跟另一个数据的标准差
     * @param x
     * @param y
     * @return
     */
    public static double StandardDiviation(double[] x,double y) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - y) * (x[i] - y);
        }
        //reture Math.sqrt(dVar/(m-1));//
        return Math.sqrt(dVar / m);
    }

    /**
     * 计算一组数据的标准差
     * @param x
     * @param y
     * @return
     */
    public static double StandardDiviation1(double[] x,double y) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        //reture Math.sqrt(dVar/(m-1));
        return Math.sqrt(dVar / m);
    }
    /**
     * 计算一组数据的方差
     * @param x
     * @param y
     * @return
     */
    public static double fangcha(double[] x,double y) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        //reture Math.sqrt(dVar/(m-1));
        return dVar / m;
    }


    /**
     * 计算一组数据的标准差
     * @param x
     * @param y
     * @return
     */
//    public static double test(double[] x,double y) {
//
//
//
//
//




//        int m = x.length;
//        double sum = 0;
//        for (int i = 0; i < m; i++) {//求和
//            sum += x[i];
//        }
//        double dAve = sum / m;//求平均值
//        double dVar = 0;
//        for (int i = 0; i < m; i++) {//求方差
//            dVar += (x[i] - dAve) * (x[i] - dAve);
//        }
//        //reture Math.sqrt(dVar/(m-1));
//        return Math.sqrt(dVar / m);
//    }






}
