package paiban;

import com.xnxy.CourseSchedulingSystem.Bean.po.ClassTask;
import com.xnxy.CourseSchedulingSystem.Bean.po.ClassroomLocation;
import com.xnxy.CourseSchedulingSystem.Bean.po.CoursePlan;
import com.xnxy.CourseSchedulingSystem.Bean.vo.ConstantInfo;
import com.xnxy.CourseSchedulingSystem.Util.ClassSchedulUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import paiban.result.AxiosResult;
import paiban.result.MyResultException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SchedulService {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void classScheduling(String para) {

        try {
            //
            //前期作业:
            //1.参数合理性校验   参数初始化
            DataUtil.checkData(para);
            //第一步先随机获取基因,生成基因List
            HashMap<String, Object> resultMap = DataUtil.createGene();
            ArrayList<String> resultList = (ArrayList<String>) resultMap.get("resultList");
            //第二步将基因List根据天数分类放入map
            HashMap<String, ArrayList<String>> resultListMap = (HashMap<String, ArrayList<String>>) resultMap.get("resultListMap");

            //第三步进行遗传进化
            System.out.println(BaseUtil.transObj2Json(resultListMap));

            resultListMap = geneticEvolution(resultListMap);
            //第四步变异

            //第五步解码返回
            AxiosResult<Object> decoding = decoding(resultList, resultListMap);
            System.out.println(BaseUtil.transObj2Json(decoding));
            //操作全部完成

        } catch (MyResultException e) {
            e.printStackTrace();
            System.out.println(e.getAxiosResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 遗传进化
     *
     * @param resultListMap
     * @return
     */
    private static HashMap<String, ArrayList<String>> geneticEvolution(HashMap<String, ArrayList<String>> resultListMap) throws ParseException {
        int generation = DataUtil.DEVELOP_NUM;//进化代数设为100
        List<String> resultGeneList;
        for (int i = 0; i < generation; ++i) {
            //第一步完成交叉操作,产生新一代的父本
            resultListMap = hybridization(resultListMap);
        }
        return resultListMap;

    }

    /**
     * 整个种群的交叉操作,将每天的两个人进行交叉
     *
     * @param resultListMap
     * @return
     */
    private static HashMap<String, ArrayList<String>> hybridization(HashMap<String, ArrayList<String>> resultListMap) throws ParseException {
        System.out.println("hybridization");
        Iterator<String> iterator = DataUtil.ManPowerSetTreeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String date = iterator.next();
            ArrayList<String> individualList = resultListMap.get(date);
            ArrayList<String> oldIndividualList = (ArrayList<String>) BaseUtil.transJson2Obj(BaseUtil.transObj2Json(individualList), ArrayList.class);
            individualList = selectiveGene(date, individualList, resultListMap);//进行基因的交叉操作生成新个体//返回这天的杂交后基因
//            if (date.equalsIgnoreCase("2021-05-08")){
//                System.out.println("2021-05-08号杂交后的基因");
//                System.out.println(BaseUtil.transObj2Json(individualList));
//            }
            //对父代的适应度值和新生成的子代适应值进行对比，选择适应度值高的一个进入下一代的遗传
            System.out.println("计算前两个基因");
            System.out.println(individualList);
            System.out.println(oldIndividualList);




            if (DataUtil.alculateExpectedValue(individualList, resultListMap, date,1) >= DataUtil.alculateExpectedValue(oldIndividualList, resultListMap, date,0)) {
                resultListMap.put(date, individualList);
            } else {
                resultListMap.put(date, oldIndividualList);
            }
        }
        return resultListMap;
    }

    //传入某天的基因,将这天的基因杂交
    //个体间的随机选择两条基因准备进行杂交并生成一个新个体(休息工作,工作工作)
    private static ArrayList<String> selectiveGene(String date, ArrayList<String> individualList, HashMap<String, ArrayList<String>> resultListMap) throws ParseException {
        ArrayList<String> reList = new ArrayList<>();
        boolean flag;
        do {
            flag = false;
            //随机选一个人
            ArrayList<String> listofPersons = DataUtil.ListofPersons;
            int i = BaseUtil.randomCommon(0, listofPersons.size(), 1)[0];
            String thisPerson = listofPersons.get(i);//人员name
            //获取可与此人交换基因的人员list
            ArrayList<String> personListOfSwapAble = getPerListOfCanSwap(date, individualList, thisPerson, resultListMap);

            if (personListOfSwapAble != null&&personListOfSwapAble.size()>0) {
                //
                String otherPerson = personListOfSwapAble.get(BaseUtil.randomCommon(0, personListOfSwapAble.size(), 1)[0]);
                //两人所在基因杂交
                System.out.println("杂交前的基因");
                System.out.println(individualList);
                System.out.println("与此人进行杂交的人");
                System.out.println(otherPerson);
                ArrayList<String> newIndividualList = getGeneHybrided(thisPerson, otherPerson, individualList);
                //维护resultListMap???

                reList = newIndividualList;
                flag = true;
            }
            if(flag){
                System.out.println("此人为"+thisPerson);
                System.out.println("可与此人交换基因的人");
                System.out.println(personListOfSwapAble);
//                System.out.println("杂交前的基因");
//                System.out.println(individualList);
                System.out.println("杂交后的基因");
                System.out.println(reList);
            }
        } while (!flag);
        return reList;
    }


    /**
     * 传入不同班次的person1   和 person2     和当天基因  进行杂交
     * 返回值:  杂交后的基因list
     */
    private static ArrayList<String> getGeneHybrided(String person1, String person2, ArrayList<String> individualList) {
        ArrayList<String> returnList = new ArrayList<>();
        HashMap<String, HashMap<String, String>> geneFromPersonOrClass = getGeneFromPersonOrClass(individualList);
        HashMap<String, String> class2gene = geneFromPersonOrClass.get("class2gene");
        HashMap<String, String> per2gene = geneFromPersonOrClass.get("per2gene");
        //两人所在基因杂交
        String oldGen1 = per2gene.get(person1);
        String oldGen2 = per2gene.get(person2);
        if (oldGen1.equalsIgnoreCase(oldGen2)){
            System.out.println("两个人得班次一样了   错了!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new RuntimeException("11");
        }
        //生成两条新的基因
        ArrayList<String> oldPersonList1 = (ArrayList<String>) DataUtil.getGeneSource(DataUtil.PERSON, oldGen1);
        ArrayList<String> oldPersonList2 = (ArrayList<String>) DataUtil.getGeneSource(DataUtil.PERSON, oldGen2);

        oldPersonList1.remove(person1);
        oldPersonList1.add(person2);
        oldPersonList2.remove(person2);
        oldPersonList2.add(person1);
        String newGene1 = oldGen1.substring(0, 8) + getPerGeneFromPerList(oldPersonList1);
        String newGene2 = oldGen2.substring(0, 8) + getPerGeneFromPerList(oldPersonList2);
        individualList.remove(oldGen1);
        individualList.remove(oldGen2);
        individualList.add(newGene1);
        if (!individualList.contains(newGene2)){
            individualList.add(newGene2);
        }
        returnList = individualList;
        return returnList;
    }


    /**
     * 传入这天的基因list  和人员   返回可以与其交叉的人员List
     * 如果选到必须休息的人  返回null
     * <p>
     * 已经考虑到了 禁止班次,必须工作,必须休息...  等规则
     *
     *
     * 不能选择自己
     *
     *
     */
    private static ArrayList<String> getPerListOfCanSwap(String date, ArrayList<String> individualList, String person, HashMap<String, ArrayList<String>> resultListMap) throws ParseException {
        ArrayList<String> returnList = new ArrayList<>();
        //这天的休息人员
        ArrayList<String> thisDayRestPerson = new ArrayList<>();
        //这天的工作人员
        ArrayList<String> thisDayWorkPerson = new ArrayList<>();
        //这天必须休息人员列表
        ArrayList<String> thisDayMustRestPerson = DataUtil.HolidayMapDate2Person.get(date);
        //这天必须工作的人员列表
        ArrayList<String> thisDayMustWorkPerson = DataUtil.WorkMapDate2Person.get(date);
        //person的禁止班次
        ArrayList<String> thisPersonForbiddenClass = DataUtil.ForbiddenClassMap.get(person);
        //person的前一天班次(没有为null)
        String before1Class = null;
        String beforeDate = BaseUtil.getBeforeDate(date, -1);
        ArrayList<String> beforeDateGene = resultListMap.get(beforeDate);
        if (beforeDateGene != null) {
            HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonNamebefore = getClassFromPersonName(beforeDateGene);
            HashMap<String, ArrayList<String>> per2classbefore = classFromPersonNamebefore.get("per2class");
            before1Class = per2classbefore.get(person).get(0);
        }
        //per2class的map
        HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = getClassFromPersonName(individualList);
        HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
        for (int i = 0; i < individualList.size(); i++) {
            if (DataUtil.getGeneSource(DataUtil.CLASS, individualList.get(i)) == "休息") {
                thisDayRestPerson = (ArrayList<String>) DataUtil.getGeneSource(DataUtil.PERSON, individualList.get(i));
            }
        }
        ArrayList<String> listofPersons = DataUtil.ListofPersons;
        for (int i = 0; i < listofPersons.size(); i++) {
            if (!thisDayRestPerson.contains(listofPersons.get(i))) {
                thisDayWorkPerson.add(listofPersons.get(i));
            }
        }
        //1. person是休息人员
        if (thisDayRestPerson.contains(person)) {
            //1.1 person是必须休息人员

            if (thisDayMustRestPerson!=null&&thisDayMustRestPerson.contains(person)) {
                return null;
            }

            //1.2 person不是必须休息人员
            //和工作的人更换(1.工作的人不能是必须工作的人  2.person不能禁止这个班次  3.工作的人员的班次不能与person的前一天班次存在互斥互依冲突)
            for (int i = 0; i < thisDayWorkPerson.size(); i++) {
                if (thisDayMustWorkPerson==null||!thisDayMustWorkPerson.contains(thisDayWorkPerson.get(i))) {
                    if (thisPersonForbiddenClass != null && thisPersonForbiddenClass.size() > 0) {
                        //此人存在禁止班次,则工作的人员不能是这些班次中的人
                        String workPersonTemp = thisDayWorkPerson.get(i);
                        String classs = per2class.get(workPersonTemp).get(0);
                        if (!thisPersonForbiddenClass.contains(classs)) {
                            //看互斥互依关系

                            //互斥班务(查看此人前天的班次是啥,如果是互斥的也返回ture  再次找人)
                            ArrayList<String> contridictClass = DataUtil.ContridictClass;
                            if (DataUtil.ContridictClass.size() > 0) {
                                if (before1Class != null) {
                                    if (before1Class.equalsIgnoreCase(contridictClass.get(0)) && classs.equalsIgnoreCase(contridictClass.get(1))) {
                                        continue;
                                    }
                                }
                            }
                            ArrayList<String> dependentClass = DataUtil.DependentClass;
                            //互依班务(查看此人前天的班次是啥,如果是互依的也返回ture  再次找人)
                            if (DataUtil.DependentClass.size() > 0) {
                                if (before1Class != null) {
                                    //如果这天有这个班次的话
                                    //如果这天有这个班次的话
                                    String class2 = dependentClass.get(1);
                                    HashMap<String, Integer> stringIntegerHashMap = DataUtil.ManPowerSetTreeMap.get(date);
                                    if (stringIntegerHashMap.containsKey(class2)) {


                                        if (before1Class.equalsIgnoreCase(dependentClass.get(0)) && !classs.equalsIgnoreCase(dependentClass.get(1))) {
                                            continue;
                                        }
                                    }
                                }
                            }
                            returnList.add(thisDayWorkPerson.get(i));
                        }
                    } else {
                        //此人不存在禁止班次
                        String workPersonTemp = thisDayWorkPerson.get(i);
                        String classs = per2class.get(workPersonTemp).get(0);
                        //互斥班务(查看此人前天的班次是啥,如果是互斥的也返回ture  再次找人)
                        ArrayList<String> contridictClass = DataUtil.ContridictClass;
                        if (DataUtil.ContridictClass.size() > 0) {
                            if (before1Class != null) {
                                if (before1Class.equalsIgnoreCase(contridictClass.get(0)) && classs.equalsIgnoreCase(contridictClass.get(1))) {
                                    continue;
                                }
                            }
                        }
                        ArrayList<String> dependentClass = DataUtil.DependentClass;
                        //互依班务(查看此人前天的班次是啥,如果是互依的也返回ture  再次找人)
                        if (DataUtil.DependentClass.size() > 0) {
                            if (before1Class != null) {
                                //如果这天有这个班次的话
                                String class2 = dependentClass.get(1);
                                HashMap<String, Integer> stringIntegerHashMap = DataUtil.ManPowerSetTreeMap.get(date);
                                if (stringIntegerHashMap.containsKey(class2)) {
                                    if (before1Class.equalsIgnoreCase(dependentClass.get(0)) && !classs.equalsIgnoreCase(dependentClass.get(1))) {
                                        continue;
                                    }
                                }

                            }
                        }
                        returnList.add(thisDayWorkPerson.get(i));
                    }
                }
            }
            return returnList;

        } else {
            //2. person是工作人员   直接返回其他工作人员(0.其他人员的班次不能是此人的班次  1.其他人员的禁止班次不能是person的当前班次  2.每个人都满足互斥互依  )
            String thisPerClass = per2class.get(person).get(0);
            thisDayWorkPerson.remove(person);
            for (int i = 0; i < thisDayWorkPerson.size(); i++) {
                String otherPerson = thisDayWorkPerson.get(i);
                String otherPersonClass = per2class.get(otherPerson).get(0);
                ArrayList<String> tempForbiddenClass = DataUtil.ForbiddenClassMap.get(thisDayWorkPerson.get(i));
                if (tempForbiddenClass == null) {
                    tempForbiddenClass = new ArrayList<>();
                }
                if (thisPerClass.equalsIgnoreCase(otherPersonClass)){
                    continue;
                }
                if (!tempForbiddenClass.contains(thisPerClass)) {
                    //每个人都满足互斥互依
                    //person的当前班次thisPerClass
                    //此人的当前班次
                    String otherPerClass = per2class.get(thisDayWorkPerson.get(i)).get(0);

                    if (beforeDateGene == null) {
                        returnList.add(otherPerson);
                    } else {
                        //此人的前一天班次
                        HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonNamebefore = getClassFromPersonName(beforeDateGene);
                        HashMap<String, ArrayList<String>> per2classbefore = classFromPersonNamebefore.get("per2class");
                        String otherbefore1Class = per2classbefore.get(otherPerson).get(0);

                        //1.person满足
                        //互斥班务(查看此人前天的班次是啥,如果是互斥的也返回ture  再次找人)
                        ArrayList<String> contridictClass = DataUtil.ContridictClass;
                        if (DataUtil.ContridictClass.size() > 0) {
                            if (before1Class != null) {
                                if (before1Class.equalsIgnoreCase(contridictClass.get(0)) && otherPerClass.equalsIgnoreCase(contridictClass.get(1))) {
                                    continue;
                                }
                            }
                        }
                        ArrayList<String> dependentClass = DataUtil.DependentClass;
                        //互依班务(查看此人前天的班次是啥,如果是互依的也返回ture  再次找人)
                        if (DataUtil.DependentClass.size() > 0) {
                            if (before1Class != null) {
                                //如果这天有这个班次的话
                                String class2 = dependentClass.get(1);
                                HashMap<String, Integer> stringIntegerHashMap = DataUtil.ManPowerSetTreeMap.get(date);
                                if (stringIntegerHashMap.containsKey(class2)) {
                                    if (before1Class.equalsIgnoreCase(dependentClass.get(0)) && !otherPerClass.equalsIgnoreCase(dependentClass.get(1))) {
                                        continue;
                                    }
                                }

                            }
                        }

                        //2.此人满足

                        //互斥班务(查看此人前天的班次是啥,如果是互斥的也返回ture  再次找人)
                        if (DataUtil.ContridictClass.size() > 0) {
                            if (before1Class != null) {
                                if (otherbefore1Class.equalsIgnoreCase(contridictClass.get(0)) && thisPerClass.equalsIgnoreCase(contridictClass.get(1))) {
                                    continue;
                                }
                            }
                        }
                        //互依班务(查看此人前天的班次是啥,如果是互依的也返回ture  再次找人)
                        if (DataUtil.DependentClass.size() > 0) {
                            if (before1Class != null) {
                                //如果这天有这个班次的话
                                String class2 = dependentClass.get(1);
                                HashMap<String, Integer> stringIntegerHashMap = DataUtil.ManPowerSetTreeMap.get(date);
                                if (stringIntegerHashMap.containsKey(class2)) {
                                    if (otherbefore1Class.equalsIgnoreCase(dependentClass.get(0)) && !thisPerClass.equalsIgnoreCase(dependentClass.get(1))) {
                                        continue;
                                    }
                                }

                            }
                        }
                        returnList.add(otherPerson);
                    }

                }
            }
            if (returnList.contains(person)){
                returnList.remove(person);
            }
            return returnList;
        }
    }


    /**
     * 传入人员list   得到人员基因片段  比如:   人员1,人员2   转换成   0021,0034
     */
    public static String getPerGeneFromPerList(ArrayList<String> perList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < perList.size(); i++) {
            String s = perList.get(i);
            stringBuilder.append(BaseUtil.str2gene1to0001(DataUtil.person2NoMap.get(s)));
        }
        return stringBuilder.toString();
    }

    /**
     * 传入这天的基因获取   班次基因对应map:class2gene   人员基因对应map:per2gene
     *
     * @param individualList
     * @return
     */
    public static HashMap<String, HashMap<String, String>> getGeneFromPersonOrClass(ArrayList<String> individualList) {
        HashMap<String, HashMap<String, String>> returnMap = new HashMap<>();
        HashMap<String, String> class2gene = new HashMap<>();
        HashMap<String, String> per2gene = new HashMap<>();
        for (int i = 0; i < individualList.size(); i++) {
            String gene = individualList.get(i);
            //获取class
            String classSource = (String) DataUtil.getGeneSource(DataUtil.CLASS, gene);
            class2gene.put(classSource, gene);
            //获取人员list
            ArrayList<String> personSource = (ArrayList<String>) DataUtil.getGeneSource(DataUtil.PERSON, gene);
            //per2classHashMap赋值
            for (int i1 = 0; i1 < personSource.size(); i1++) {
                String person = personSource.get(i1);
                per2gene.put(person, gene);
            }
        }
        returnMap.put("class2gene", class2gene);
        returnMap.put("per2gene", per2gene);
        return returnMap;
    }


    /**
     * 传入这天的基因获取   班次人员对应map:class2per   人员班次对应map:per2class
     *
     * @param individualList
     * @return
     */
    public static HashMap<String, HashMap<String, ArrayList<String>>> getClassFromPersonName(ArrayList<String> individualList) {
        HashMap<String, HashMap<String, ArrayList<String>>> returnMap = new HashMap<>();
        HashMap<String, ArrayList<String>> class2perHashMap = new HashMap<>();
        HashMap<String, ArrayList<String>> per2classHashMap = new HashMap<>();
        for (int i = 0; i < individualList.size(); i++) {
            String gene = individualList.get(i);
            //获取class
            String classSource = (String) DataUtil.getGeneSource(DataUtil.CLASS, gene);
//            System.out.println(gene);
            //获取人员list
            ArrayList<String> personSource = (ArrayList<String>) DataUtil.getGeneSource(DataUtil.PERSON, gene);
            //class2perHashMap赋值
            class2perHashMap.put(classSource, personSource);
            //per2classHashMap赋值
            for (int i1 = 0; i1 < personSource.size(); i1++) {
                String person = personSource.get(i1);
                ArrayList<String> strings = new ArrayList<>();
                strings.add(classSource);
                per2classHashMap.put(person, strings);
            }
        }
        returnMap.put("class2per", class2perHashMap);
        returnMap.put("per2class", per2classHashMap);
        return returnMap;
    }


    //开始解码，将基因字符串解码为对象
    private static AxiosResult<Object> decoding(List<String> resultList, HashMap<String, ArrayList<String>> resultListMap) {
        HashMap<String, HashMap<String, Map<String, String>>> result = new HashMap<>();
        HashMap<String, Map<String, String>> schedule = new HashMap<>();
        //给schedule赋值
        //全部人员
        ArrayList<String> listofPersons = DataUtil.ListofPersons;
        for (int i = 0; i < listofPersons.size(); i++) {
            //key值listofPersons.get(i)
            //value
            TreeMap<String, String> stringObjectTreeMap = new TreeMap<>(new Comparator<String>() {
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
            //对日期遍历
            Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = DataUtil.ManPowerSet.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, HashMap<String, Integer>> next = iterator.next();
                String key = next.getKey();//date
                HashMap<String, Integer> value = next.getValue();//不用
                //获取这个人,这个日期的班次
                ArrayList<String> thisDateGenes = resultListMap.get(key);
                HashMap<String, HashMap<String, ArrayList<String>>> classFromPersonName = getClassFromPersonName(thisDateGenes);
                HashMap<String, ArrayList<String>> per2class = classFromPersonName.get("per2class");
                String thisPersonClass = per2class.get(listofPersons.get(i)).get(0);
                stringObjectTreeMap.put(key, thisPersonClass);

            }
            schedule.put(listofPersons.get(i), stringObjectTreeMap);
        }
        result.put("schedule", schedule);
        System.out.println(DataUtil.person2NoMap);
        return AxiosResult.success(result);
    }
}
