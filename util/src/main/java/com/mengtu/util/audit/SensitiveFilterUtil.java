package com.mengtu.util.audit;

import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * 敏感词过滤工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/24 14:02
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Component
public class SensitiveFilterUtil {


    /**
     * 敏感词集合
     */
    public static HashMap SENSITIVE_WORD_MAP;

    /**
     * 初始化敏感词库,构建 DFA 模型
     */
    public static void initContext(ByteArrayOutputStream data) {
        HashSet<String> set = new HashSet<>();
        try {
            Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(data.toByteArray()));
            Sheet sheet = workbook.getSheetAt(0);
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    if (row.getCell(3) != null) {
                        Cell cell = row.getCell(3);
                        cell.setCellType(CellType.STRING);
                        set.add(cell.getStringCellValue());
                    }
                }
            }
            initSensitiveWordMap(set);
        } catch (Exception e) {
            throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "解析敏感词文件报错");
        }
    }

    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        SENSITIVE_WORD_MAP = new HashMap<String, String>(sensitiveWordSet.size());
        Map<Object, Object> temp;
        Map<Object, Object> newWorMap;
        for (String key : sensitiveWordSet) {
            temp = SENSITIVE_WORD_MAP;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = temp.get(keyChar);
                if (wordMap != null) {
                    temp = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");
                    temp.put(keyChar, newWorMap);
                    temp = newWorMap;
                }
                if (i == key.length() - 1) {
                    temp.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt
     * @return
     */
    public static boolean contains(String txt) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 检查文字中是否包含敏感字符
     *
     * @param txt
     * @param beginIndex
     * @return 如果存在, 则返回敏感词字符的长度, 不存在返回0
     */
    private static int checkSensitiveWord(String txt, int beginIndex) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = SENSITIVE_WORD_MAP;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt
     * @return
     */
    public static List getSensitiveWord(String txt) {
        List sensitiveWordList = new ArrayList();
        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    /**
     * 实例初始化
     *
     * @param data
     */
    public void getInstance(ByteArrayOutputStream data) {
        initContext(data);
    }
}
