package com.mengtu.util.storage;

import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * OBS 工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/12 10:06
 */
@Component
public class ObsUtil {
    /**
     * OBS 客户端实例
     */
    private ObsClient obsClient = null;

    /**
     * 过期时间预定义
     */
    private static final long EXPIRE_SECONDS = 300L;

    /**
     * 流操作字符数组大小预定义
     */
    private static final int STREAM_BYTE_ARRAY_SIZE = 1024;

    /**
     * OBS MaxKey 预定义
     */
    private static final int OBS_MAX_KEY_SIZE = 1000;

    /**
     * OBS 分隔符预定义
     */
    private static final String OBS_DELIMITER = "/";

    /**
     * OBS 存储桶名称
     */
    private static final String BUCKET = "mt-backend";

    @Value("${kaichi.obs.endpoint}")
    private String endPoint;

    @Value("${kaichi.obs.access_key_id}")
    private String accessKeyId;

    @Value("${kaichi.obs.secret_access_key}")
    private String secretAccessKey;

    public void getInstance() {
        try {
            obsClient = new ObsClient(accessKeyId, secretAccessKey, endPoint);
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "OBS 实例初始化异常");
        }

        ListBucketsRequest request = new ListBucketsRequest();
        request.setQueryLocation(true);
        List<ObsBucket> buckets = obsClient.listBuckets(request);

        boolean flag = false;
        for (ObsBucket bucket : buckets) {
            if (BUCKET.equals(bucket.getBucketName())) {
                flag = true;
            }
        }
        AssertUtil.assertTrue(flag, "OBS 存储桶异常");
    }

    public void uploadFile(File file, String location, String rename) {
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName(BUCKET);
        request.setObjectKey(location + rename);
        request.setFile(file);
        obsClient.putObject(request);
    }

    public void uploadFile(File file, String location) {
        this.uploadFile(file, location, file.getName());
    }

    public void uploadFile(File file) {
        this.uploadFile(file, "");
    }

    public ObsObject fetchFile(String location, String keyword) {
        ListObjectsRequest request = new ListObjectsRequest(BUCKET);
        request.setMaxKeys(OBS_MAX_KEY_SIZE);
        request.setDelimiter(OBS_DELIMITER);
        request.setPrefix(location + keyword);
        ObjectListing result = obsClient.listObjects(request);

        List<ObsObject> obsObjectList = result.getObjects();
        AssertUtil.assertTrue(!obsObjectList.isEmpty(), RestResultCode.ILLEGAL_PARAMETERS, "OBS错误: 无相关文件");
        AssertUtil.assertTrue(obsObjectList.size() == 1, RestResultCode.ILLEGAL_PARAMETERS, "OBS错误: 重名文件");

        return obsObjectList.get(0);
    }

    public void deleteFile(ObsObject obsObject) {
        obsClient.deleteObject(BUCKET, obsObject.getObjectKey());
    }

    public void deleteFile(String location, String keyword) {
        ObsObject obsObject = fetchFile(location, keyword);
        this.deleteFile(obsObject);
    }

    static void listObjectsByPrefix(ObsClient obsClient, ListObjectsRequest request, ObjectListing result) throws ObsException {
        for (String prefix : result.getCommonPrefixes()) {

            System.out.println("Objects in folder [" + prefix + "]:");

            request.setPrefix(prefix);
            result = obsClient.listObjects(request);
            for (ObsObject obsObject : result.getObjects()) {
                System.out.println("\t" + obsObject.getObjectKey());
                System.out.println("\t" + obsObject.getOwner());
            }
            listObjectsByPrefix(obsClient, request, result);
        }
    }


    public String getSignatureDownloadUrl(ObsObject obsObject, Long lifetime) {
        AssertUtil.assertNotNull(obsObject, RestResultCode.ILLEGAL_PARAMETERS, "OBS错误: 空对象");
        TemporarySignatureRequest signatureRequest = new TemporarySignatureRequest(HttpMethodEnum.GET, lifetime);
        signatureRequest.setBucketName(BUCKET);
        signatureRequest.setObjectKey(obsObject.getObjectKey());

        TemporarySignatureResponse response = obsClient.createTemporarySignature(signatureRequest);
        return response.getSignedUrl();
    }

    public String getSignatureDownloadUrl(ObsObject obsObject) {
        return this.getSignatureDownloadUrl(obsObject, EXPIRE_SECONDS);
    }


    public String getSignatureDownloadUrl(String location, String keyword) {
        return getSignatureDownloadUrl(location, keyword, EXPIRE_SECONDS);
    }

    public String getSignatureDownloadUrl(String location, String keyword, Long lifetime) {
        return getSignatureDownloadUrl(fetchFile(location, keyword), lifetime);
    }

    public void copy(ObsObject obsObject, String key) {
        try {
            CopyObjectResult result = obsClient.copyObject(BUCKET, obsObject.getObjectKey(), BUCKET, key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KaiChiException(RestResultCode.SYSTEM_ERROR, "OBS错误: 拷贝失败");
        }
    }

    public ByteArrayOutputStream download(ObsObject obsObject) {
        try {
            InputStream input = obsClient.getObject(BUCKET, obsObject.getObjectKey()).getObjectContent();
            byte[] b = new byte[STREAM_BYTE_ARRAY_SIZE];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }

            bos.close();
            input.close();
            return bos;
        } catch (Exception ignored) {
        }
        return null;
    }

    public String downloadString(ObsObject obsObject) {
        return download(obsObject).toString();
    }

}
