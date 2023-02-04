package com.mengtu.util.storage;

import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

/**
 * 文件工具
 *
 * @author
 * @version 1.1 @ 2022/5/16 11:23
 */
public class FileUtil {

    /**
     * 本地根路径
     */
    private static final String ROOT_LOCATION = "/mnt/Data-8T/LinkPoint_Data/backend/";

    /**
     * MultipartFile -> File
     *
     * @param multiFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multiFile) {
        String fileName = multiFile.getOriginalFilename();
        AssertUtil.assertNotNull(fileName, "文件名非法");
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "文件处理异常");
        }
    }

    /**
     * File -> String
     *
     * @param file
     * @return
     */
    public static String fileToString(final File file) {
        try {
            if (file.exists()) {
                byte[] data = new byte[(int) file.length()];
                boolean result;
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    int len = inputStream.read(data);
                    result = len == data.length;
                }
                if (result) {
                    return new String(data);
                }
            }
            return null;
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "文件处理异常");
        }
    }

    /**
     * String -> File
     *
     * @param data
     * @return
     */
    public static File stringToFile(String data) {
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        try {
            File distFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".txt");
            distFile.deleteOnExit();

            bufferedReader = new BufferedReader(new StringReader(data));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            String len;
            while ((len = bufferedReader.readLine()) != null) {
                len = len.replace("&apos;", "'");
                bufferedWriter.write(len);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
            return distFile;
        } catch (IOException e) {
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "文件处理异常");
        }
    }

    public static File getFile(String path, String key) {
        System.out.println(ROOT_LOCATION + path);
        File file = new File(ROOT_LOCATION + path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            AssertUtil.assertNotNull(files, "");
            for (File value : files) {
                if (value.getName().contains(key)) {
                    return value;
                }
            }
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, "文件不存在");
        } else {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, "路径错误");
        }
    }

    public static void putFile(File file, String path, String key) {
        try {
            InputStream is = Files.newInputStream(file.toPath());
            String originalFilename = file.getName();

            File toFile = new File(ROOT_LOCATION + path + key + originalFilename.substring(originalFilename.lastIndexOf(".")));
            if (toFile.exists()) {
                AssertUtil.assertTrue(toFile.delete(), "服务端文件重名删除失败");
            }
            AssertUtil.assertTrue(toFile.createNewFile(), "服务端文件创建操作失败");

            FileOutputStream fos = new FileOutputStream(toFile);
            BufferedOutputStream bfos = new BufferedOutputStream(fos);

            long count = IOUtils.copyLarge(is, bfos);
            AssertUtil.assertTrue(count > 0, "复制失败");
            fos.flush();
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "文件处理异常");
        }
    }

    public static void deleteFile(String path, String key) {
        File folder = new File(ROOT_LOCATION + path);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            AssertUtil.assertNotNull(files, "路径异常");
            for (File file : files) {
                if (file.getName().contains(key)) {
                    AssertUtil.assertTrue(file.delete(), "服务端文件重名删除失败");
                }
            }
        } else {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, "路径错误");
        }
    }

}
