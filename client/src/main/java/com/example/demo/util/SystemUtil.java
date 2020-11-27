package com.example.demo.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class SystemUtil {

   public static String getLocalIPv4Ip(){
       InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
       InetAddress firstNonLoopbackAddress = inetUtils.findFirstNonLoopbackAddress();
       return firstNonLoopbackAddress.getHostAddress();
   }

   public  static  String getServiceAddress(String serverAddr){
       String localIPv4Ip = getLocalIPv4Ip();
       if (StringUtils.isNotBlank(serverAddr)&&localIPv4Ip.startsWith("127")){
           return serverAddr;
       }
       return localIPv4Ip;

   }

    public static BigDecimal getCpuUseRateByTOP() {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return null;
        }

        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("top -b -n 1");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String readLine;
            List<String> readList = new ArrayList<>();
            while ((readLine = reader.readLine()) != null) {
                readList.add(readLine);
            }
            if (readList == null || readList.size() < 3) {
                return null;
            }
            String cpuInfo = readList.get(2);
            String[] items = cpuInfo.trim().replaceAll("\\s{1,}", "").split(",");
            BigDecimal useRate = null;
            for (String item:items) {
                if (item.contains("id")) {
                    log.info("getCpuUseRateByTOP item:{}", item);
                    String rate = item.replace("id", "");
                    useRate = BigDecimal.valueOf(1).subtract(new BigDecimal(rate).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                }
            }
            return useRate;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static BigDecimal getCpuUsrRateByVMSTAT() {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return null;
        }
        Process process = null;
        BufferedReader reader = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            List<String> readList = new ArrayList<>();
            process = runtime.exec("vmstat 1 2");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                readList.add(readLine);
            }
            // cpu-id 索引
            int idIndex = 0;
            // 查询次数
            int number = 0;
            // cpu未使用总计
            int cpuIdSum = 0;
            int cpuIdLine = 0;
            for (String line:readList) {
                if (line.contains("cpu")) {
                    // 该行说明下列数据是磁盘,内存,io,cpu的信息 因此不做分析
                    continue;
                }
                String[] items = line.trim().replaceAll("\\s{1,}", ",").split(",");
                if (line.contains("id")) {
                    // 该行说明下列数据是的标签, 此次仅取cpu未使用值, 因此找到该坐标
                    List<String> lineList = Arrays.asList(items);
                    idIndex = lineList.indexOf("id");
                    continue;
                }
                if (idIndex == 0) {
                    System.out.println("---异常---");
                    continue;
                }
                if (cpuIdLine == 0) {
                    // 去除第一条
                    cpuIdLine = 1;
                    continue;
                }
                log.info("getCpuUsrRateByVMSTAT line:{}", line);
                // cpu未使用值
                String cpuId = items[idIndex];
                if (StringUtils.isNumeric(cpuId)) {
                    number++;
                    cpuIdSum += Integer.parseInt(cpuId);
                } else {
                    System.out.println("---异常2---");
                }
            }
            BigDecimal useRate = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(cpuIdSum).divide(BigDecimal.valueOf(number).multiply(new BigDecimal("100")), 4, BigDecimal.ROUND_HALF_UP));
            return useRate;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
