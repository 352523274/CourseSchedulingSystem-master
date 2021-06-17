package paiban;


import com.xnxy.CourseSchedulingSystem.Bean.vo.ConstantInfo;
import org.apache.commons.lang3.ArrayUtils;
import paiban.result.AxiosResult;
import paiban.result.AxiosStatus;
import paiban.result.MyResultException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataUtil {
    static String para; //总参数Json
    static HashMap<String, Object> paraObject; //总参数Object

    //必须参数
    static String StartDate; //开始时间字符串2021-05-01
    static String EndDate; //结束时间字符串"2021-05-31",
    static HashMap<String, String> date2NoMap = new HashMap<>(); //日期基因编码对应map
    static HashMap<String, String> no2dateMap = new HashMap<>(); //日期基因编码对应map

    static ArrayList<String> Shift; //全部班次ArrayList["周末主班","晚班","中班","早班","周末值班"]
    static HashMap<String, String> class2NoMap = new HashMap<>();
    ; //班次基因编码对应map
    static HashMap<String, String> no2classMap = new HashMap<>();
    ; //班次基因编码对应map

    static ArrayList<String> ListofPersons; //全部人员"ListofPersons":["111912","111231","100259","106367","108865","106870","105886","107575","111411","100295"],
    static HashMap<String, String> person2NoMap = new HashMap<>();
    ; //班次基因编码对应map
    static HashMap<String, String> no2personMap = new HashMap<>();
    ; //班次基因编码对应map

    static HashMap<String, HashMap<String, Integer>> ManPowerSet = new HashMap<>();
    ; //{"2021-05-01": {"周末主班": 3,"周末值班": 3},"2021-05-02": {"周末主班": 3,"周末值班": 3},
    static TreeMap<String, HashMap<String, Integer>> ManPowerSetTreeMap = new TreeMap<>(); //{"2021-05-01": {"周末主班": 3,"周末值班": 3},"2021-05-02": {"周末主班": 3,"周末值班": 3},


    //可选参数,可为null,可空值""
    static HashMap<String, Object> RuleSets; //总规则map
    static HashMap<String, ArrayList<ArrayList<String>>> AdjacentClassSet; //总班务配置:互斥班务"AdjacentClassSet":{"ContridictClass":[["晚班","早班"]],"DependentClass":[["早班","中班"]]}
    static ArrayList<String> ContridictClass = new ArrayList<>();//互斥班务  0:晚班  1早班
    static ArrayList<String> DependentClass = new ArrayList<>();//互依班务  0:早班  1中班


    static HashMap<String, Object> TurnSet; //总上班规则配置(最大最小必须工作日期等)
    static ArrayList<ArrayList<Object>> MinHoliday; //最小休息天数Liat:"MinHoliday": [["100259",5]],
    static ArrayList<ArrayList<Object>> MaxHoliday; //最大休息天数List:"MaxHoliday": [["100259",5]],
    static ArrayList<ArrayList<Object>> MaxContinueWorkDay; //最大连续工作天数List:"MaxContinueWorkDay": [["100259",5]],
    static ArrayList<ArrayList<Object>> MinContinueWorkDay; //最小连续工作天数List:"MinContinueWorkDay": [["100259",5]],
    static HashMap<String, Integer> MinHolidayMap = new HashMap<>(); //最小连续工作天数List:"MinContinueWorkDay": [["100259",5]],
    static HashMap<String, Integer> MaxHolidayMap = new HashMap<>(); //最大休息天数List:"MaxHoliday": [["100259",5]],
    static HashMap<String, Integer> MaxContinueWorkDayMap = new HashMap<>(); //最大连续工作天数List:"MaxContinueWorkDay": [["100259",5]],
    static HashMap<String, Integer> MinContinueWorkDayMap = new HashMap<>(); //最小连续工作天数List:"MinContinueWorkDay": [["100259",5]],


    static ArrayList<ArrayList<String>> Holiday; //必须休息的日期[["100259","2021-05-01"]],
    static ArrayList<ArrayList<String>> Work; //必须工作的日期[["100259","2021-05-01"]],
    static HashMap<String, ArrayList<String>> HolidayMap = new HashMap<>();//必须休息的日期的 map   person2date
    static HashMap<String, ArrayList<String>> HolidayMapDate2Person = new HashMap<>();//必须休息的日期的 map   Date2Person
    static HashMap<String, ArrayList<String>> WorkMap = new HashMap<>();//必须工作日期的 map      person2date
    static HashMap<String, ArrayList<String>> WorkMapDate2Person = new HashMap<>();//必须工作日期的 map      Date2Person


    static HashMap<String, HashMap<String, Object>> BalanceSet; //平衡班次配置"BalanceSet":{"晚班":{"member":["111912","111231","100259","106367","108865","106870","105886","107575","111411","100295"],"Deviation":3}},
    static HashMap<String, ArrayList<ArrayList<String>>> ForbiddenSet = new HashMap<>(); //"ForbiddenSet":{"ForbiddenClass":[["100259","晚班"]]}}
    static HashMap<String, ArrayList<String>> ForbiddenClassMap = new HashMap<>();//key:person    v:班次list

    //相关常量
    static final String DATE = "DATE"; //基因日期
    static final String CLASS = "CLASS"; //基因班次
    static final String PERSON_NUM = "PERSON_NUM"; //基因人员数量
    static final String PERSON = "PERSON"; //基因人员


//    static final int SPECIES_NUM = 200; //种群数
    static final int DEVELOP_NUM = 50; //进化代数
//    static final float pcl = 0.1f, pch = 0.95f;//交叉概率
//    static final float pm = 0.4f;//变异概率


    private static void init(String para){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //城市坐标集合
//        para = "{\"StartDate\":\"2021-05-01\",\"Shift\":[\"周末值班\",\"周末主班\",\"晚班\",\"中班\",\"早班\"],\"RuleSets\":{\"AdjacentClassSet\":{\"ContridictClass\":[[\"晚班\",\"早班\"]]},\"TurnSet\":{\"MinHoliday\":[[\"100259\",5]],\"MinContinueWorkDay\":[[\"100259\",3]],\"Holiday\":[[\"100259\",\"2021-05-01\"]],\"Work\":[[\"100259\",\"2021-05-05\"],[\"100259\",\"2021-05-06\"]],\"MaxContinueWorkDay\":[[\"111912\",7],[\"111231\",7],[\"100259\",8],[\"106367\",7],[\"108865\",7],[\"106870\",7],[\"105886\",7],[\"107575\",7],[\"111411\",7],[\"100295\",7]],\"MaxHoliday\":[[\"100259\",8]]},\"BalanceSet\":{\"晚班\":{\"member\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"111411\",\"100295\"],\"Deviation\":3}},\"ForbiddenSet\":{\"ForbiddenClass\":[[\"100259\",\"晚班\"]]}},\"ManPowerSet\":{\"2021-05-01\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-02\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-03\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-04\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-05\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-06\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-07\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-08\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-09\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-10\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-11\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-12\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-13\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-14\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-15\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-16\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-17\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-18\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-19\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-20\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-21\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-22\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-23\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-24\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-25\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-26\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-27\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-28\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-29\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-30\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-31\":{\"早班\":2,\"中班\":2,\"晚班\":2}},\"ListofPersons\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"111411\",\"100295\"],\"EndDate\":\"2021-05-31\"}";
//        para = "{\"StartDate\":\"2021-05-01\",\"Shift\":[\"周末主班\",\"晚班\",\"中班\",\"早班\",\"周末值班\"],\"RuleSets\":{\"AdjacentClassSet\":{\"ContridictClass\":[[\"晚班\",\"早班\"]],\"DependentClass\":[[\"早班\",\"中班\"]]},\"TurnSet\":{\"MinHoliday\":[[\"111912\",5],[\"111231\",5],[\"100259\",5],[\"106367\",5],[\"108865\",5],[\"106870\",5],[\"105886\",5],[\"107575\",5],[\"100658\",5],[\"111181\",5]],\"MinContinueWorkDay\":[[\"111912\",3],[\"111231\",3],[\"100259\",3],[\"106367\",3],[\"108865\",3],[\"106870\",3],[\"105886\",3],[\"107575\",3],[\"100658\",3],[\"111181\",3]],\"MaxContinueWorkDay\":[[\"111912\",7],[\"111231\",7],[\"100259\",7],[\"106367\",7],[\"108865\",7],[\"106870\",7],[\"105886\",7],[\"107575\",7],[\"100658\",7],[\"111181\",7]],\"MaxHoliday\":[[\"111912\",8],[\"111231\",8],[\"100259\",8],[\"106367\",8],[\"108865\",8],[\"106870\",8],[\"105886\",8],[\"107575\",8],[\"100658\",8],[\"111181\",8]]},\"BalanceSet\":{\"晚班\":{\"member\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"Deviation\":3}}},\"ManPowerSet\":{\"2021-05-01\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-02\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-03\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-04\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-05\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-06\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-07\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-08\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-09\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-10\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-11\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-12\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-13\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-14\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-15\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-16\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-17\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-18\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-19\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-20\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-21\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-22\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-23\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-24\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-25\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-26\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-27\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-28\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-29\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-30\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-31\":{\"早班\":2,\"中班\":2,\"晚班\":2}},\"ListofPersons\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"EndDate\":\"2021-05-31\"}";
//        para = "{\"StartDate\":\"2021-05-01\",\"Shift\":[\"周末值班\",\"周末主班\",\"晚班\",\"中班\",\"早班\"],\"RuleSets\":{\"AdjacentClassSet\":{\"ContridictClass\":[[\"晚班\",\"早班\"]],\"DependentClass\":[[\"早班\",\"中班\"]]},\"TurnSet\":{\"MinHoliday\":[[\"111912\",5],[\"111231\",5],[\"100259\",5],[\"106367\",5],[\"108865\",5],[\"106870\",5],[\"105886\",5],[\"107575\",5],[\"100658\",5],[\"111181\",5]],\"MinContinueWorkDay\":[[\"111912\",3],[\"111231\",3],[\"100259\",3],[\"106367\",3],[\"108865\",3],[\"106870\",3],[\"105886\",3],[\"107575\",3],[\"100658\",3],[\"111181\",3]],\"Holiday\":[[\"100259\",\"2021-05-01\"],[\"100259\",\"2021-05-02\"],[\"100259\",\"2021-05-03\"],[\"100259\",\"2021-05-04\"],[\"100259\",\"2021-05-05\"]],\"Work\":[[\"100259\",\"2021-05-06\"],[\"100259\",\"2021-05-07\"],[\"100259\",\"2021-05-08\"],[\"100259\",\"2021-05-09\"],[\"100259\",\"2021-05-10\"]],\"MaxContinueWorkDay\":[[\"111912\",7],[\"111231\",7],[\"100259\",7],[\"106367\",7],[\"108865\",7],[\"106870\",7],[\"105886\",7],[\"107575\",7],[\"100658\",7],[\"111181\",7]],\"MaxHoliday\":[[\"111912\",8],[\"111231\",8],[\"100259\",8],[\"106367\",8],[\"108865\",8],[\"106870\",8],[\"105886\",8],[\"107575\",8],[\"100658\",8],[\"111181\",8]]},\"BalanceSet\":{\"晚班\":{\"member\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"Deviation\":3}},\"ForbiddenSet\":{\"ForbiddenClass\":[[\"100259\",\"中班\"]]}},\"ManPowerSet\":{\"2021-05-01\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-02\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-03\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-04\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-05\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-06\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-07\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-08\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-09\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-10\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-11\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-12\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-13\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-14\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-15\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-16\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-17\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-18\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-19\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-20\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-21\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-22\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-23\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-24\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-25\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-26\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-27\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-28\":{\"早班\":2,\"中班\":2,\"晚班\":2},\"2021-05-29\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-30\":{\"周末主班\":3,\"周末值班\":3},\"2021-05-31\":{\"早班\":2,\"中班\":2,\"晚班\":2}},\"ListofPersons\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"EndDate\":\"2021-05-31\"}";
//        para = "{\"StartDate\":\"2021-05-01\",\"Shift\":[\"周末值班\",\"周末主班\",\"晚班\",\"中班\",\"早班\"],\"RuleSets\":{\"AdjacentClassSet\":{\"ContridictClass\":[[\"晚班\",\"早班\"]],\"DependentClass\":[[\"早班\",\"中班\"]]},\"TurnSet\":{\"MinHoliday\":[[\"111912\",5],[\"111231\",5],[\"100259\",5],[\"106367\",5],[\"108865\",5],[\"106870\",5],[\"105886\",5],[\"107575\",5],[\"100658\",5],[\"111181\",5]],\"MinContinueWorkDay\":[[\"111912\",3],[\"111231\",3],[\"100259\",3],[\"106367\",3],[\"108865\",3],[\"106870\",3],[\"105886\",3],[\"107575\",3],[\"100658\",3],[\"111181\",3]],\"Holiday\":[[\"100259\",\"2021-05-01\"],[\"100259\",\"2021-05-02\"],[\"100259\",\"2021-05-03\"],[\"100259\",\"2021-05-04\"],[\"100259\",\"2021-05-05\"]],\"Work\":[[\"100259\",\"2021-05-06\"],[\"100259\",\"2021-05-07\"],[\"100259\",\"2021-05-08\"],[\"100259\",\"2021-05-09\"],[\"100259\",\"2021-05-10\"]],\"MaxContinueWorkDay\":[[\"111912\",7],[\"111231\",7],[\"100259\",7],[\"106367\",7],[\"108865\",7],[\"106870\",7],[\"105886\",7],[\"107575\",7],[\"100658\",7],[\"111181\",7]],\"MaxHoliday\":[[\"111912\",8],[\"111231\",8],[\"100259\",8],[\"106367\",8],[\"108865\",8],[\"106870\",8],[\"105886\",8],[\"107575\",8],[\"100658\",8],[\"111181\",8]]},\"BalanceSet\":{\"晚班\":{\"member\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"Deviation\":3}},\"ForbiddenSet\":{\"ForbiddenClass\":[[\"100259\",\"中班\"]]}},\"ManPowerSet\":{\"2021-05-01\":{\"周末主班\":5,\"周末值班\":4},\"2021-05-02\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-03\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-04\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-05\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-06\":{\"周末主班\":5,\"周末值班\":2},\"2021-05-07\":{\"早班\":2,\"中班\":2,\"晚班\":3},\"2021-05-08\":{\"周末主班\":4,\"周末值班\":4},\"2021-05-09\":{\"周末主班\":2,\"周末值班\":3},\"2021-05-10\":{\"早班\":3,\"中班\":4,\"晚班\":0},\"2021-05-11\":{\"早班\":1,\"中班\":2,\"晚班\":3},\"2021-05-12\":{\"早班\":2,\"中班\":3,\"晚班\":2},\"2021-05-13\":{\"早班\":2,\"中班\":3,\"晚班\":2},\"2021-05-14\":{\"早班\":4,\"中班\":2,\"晚班\":1},\"2021-05-15\":{\"周末主班\":4,\"周末值班\":3},\"2021-05-16\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-17\":{\"早班\":4,\"中班\":1,\"晚班\":2},\"2021-05-18\":{\"早班\":2,\"中班\":1,\"晚班\":4},\"2021-05-19\":{\"早班\":3,\"中班\":2,\"晚班\":2},\"2021-05-20\":{\"早班\":4,\"中班\":0,\"晚班\":3},\"2021-05-21\":{\"早班\":1,\"中班\":2,\"晚班\":3},\"2021-05-22\":{\"周末主班\":4,\"周末值班\":2},\"2021-05-23\":{\"周末主班\":5,\"周末值班\":2},\"2021-05-24\":{\"早班\":2,\"中班\":3,\"晚班\":2},\"2021-05-25\":{\"早班\":1,\"中班\":2,\"晚班\":4},\"2021-05-26\":{\"早班\":2,\"中班\":3,\"晚班\":2},\"2021-05-27\":{\"早班\":5,\"中班\":2,\"晚班\":0},\"2021-05-28\":{\"早班\":0,\"中班\":5,\"晚班\":2},\"2021-05-29\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-30\":{\"周末主班\":3,\"周末值班\":4},\"2021-05-31\":{\"早班\":5,\"中班\":2,\"晚班\":0}},\"ListofPersons\":[\"111912\",\"111231\",\"100259\",\"106367\",\"108865\",\"106870\",\"105886\",\"107575\",\"100658\",\"111181\"],\"EndDate\":\"2021-05-31\"}";
        paraObject = (HashMap<String, Object>) BaseUtil.transJson2Obj(para, HashMap.class);
        //参数赋值

        //必须参数
        StartDate = (String) paraObject.get("StartDate");
        EndDate = (String) paraObject.get("EndDate");

        Shift = (ArrayList<String>) paraObject.get("Shift");
        ListofPersons = (ArrayList<String>) paraObject.get("ListofPersons");
        ManPowerSet = (HashMap<String, HashMap<String, Integer>>) paraObject.get("ManPowerSet");
        //给ManPowerSetTreeMap赋值
        TreeMap<String, HashMap<String, Integer>> stringObjectTreeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = simpleDateFormat.parse(o1);
                    date2 = simpleDateFormat.parse(o2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert date1 != null;
                return date1.compareTo(date2);
            }
        });

            /*ManPowerSet.forEach((k, v) -> {
                stringObjectTreeMap.put(k, v);
            });*/
        for (String itNext : ManPowerSet.keySet()) {
            stringObjectTreeMap.put(itNext, ManPowerSet.get(itNext));
        }
        ManPowerSetTreeMap = stringObjectTreeMap;
        //给date2NoMap赋值
        Set<Map.Entry<String, HashMap<String, Integer>>> entries = ManPowerSetTreeMap.entrySet();
        Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = entries.iterator();
        int ii = 1;
        while (iterator.hasNext()) {

            Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
            String key = next.getKey();
            HashMap<String, Integer> value = next.getValue();
            date2NoMap.put(key, BaseUtil.str2gene1to01(ii + ""));
            no2dateMap.put(BaseUtil.str2gene1to01(ii + ""), key);
            ii++;
        }
        //给班次class2NoMap赋值  包含休息:00
        for (int i = 0; i < Shift.size(); i++) {
            class2NoMap.put(Shift.get(i), BaseUtil.str2gene1to01((i + 1) + ""));
            no2classMap.put(BaseUtil.str2gene1to01((i + 1) + ""), Shift.get(i));
        }
        class2NoMap.put("休息", "00");
        no2classMap.put("00", "休息");
        //给人员编号map  person2NoMap赋值
        for (int i = 0; i < ListofPersons.size(); i++) {
            person2NoMap.put(ListofPersons.get(i), BaseUtil.str2gene1to0001((i + 1) + ""));
            no2personMap.put(BaseUtil.str2gene1to0001((i + 1) + ""), ListofPersons.get(i));
        }


        //非必须参数
        RuleSets = (HashMap<String, Object>) paraObject.get("RuleSets");
        if (RuleSets != null) {
            AdjacentClassSet = (HashMap<String, ArrayList<ArrayList<String>>>) RuleSets.get("AdjacentClassSet");
            BalanceSet = (HashMap<String, HashMap<String, Object>>) RuleSets.get("BalanceSet");
            ForbiddenSet = (HashMap<String, ArrayList<ArrayList<String>>>) RuleSets.get("ForbiddenSet");
            TurnSet = (HashMap<String, Object>) RuleSets.get("TurnSet");
            if (TurnSet != null) {
                MinHoliday = (ArrayList<ArrayList<Object>>) TurnSet.get("MinHoliday");
                MaxHoliday = (ArrayList<ArrayList<Object>>) TurnSet.get("MaxHoliday");
                MaxContinueWorkDay = (ArrayList<ArrayList<Object>>) TurnSet.get("MaxContinueWorkDay");
                MinContinueWorkDay = (ArrayList<ArrayList<Object>>) TurnSet.get("MinContinueWorkDay");
                //给最大最小休息天数赋值MinHolidayMap   MaxHolidayMap  MaxContinueWorkDayMap   MinContinueWorkDayMap
                if (MinHoliday != null) {
                    for (int i = 0; i < MinHoliday.size(); i++) {
                        String person = (String) MinHoliday.get(i).get(0);
                        Integer days = (Integer) MinHoliday.get(i).get(1);
                        MinHolidayMap.put(person, days);
                    }
                }
                if (MaxHoliday != null) {
                    for (int i = 0; i < MaxHoliday.size(); i++) {
                        String person = (String) MaxHoliday.get(i).get(0);
                        Integer days = (Integer) MaxHoliday.get(i).get(1);
                        MaxHolidayMap.put(person, days);
                    }
                }
                if (MaxContinueWorkDay != null) {
                    for (int i = 0; i < MaxContinueWorkDay.size(); i++) {
                        String person = (String) MaxContinueWorkDay.get(i).get(0);
                        Integer days = (Integer) MaxContinueWorkDay.get(i).get(1);
                        MaxContinueWorkDayMap.put(person, days);
                    }
                }
                if (MinContinueWorkDay != null) {
                    for (int i = 0; i < MinContinueWorkDay.size(); i++) {
                        String person = (String) MinContinueWorkDay.get(i).get(0);
                        Integer days = (Integer) MinContinueWorkDay.get(i).get(1);
                        MinContinueWorkDayMap.put(person, days);
                    }
                }
                Holiday = (ArrayList<ArrayList<String>>) TurnSet.get("Holiday");
                Work = (ArrayList<ArrayList<String>>) TurnSet.get("Work");
            }
            //给互斥互依班务赋值
            if (AdjacentClassSet != null) {
                ArrayList<ArrayList<String>> contridictClasstemp = AdjacentClassSet.get("ContridictClass");
                ArrayList<ArrayList<String>> DependentClasstemp = AdjacentClassSet.get("DependentClass");
                if (contridictClasstemp != null) {
                    ContridictClass.add(0, contridictClasstemp.get(0).get(0));
                    ContridictClass.add(1, contridictClasstemp.get(0).get(1));
                }
                if (DependentClasstemp != null) {
                    DependentClass.add(0, DependentClasstemp.get(0).get(0));
                    DependentClass.add(1, DependentClasstemp.get(0).get(1));
                }
            }
            //给HolidayMap赋值
            if (Holiday != null) {
                HolidayMap = new HashMap<>();
                HolidayMapDate2Person = new HashMap<>();
                for (int i = 0; i < Holiday.size(); i++) {
                    String person = Holiday.get(i).get(0);
                    String date = Holiday.get(i).get(1);
                    if (HolidayMap.containsKey(person)) {
                        ArrayList<String> holidayDateList = HolidayMap.get(person);
                        holidayDateList.add(date);
                        //给HolidayMapDate2Person赋值
                        if (HolidayMapDate2Person.get(date) == null) {
                            ArrayList<String> holidayDateDate2PersonList = new ArrayList<>();
                            holidayDateDate2PersonList.add(person);
                            HolidayMapDate2Person.put(date, holidayDateDate2PersonList);
                        } else {
                            ArrayList<String> strings = HolidayMapDate2Person.get(date);
                            strings.add(person);
                            HolidayMapDate2Person.put(date, strings);
                        }
                    } else {
                        ArrayList<String> holidayDateList = new ArrayList<>();
                        holidayDateList.add(date);
                        HolidayMap.put(person, holidayDateList);
                        //给HolidayMapDate2Person赋值
                        if (HolidayMapDate2Person.get(date) == null) {
                            ArrayList<String> holidayDateDate2PersonList = new ArrayList<>();
                            holidayDateDate2PersonList.add(person);
                            HolidayMapDate2Person.put(date, holidayDateDate2PersonList);
                        } else {
                            ArrayList<String> strings = HolidayMapDate2Person.get(date);
                            strings.add(person);
                            HolidayMapDate2Person.put(date, strings);
                        }
                    }
                }
            }
            //给Work赋值
            if (Work != null) {
                WorkMap = new HashMap<>();
                WorkMapDate2Person = new HashMap<>();
                for (int i = 0; i < Work.size(); i++) {
                    String person = Work.get(i).get(0);
                    String date = Work.get(i).get(1);
                    if (WorkMap.containsKey(person)) {
                        ArrayList<String> workDateList = WorkMap.get(person);
                        workDateList.add(date);
                        //WorkMapDate2Person赋值
                        if (WorkMapDate2Person.get(date) == null) {
                            ArrayList<String> holidayDateDate2PersonList = new ArrayList<>();
                            holidayDateDate2PersonList.add(person);
                            WorkMapDate2Person.put(date, holidayDateDate2PersonList);
                        } else {
                            ArrayList<String> strings = WorkMapDate2Person.get(date);
                            strings.add(person);
                            WorkMapDate2Person.put(date, strings);
                        }

                    } else {
                        ArrayList<String> workDateList = new ArrayList<>();
                        workDateList.add(date);
                        WorkMap.put(person, workDateList);
                        //给HolidayMapDate2Person赋值
                        if (WorkMapDate2Person.get(date) == null) {
                            ArrayList<String> holidayDateDate2PersonList = new ArrayList<>();
                            holidayDateDate2PersonList.add(person);
                            WorkMapDate2Person.put(date, holidayDateDate2PersonList);
                        } else {
                            ArrayList<String> strings = WorkMapDate2Person.get(date);
                            strings.add(person);
                            WorkMapDate2Person.put(date, strings);
                        }
                    }
                }
            }


            //给ForbiddenClassMap赋值
            if (ForbiddenSet != null && ForbiddenSet.containsKey("ForbiddenClass")) {
                ArrayList<ArrayList<String>> forbiddenClass = ForbiddenSet.get("ForbiddenClass");
                ForbiddenClassMap = new HashMap<>();
                for (int i = 0; i < forbiddenClass.size(); i++) {

                    String person = forbiddenClass.get(i).get(0);
                    String className = forbiddenClass.get(i).get(1);
                    if (ForbiddenClassMap.containsKey(person)) {
                        ArrayList<String> classList = ForbiddenClassMap.get(person);
                        classList.add(className);
                    } else {
                        ArrayList<String> classList = new ArrayList<>();
                        classList.add(className);
                        ForbiddenClassMap.put(person, classList);
                    }
                }
            }
        }

    }

    static {

    }

    /**
     * 初始参数的合理性校验
     */
    public static void checkData(String para) {
        init(para);
        //对日期班次人员的分布参数,进行遍历
        Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = ManPowerSet.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
            String key = next.getKey();
            HashMap<String, Integer> value = next.getValue();
            int personNum = 0;//这天一共需要上班的人员数量
            for (String s : Shift) {
                if (value.containsKey(s)) {
                    personNum += value.get(s);
                }
            }
            //如果存在必须休息日期,需要减掉人数
            ArrayList<String> persons = HolidayMap.get(key);
            int WorkablePersonNum = ListofPersons.size();
            if (persons != null && persons.size() > 0) {
                WorkablePersonNum = WorkablePersonNum - persons.size();
            }
            //人员不足抛出异常
            if (personNum > WorkablePersonNum) {
                throw new MyResultException(AxiosResult.error(AxiosStatus.PERSON_NUM_NOT_ENOUGH));
            }

        }
    }

    /**
     * 根据参数生成基因map
     * 全部基因的list:        resultList
     * 时间分组的基因:         resultListMap
     *
     * @return
     */
    public static HashMap<String, Object> createGene() throws ParseException {
        HashMap<String, Object> resultMap = new HashMap<>();

        ArrayList<String> resultList = new ArrayList<>();
        HashMap<String, ArrayList<String>> resultListMap = new HashMap<>();//以日期为key的基因list
        //按天遍历
        Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = ManPowerSet.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
            /**
             * "2021-05-01": {
             * "周末主班": 3,
             * "周末值班": 3
             * },
             */
            String key = next.getKey();//date
            HashMap<String, Integer> value = next.getValue();
            //日期编号
            String dateNo = date2NoMap.get(key);
            //此日期所有基因的list
            ArrayList<String> dateGene = new ArrayList<>();
            //暂存此日期所有休息的人(维护)
            ArrayList<String> restPersons = (ArrayList<String>) BaseUtil.transJson2Obj(BaseUtil.transObj2Json(ListofPersons), ArrayList.class);
            //此日期所有上班的人(维护)
            ArrayList<String> workPersons = new ArrayList<>();
            //此日期所有的必须上班的人
            ArrayList<String> WorkPersonList = WorkMapDate2Person.get(key);
            if (WorkPersonList == null) {
                WorkPersonList = new ArrayList<>();
            }
            //此日期所有的必须休息的人
            ArrayList<String> HolidayPersonList = HolidayMapDate2Person.get(key);
            if (HolidayPersonList == null) {
                HolidayPersonList = new ArrayList<>();
            }
            //根据人员获取这天的基因
            HashMap<String, String> per2gene = new HashMap<>();//不含休息基因

//            System.out.println(key);
            //对班次进行遍历
            for (int i = 0; i < Shift.size(); i++) {
                if (value.containsKey(Shift.get(i))) {
                    //最终的基因
                    StringBuilder gene = new StringBuilder();
                    //班次编号
                    String classNo = class2NoMap.get(Shift.get(i));
                    //班次人员数量
                    Integer personNum = value.get(Shift.get(i));
                    String personNumNo = BaseUtil.str2gene1to0001("" + personNum);
                    //随机personNum个人员(每次取一个)
                    //此班次中可以选来工作的人
                    ArrayList<String> restPersonstem = (ArrayList<String>) BaseUtil.transJson2Obj(BaseUtil.transObj2Json(restPersons), ArrayList.class);

                    ArrayList<Integer> ints = new ArrayList<>();
                    ArrayList<String> chosePerson = new ArrayList<>();//储存此班次中被选中的人
                    for (Integer integer = 0; integer < personNum; integer++) {
                        String persontemp = null;
                        boolean flag = false;//0取到了 1取到的不合理再取  Holiday  Work  ForbiddenClass
                        int loopNum = 0;//下面的循环执行次数
                        do {
                            flag = false;
                            //从休息人员并且没被之前循环选中的人里随机选一个
                            Integer[] ints1 = BaseUtil.randomCommon(0, restPersonstem.size(), 1);
                            for (int j = 0; j < ints1.length; j++) {
                                String person = restPersonstem.get(ints1[j]);
                                persontemp = person;
                                //必须休息的人被选中了,重新选择
                                if (HolidayPersonList.size() > 0) {
                                    for (int i1 = 0; i1 < HolidayPersonList.size(); i1++) {
                                        String temPerson = HolidayPersonList.get(i1);
                                        if (person.equalsIgnoreCase(temPerson)) {
                                            flag = true;
                                            restPersonstem.remove(person);//维护restPersonstem
                                        }
                                    }
                                }
                                if (ForbiddenClassMap != null) {
                                    //禁止此班次的人被选中了也要重新选择(如果有的话)
                                    ArrayList<String> forBclasslist = ForbiddenClassMap.get(person);
                                    if (forBclasslist != null) {
                                        for (int i1 = 0; i1 < forBclasslist.size(); i1++) {
                                            if (Shift.get(i).equalsIgnoreCase(forBclasslist.get(i1))) {
                                                flag = true;
                                                restPersonstem.remove(person);//维护restPersonstem
                                            }
                                        }
                                    }
                                }
                                //互斥班务(查看此人前天的班次是啥,如果是互斥的也返回ture  再次找人)
                                if (DataUtil.ContridictClass.size() > 0) {
                                    String beforeDate1 = BaseUtil.getBeforeDate(key, -1);//前一天的日期
                                    ArrayList<String> beforeDate1gene = resultListMap.get(beforeDate1);
                                    if (beforeDate1gene != null) {
                                        //找到此人的前天班次
                                        HashMap<String, HashMap<String, ArrayList<String>>> map = SchedulService.getClassFromPersonName(beforeDate1gene);
                                        HashMap<String, ArrayList<String>> class2per = map.get("class2per");
                                        HashMap<String, ArrayList<String>> per2class = map.get("per2class");
                                        String beforeDate1Class = per2class.get(person).get(0);
                                        if (beforeDate1Class.equalsIgnoreCase(ContridictClass.get(0)) && Shift.get(i).equalsIgnoreCase(ContridictClass.get(1))) {
                                            if (restPersonstem.size()>1){
                                                flag = true;
                                            }
//                                            restPersonstem.remove(person);//维护restPersonstem
                                        }
                                    }
                                }
                                //互依班务(查看此人前天的班次是啥,如果是互依的也返回ture  再次找人)
                                if (DataUtil.DependentClass.size() > 0) {
                                    String beforeDate1 = BaseUtil.getBeforeDate(key, -1);//前一天的日期
                                    ArrayList<String> beforeDate1gene = resultListMap.get(beforeDate1);
                                    if (beforeDate1gene != null) {
                                        //找到此人的前天班次
                                        HashMap<String, HashMap<String, ArrayList<String>>> map = SchedulService.getClassFromPersonName(beforeDate1gene);
                                        HashMap<String, ArrayList<String>> class2per = map.get("class2per");
                                        HashMap<String, ArrayList<String>> per2class = map.get("per2class");
                                        String beforeDate1Class = per2class.get(person).get(0);
                                        //如果今天有这个班次的话
                                        String class2 = DependentClass.get(1);
                                        HashMap<String, Integer> stringIntegerHashMap = ManPowerSetTreeMap.get(key);
                                        if (stringIntegerHashMap.containsKey(class2)) {
                                            if (beforeDate1Class.equalsIgnoreCase(DependentClass.get(0)) && !Shift.get(i).equalsIgnoreCase(DependentClass.get(1))) {
                                                if (restPersonstem.size()>8){
                                                    flag = true;
                                                }
//                                                restPersonstem.remove(person);//维护restPersonstem
                                            }
                                        }

                                    }
                                }

                            }
                            if (loopNum++ > 1000) {
                                //取人员一直取不到,报错
                                System.out.println(key);
                                throw new MyResultException(AxiosResult.error(AxiosStatus.PERSON_NUM_NOT_ENOUGH));
                            }
                        } while (flag);
                        //获取persontemp的index
                        restPersonstem.remove(persontemp);
                        ints.add(restPersons.indexOf(persontemp));
                        chosePerson.add(persontemp);
                    }
                    StringBuilder personNo = new StringBuilder();//随机的人员基因组合
                    ArrayList<String> personList = new ArrayList<>();//随机的人员list
                    for (int j = 0; j < chosePerson.size(); j++) {
                        personList.add(chosePerson.get(j));
                        workPersons.add(chosePerson.get(j));//维护今天的工作人员list
                        personNo.append(person2NoMap.get(chosePerson.get(j)));
                    }
//                    for (int j = 0; j < ints.size(); j++) {
//                        personList.add(restPersons.get(ints.get(j)));
//                        workPersons.add(restPersons.get(ints.get(j)));//维护今天的工作人员list
//                        personNo.append(person2NoMap.get(restPersons.get(ints.get(j))));
//                    }
                    for (int i1 = 0; i1 < personList.size(); i1++) {
                        restPersons.remove(personList.get(i1));//维护今天休息人员list
                    }
                    //生成基因
                    gene.append(dateNo + classNo + personNumNo + personNo);
                    //维护per2gene  //由人员获取此人员相关的班次基因
                    for (String s : personList) {
                        per2gene.put(s, gene.toString());
                    }
//                    if (key.equalsIgnoreCase("2021-05-08")){
//                        System.out.println("5-8号的"+Shift.get(i));
//                        System.out.println(dateNo + "00" + personNumNo + personNo);
//                    }
                    dateGene.add(gene.toString());
                    resultList.add(gene.toString());
                }
            }
            //必须上班人员安排WorkPersonList
            for (int i1 = 0; i1 < WorkPersonList.size(); i1++) {
//                System.out.println("必须上班的人");
//                System.out.println(key);
//                System.out.println(WorkPersonList);
                String newPerson = WorkPersonList.get(i1);
                if (restPersons.contains(newPerson)) {
                    //这天必须工作的人里有,在休息人员list里,将此人员与工作的人互换
                    //(1.工作的人不能是必须工作的人 2.工作的人的班次不能是必须工作的人的禁止班次 3.工作的人的班次不能和 必须工作的人的互依互斥班次限制)
                    //随机获取一个已经安排工作的人(从workPersons中获取)
                    //暂存人员
                    ArrayList<String> workPersonstem = (ArrayList<String>) BaseUtil.transJson2Obj(BaseUtil.transObj2Json(workPersons), ArrayList.class);
                    ArrayList<Integer> ints = new ArrayList<>();
                    Integer[] ints1 = null;
                    boolean flag1 = false;//0取到了 1取到的不合理再取  Holiday  Work  ForbiddenClass
                    int loopNum1 = 0;//下面的循环执行次数
                    do {
                        flag1 = false;
                        ints1 = BaseUtil.randomCommon(0, workPersonstem.size(), 1);
//                        System.out.println(Arrays.toString(ints1));
//                        System.out.println(ints1.length);
//                        System.out.println(workPersonstem);
                        String person = workPersonstem.get(ints1[0]);
                        //必须工作的人被选中了,重新选择
                        if (WorkPersonList.contains(person)) {
//                            System.out.println("必须工作的人被选中了");
                            flag1 = true;
                        }
                        //选中的人person的目前班次信息获取
                        String className = (String) getGeneSource(CLASS, per2gene.get(person));
                        //选中的人的班次刚好是必须上班那个人得禁止班次,也要重新选择(如果有禁止班次的话)
                        ArrayList<String> forBclasslist = ForbiddenClassMap.get(newPerson);
                        if (forBclasslist != null) {
                            for (int i11 = 0; i11 < forBclasslist.size(); i11++) {
                                if (className.equalsIgnoreCase(forBclasslist.get(i1))) {
//                                    System.out.println("选中的人得班次时必须工作的人得禁止班次");
                                    flag1 = true;
                                }
                            }
                        }


                        //互斥班务(查看newperson前天的班次是啥,如果是互斥的也返回ture  再次找人)
                        if (DataUtil.ContridictClass.size() > 0) {
                            String beforeDate1 = BaseUtil.getBeforeDate(key, -1);//前一天的日期
                            ArrayList<String> beforeDate1gene = resultListMap.get(beforeDate1);
                            if (beforeDate1gene != null) {
                                //找到此人的前天班次
                                HashMap<String, HashMap<String, ArrayList<String>>> map = SchedulService.getClassFromPersonName(beforeDate1gene);
                                HashMap<String, ArrayList<String>> class2per = map.get("class2per");
                                HashMap<String, ArrayList<String>> per2class = map.get("per2class");
                                String beforeDate1Class = per2class.get(newPerson).get(0);
                                if (beforeDate1Class.equalsIgnoreCase(ContridictClass.get(0)) && className.equalsIgnoreCase(ContridictClass.get(1))) {
//                                    System.out.println("互斥班务影响");
                                    flag1 = true;
                                }
                            }
                        }
                        //互依班务(查看newperson前天的班次是啥,如果是互依的也返回ture  再次找人)
                        if (DataUtil.DependentClass.size() > 0) {
                            String beforeDate1 = BaseUtil.getBeforeDate(key, -1);//前一天的日期
                            ArrayList<String> beforeDate1gene = resultListMap.get(beforeDate1);
                            if (beforeDate1gene != null) {
                                //找到此人的前天班次
                                HashMap<String, HashMap<String, ArrayList<String>>> map = SchedulService.getClassFromPersonName(beforeDate1gene);
                                HashMap<String, ArrayList<String>> class2per = map.get("class2per");
                                HashMap<String, ArrayList<String>> per2class = map.get("per2class");
                                String beforeDate1Class = per2class.get(newPerson).get(0);//必须上班的人得前一天班次
                                String class2 = DependentClass.get(1);
                                //获取此日期所有班次
                                HashMap<String, Integer> ManPowerSetTreeMap1 = ManPowerSetTreeMap.get(key);
                                if (ManPowerSetTreeMap1.containsKey(class2)) {
                                    if (beforeDate1Class.equalsIgnoreCase(DependentClass.get(0)) && !className.equalsIgnoreCase(DependentClass.get(1))) {
//                                        System.out.println(DependentClass);
//                                        System.out.println(className);
//                                        System.out.println("互依班务影响");
                                        flag1 = true;
                                    }
                                }


                            }
                        }

                        if (!flag1) {
                            ints1[0] = workPersons.indexOf(person);
                        }
                        workPersonstem.remove(person);
                        if (loopNum1++ > 1000 || workPersonstem.size() < 1) {
                            //取人员一直取不到,报错
                            throw new MyResultException(AxiosResult.error(AxiosStatus.PERSON_NUM_NOT_ENOUGH));
                        }


                    } while (flag1);
                    //将基因互换
                    String person = workPersons.get(ints1[0]);
                    String gene = per2gene.get(person);
                    ArrayList<String> personList = (ArrayList<String>) getGeneSource(PERSON, gene);
                    personList.remove(person);
                    personList.add(newPerson);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < personList.size(); i++) {
                        stringBuilder.append(person2NoMap.get(personList.get(i)));
                    }
                    String newGene = gene.substring(0, 8) + stringBuilder.toString();//新的基因
                    //移除老的
                    per2gene.remove(person);
                    dateGene.remove(gene);
                    resultList.remove(gene);
                    //添加新的
                    per2gene.put(newPerson, newGene);
                    dateGene.add(newGene);
                    resultList.add(newGene);
                    //维护工作人员和休息人员
                    restPersons.add(person);
                    restPersons.remove(newPerson);
                    workPersons.remove(person);
                    workPersons.add(newPerson);
                }
            }
            resultListMap.put(key, dateGene);//以日期为key的基因list赋值
            //添加休息的基因
            int restPersonsSize = restPersons.size();
            //班次编号00
            //人员数量编号
            String personNumNo = BaseUtil.str2gene1to0001("" + restPersonsSize);
            //人员编号
            StringBuilder personNo = new StringBuilder();
            for (int i = 0; i < restPersonsSize; i++) {
                personNo.append(person2NoMap.get(restPersons.get(i)));
            }
            dateGene.add(dateNo + "00" + personNumNo + personNo);
            resultListMap.put(key, dateGene);
            resultList.add(dateNo + "00" + personNumNo + personNo);
        }

        resultMap.put("resultList", resultList);
        resultMap.put("resultListMap", resultListMap);
        return resultMap;
    }

    /**
     * 对基因进行按要求剪切
     *
     * @param aim
     * @param source
     * @return
     */
    public static String cutGene(String aim, String source) {
        switch (aim) {
            case DATE:
                //日期取前两个数字
                return source.substring(0, 2);
            case CLASS:
                //班次取第三个第四个
                return source.substring(2, 4);
            case PERSON_NUM:
                //人员数量四位  取5到8
                return source.substring(4, 8);
            case PERSON:
                //人员(四位)取剩下的全部
                return source.substring(8);
            default:
                return "";
        }
    }

    /**
     * 对基因进行按要求获取值
     */
    public static Object getGeneSource(String aim, String source) {
        String s = cutGene(aim, source);

        switch (aim) {
            case DATE:
                //如果是日期
                return no2dateMap.get(s);
            case CLASS:
                //如果是班次名
                return no2classMap.get(s);
            case PERSON_NUM:
                //如果是人员数量
                return Integer.parseInt(s);
            case PERSON:
                ArrayList<String> personList = new ArrayList<>();//人员list
                //如果是人员noList
                int PersonNum = s.length() / 4;
                for (int i = 0; i < PersonNum; i++) {
                    personList.add(no2personMap.get(s.substring(4 * i, 4 * i + 4)));
                }
                return personList;
            default:
                return "";
        }
    }


    /**
     * 传入 按天分的基因map  时间  基因list
     * <p>
     * <p>
     * <p>
     * 注意::1.individualList是这天的基因   2.resultListMap需要先移除之前这天的基因,再添加新的这天的基因
     *
     * @return
     */
    public static double alculateExpectedValue(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date,int a) {

        double K1 = 0.2;//个人最大连上天数所占权重
        double K2 = 0.2;//个人最小连上天数所占权重
        double K3 = 0.3;//个人最多休息天数所占权重
        double K4 = 0.3;//个人最少休息天数所占权重
        double K5 = 0.4;//班次平衡天数所占权重
        double K6 = 0;//个人上班班次分布连续程度所占权重


        double F1 = 0;//个人最大连上天数期望总值  总1
        double F2 = 0;//个人最小连上天数期望总值  总1
        double F3 = 0;//个人最多休息天数期望总值  总1
        double F4 = 0;//个人最少休息天数期望总值  总1
        double F5 = 0;//班次平衡天数期望总值     总1
        double F6 = 0;//个人上班班次分布连续程度期望总值   总1
        double Fx;//适应度值
        resultListMap.remove(date);
        resultListMap.put(date, individualList);

        //计算F1 到  F4
        System.out.println(resultListMap);
        F1 = alculateExpectedValueF1(individualList, resultListMap, date);
        F2 = alculateExpectedValueF2(individualList, resultListMap, date);
        F3 = alculateExpectedValueF3(individualList, resultListMap, date);
        F4 = alculateExpectedValueF4(individualList, resultListMap, date);
        F5 = alculateExpectedValueF5(individualList, resultListMap, date);
        //计算F5
        System.out.println(date+(a==1?"进化后的基因":"老的基因"));
        Fx = K1 * F1 + K2 * F2 + K3 * F3 + K4 * F4+ K5 * F5;
        System.out.println("F1:" + F1);
        System.out.println("F2:" + F2);
        System.out.println("F3:" + F3);
        System.out.println("F4:" + F4);
        System.out.println("F5:" + F5);
        System.out.println("FX:" + Fx);
        return Fx;
    }

    /**
     * @param individualList 这天的基因组
     * @param resultListMap  已经更新过的全天基因map
     * @param date           时间
     * @return
     */
    public static double alculateExpectedValueF1(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date) {
        //最大连上天数期望值
        //最大连上天数的最终的期望值F1=Y1*Y2*....*Yn
        //如果有最大连上天数要求的话所有的值存到map里    key  是person    value是真实最大连上天数
        HashMap<String, Integer> dayMap = new HashMap<>();
        HashMap<String, Double> F1YMap = new HashMap<>();
        for (int i = 0; i < ListofPersons.size(); i++) {
            //如果此人有最大连上天数的要求的话
            if (MaxContinueWorkDayMap.containsKey(ListofPersons.get(i))) {
                //求此人最大连上天数
                //此人最大连上天数为maxcontinday
                //暂时存储
                ArrayList<Integer> tem = new ArrayList<>();
                Integer maxcontinday = 0;
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
                int daynum = 1;
                //对日期遍历
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                    String key = next.getKey();//date
                    HashMap<String, Integer> value = next.getValue();//不用
                    //获取这个人,这个日期,的班次
                    ArrayList<String> thisDateGenes = resultListMap.get(key);
//                    System.out.println("这天的基因");
//                    System.out.println(thisDateGenes);
                    HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = SchedulService.getClassFromPersonName(thisDateGenes);
                    HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                    String thisPersonClass = per2class.get(ListofPersons.get(i)).get(0);
//                    System.out.println(thisPersonClass);
                    if (!"休息".equals(thisPersonClass)) {
                        maxcontinday++;
                    } else {
                        //如果不是第一天
                        if (daynum != 1) {
                            tem.add(maxcontinday);
                        }
                        maxcontinday = 0;
                    }
                    //如果遍历到最后一个也赋值
                    if (daynum==ManPowerSet.size()) {
                        tem.add(maxcontinday);
                    }
                    daynum++;
                }
                maxcontinday = Collections.max(tem);
                dayMap.put(ListofPersons.get(i), maxcontinday);
//                System.out.println(dayMap);
                //计算Y
                double y = 1;
                Integer needDay = MaxContinueWorkDayMap.get(ListofPersons.get(i));
                if (maxcontinday > needDay) {
//                    y = 1 / (Math.exp(maxcontinday - needDay));
                    y = 1 / (Math.pow(1.1,maxcontinday - needDay));
//                    System.out.println("y" + Math.exp(maxcontinday - needDay));
//                    System.out.println(y);
                }
                F1YMap.put(ListofPersons.get(i), y);
            }
        }
        //计算F1
        double F1 = 1;
        Iterator<Map.Entry<String, Double>> iterator = F1YMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            F1 = next.getValue() * F1;
        }


        return F1;
    }

    /**
     * @param individualList 这天的基因组
     * @param resultListMap  已经更新过的全天基因map
     * @param date           时间
     * @return
     */
    public static double alculateExpectedValueF2(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date) {
        //最小连上天数期望值
        //最小连上天数的最终的期望值F1=Y1*Y2*....*Yn
        //如果有最小连上天数要求的话所有的值存到map里    key  是person    value是真实最小连上天数
        HashMap<String, Integer> dayMap = new HashMap<>();
        HashMap<String, Double> F1YMap = new HashMap<>();
        for (int i = 0; i < ListofPersons.size(); i++) {
            //如果此人有最大连上天数的要求的话
            if (MinContinueWorkDayMap.containsKey(ListofPersons.get(i))) {
                //求此人最小连上天数
                //此人最小连上天数为maxcontinday
                //暂时存储
                ArrayList<Integer> tem = new ArrayList<>();
                Integer mincontinday = 0;
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
                int daynum = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                    String key = next.getKey();//date
                    HashMap<String, Integer> value = next.getValue();//不用
                    //获取这个人,这个日期,的班次
                    ArrayList<String> thisDateGenes = resultListMap.get(key);
                    HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = SchedulService.getClassFromPersonName(thisDateGenes);
                    HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                    String thisPersonClass = per2class.get(ListofPersons.get(i)).get(0);
                    if (!"休息".equals(thisPersonClass)) {
                        mincontinday++;
                        //
                    } else {
                        //如果不是第一天,并且前一天不是休息
                        if (daynum != 1&&mincontinday!=0) {
                            tem.add(mincontinday);
                        }
                        mincontinday = 0;
                    }
                    //如果遍历到最后一个也赋值
                    if (daynum==ManPowerSet.size()&&mincontinday!=0) {
                        tem.add(mincontinday);
                    }
                    daynum++;
                }
                mincontinday = Collections.min(tem);
                dayMap.put(ListofPersons.get(i), mincontinday);
//                System.out.println(dayMap);
                //计算Y
                double y = 1;
                Integer needDay = MinContinueWorkDayMap.get(ListofPersons.get(i));
                if (mincontinday < needDay) {
//                    y = 1 / (Math.exp(needDay - mincontinday));
                    y = 1 / (Math.pow(1.1,needDay - mincontinday));
                }
                F1YMap.put(ListofPersons.get(i), y);
            }
        }
        //计算F2
        double F2 = 1;
        Iterator<Map.Entry<String, Double>> iterator = F1YMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            F2 = next.getValue() * F2;
        }


        return F2;
    }

    /**
     * @param individualList 这天的基因组
     * @param resultListMap  已经更新过的全天基因map
     * @param date           时间
     * @return
     */
    public static double alculateExpectedValueF3(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date) {
        //最大休息天数期望值
        //最大休息天数的最终的期望值F1=Y1*Y2*....*Yn
        //如果有最大休息天数要求的话所有的值存到map里    key  是person    value是真实最大连上天数
        HashMap<String, Integer> dayMap = new HashMap<>();
        HashMap<String, Double> F1YMap = new HashMap<>();
        for (int i = 0; i < ListofPersons.size(); i++) {
            //如果此人有最大休息天数的要求的话
            if (MaxHolidayMap.containsKey(ListofPersons.get(i))) {
                //求此人最大休息天数
                //此人最大休息天数为maxrestday
                //暂时存储
                ArrayList<Integer> tem = new ArrayList<>();
                Integer maxrestday = 0;//一共休息多少天
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
                int daynum = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                    String key = next.getKey();//date
                    HashMap<String, Integer> value = next.getValue();//不用
                    //获取这个人,这个日期,的班次
                    ArrayList<String> thisDateGenes = resultListMap.get(key);
                    HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = SchedulService.getClassFromPersonName(thisDateGenes);
                    HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                    String thisPersonClass = per2class.get(ListofPersons.get(i)).get(0);
                    if ("休息".equals(thisPersonClass)) {
                        maxrestday++;
                    }
                    daynum++;
                }
                dayMap.put(ListofPersons.get(i), maxrestday);
//                System.out.println(dayMap);
                //计算Y
                double y = 1;
                Integer needDay = MaxHolidayMap.get(ListofPersons.get(i));
                if (maxrestday > needDay) {
//                    y = 1 / (Math.exp(maxrestday - needDay));
                    y = 1 / (Math.pow(1.1,maxrestday - needDay));
                }
                F1YMap.put(ListofPersons.get(i), y);
            }
        }
        //计算F1
        double F3 = 1;
        Iterator<Map.Entry<String, Double>> iterator = F1YMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            F3 = next.getValue() * F3;
        }
        return F3;
    }

    /**
     * @param individualList 这天的基因组
     * @param resultListMap  已经更新过的全天基因map
     * @param date           时间
     * @return
     */
    public static double alculateExpectedValueF4(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date) {
        //最少休息天数期望值
        //最少休息天数的最终的期望值F1=Y1*Y2*....*Yn
        //如果有最少天数要求的话所有的值存到map里    key  是person    value是真实休息天数
        HashMap<String, Integer> dayMap = new HashMap<>();
        HashMap<String, Double> F1YMap = new HashMap<>();
        for (int i = 0; i < ListofPersons.size(); i++) {
            //如果此人有最大连上天数的要求的话
            if (MinHolidayMap.containsKey(ListofPersons.get(i))) {
                //求此人最大连上天数
                //此人最大连上天数为maxcontinday
                //暂时存储
                ArrayList<Integer> tem = new ArrayList<>();
                Integer maxcontinday = 0;//总的休息天数
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
                int daynum = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                    String key = next.getKey();//date
                    HashMap<String, Integer> value = next.getValue();//不用
                    //获取这个人,这个日期,的班次
                    ArrayList<String> thisDateGenes = resultListMap.get(key);
                    HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = SchedulService.getClassFromPersonName(thisDateGenes);
                    HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                    String thisPersonClass = per2class.get(ListofPersons.get(i)).get(0);
                    if ("休息".equals(thisPersonClass)) {
                        maxcontinday++;
                    }
                    daynum++;
                }
                dayMap.put(ListofPersons.get(i), maxcontinday);
//                System.out.println(dayMap);
                //计算Y
                double y = 1;
                Integer needDay = MinHolidayMap.get(ListofPersons.get(i));
                if (maxcontinday < needDay) {
//                    y = 1 / (Math.exp(needDay - maxcontinday));
                    y = 1 / (Math.pow(1.1,needDay - maxcontinday));
                }
                F1YMap.put(ListofPersons.get(i), y);
            }
        }
        //计算F1
        double F4 = 1;
        Iterator<Map.Entry<String, Double>> iterator = F1YMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            F4 = next.getValue() * F4;
        }
        return F4;
    }
    /**
     * @param individualList 这天的基因组
     * @param resultListMap  已经更新过的全天基因map
     * @param date           时间
     * @return
     */
    public static double alculateExpectedValueF5(ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap, String date) {
        //最少休息天数期望值
        //最少休息天数的最终的期望值F1=Y1*Y2*....*Yn
        //如果有最少天数要求的话所有的值存到map里    key  是person    value是真实休息天数
        HashMap<String, Integer> dayMap = new HashMap<>();
        HashMap<String, Double> F1YMap = new HashMap<>();
        ArrayList<Double> doubles = new ArrayList<>();
        for (int i = 0; i < ListofPersons.size(); i++) {
            //如果此人有最大连上天数的要求的话
            if (MinHolidayMap.containsKey(ListofPersons.get(i))) {
                //求此人最大连上天数
                //此人最大连上天数为maxcontinday
                //暂时存储
                ArrayList<Integer> tem = new ArrayList<>();
                Integer maxcontinday = 0;//总的休息天数
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
                int daynum = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                    String key = next.getKey();//date
                    HashMap<String, Integer> value = next.getValue();//不用
                    //获取这个人,这个日期,的班次
                    ArrayList<String> thisDateGenes = resultListMap.get(key);
                    HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = SchedulService.getClassFromPersonName(thisDateGenes);
                    HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                    String thisPersonClass = per2class.get(ListofPersons.get(i)).get(0);
                    if ("休息".equals(thisPersonClass)) {
                        maxcontinday++;
                    }
                    daynum++;
                }
                dayMap.put(ListofPersons.get(i), maxcontinday);

                //计算Y
                double y = 1;
                Integer needDay = MinHolidayMap.get(ListofPersons.get(i));
                if (maxcontinday < needDay) {
//                    y = 1 / (Math.exp(needDay - maxcontinday));
                    y = 1 / (Math.pow(1.1,needDay - maxcontinday));
                }

                F1YMap.put(ListofPersons.get(i), y);
                doubles.add((double)maxcontinday);
            }
        }
        System.out.println(dayMap);

        //计算F5
        double F5 = 1;
        Double[] yy=new Double[doubles.size()];
        Double[] doubles1 = doubles.toArray(yy);
        double fangcha = StandardDiviation1(doubles1);
        F5=1/Math.pow(1.1,fangcha);
        System.out.println("计算得出F5："+F5);
//        Iterator<Map.Entry<String, Double>> iterator = F1YMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Double> next = iterator.next();
//            F5 = next.getValue() * F5;
//        }
        return F5;
    }


    /**
     * 计算一组数据的标准差
     * @param
     * @param x
     * @return
     */
    public static double StandardDiviation1(Double[] x) {
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

}
