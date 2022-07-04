//package com.mengtu.kaichi.user.util;
//
//import com.UpYun;
//import com.mengtu.util.enums.CommonResultCode;
//import com.mengtu.util.exception.KaiChiException;
//import com.mengtu.util.hash.HashUtil;
//import com.mengtu.util.tools.AssertUtil;
//import com.mengtu.util.upyun.UssUtil;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 用户头像操作工具
// *
// * @author GHT
// * @version 1.2 @ 2020.05.06 11:58 AM
// */
//@Component
//public class AvatarUtil {
//    /**
//     * CDN 基础路径
//     */
//    private static final String CDN_URL = "https://cdn.kaichi.tonyguo.cn";
//
//    /**
//     * 斜杠
//     */
//    private static final String FLAG_SLANTING_ROD = "/";
//
//    /**
//     * 叹号（Content-Secret 间隔标识符）
//     */
//    private static final String FLAG_EXCLAMATION_MARK = "!";
//
//    /**
//     * UpYun API 返回值 40400001
//     */
//    private static final String NOT_FOUND_FLAG = "\"msg\":\"file or directory not found\",\"code\":40400001";
//
//    @Resource
//    private UssUtil ussUtil;
//
//    /**
//     * 根据命名规约，拼装目标用户头像所在文件夹的路径
//     *
//     * @param userId UserID
//     * @return (String)UserAvatarFolderUri
//     */
//    private static String getUserAvatarFolderUri(String userId) {
//        return "/userInfo/" + userId + "/avatar";
//    }
//
//    /**
//     * 获取目标用户头像文件夹信息
//     *
//     * @param userId UserID
//     * @return FolderItemIter
//     */
//    private UpYun.FolderItemIter getAvatarFolderInfo(String userId) {
//        UpYun.FolderItemIter result;
//        try {
//            result = ussUtil.readDirIter(getUserAvatarFolderUri(userId));
//        } catch (Exception e) {
//            if (e.getCause().getMessage().contains(NOT_FOUND_FLAG)) {
//                AssertUtil.assertTrue(ussUtil.mkDir(getUserAvatarFolderUri(userId), true), "用户头像目录创建失败");
//                this.getAvatarFolderInfo(userId);
//            }
//            throw new KaiChiException(e, CommonResultCode.SYSTEM_ERROR.getCode(), "USS 接口异常");
//        }
//        AssertUtil.assertTrue(!result.files.isEmpty(), "用户无头像");
//        return result;
//    }
//
//    /**
//     * 获取目标用户头像文件的服务器路径
//     *
//     * @param userId UserID
//     * @return (String)URI
//     */
//    public String getAvatarUri(String userId) {
//        return getUserAvatarFolderUri(userId) + FLAG_SLANTING_ROD + this.getAvatarFolderInfo(userId).files.get(0).name;
//    }
//
//    /**
//     * 获取目标用户头像文件的 CDN 外部访问链接
//     *
//     * @param userId UserID
//     * @return (String)URL
//     */
//    public String getAvatarUrl(String userId) {
//        String uri = this.getAvatarUri(userId);
//        return CDN_URL + uri + FLAG_EXCLAMATION_MARK + HashUtil.sha256(HashUtil.sha256(userId) + uri);
//    }
//
//    /**
//     * 上传更新用户头像
//     *
//     * @param userId UserID
//     * @return true if success
//     */
//    public boolean updateAvatar(String userId, File file) {
//        try {
//            AssertUtil.assertTrue(ussUtil.deleteFile(this.getAvatarUri(userId)));
//        } catch (Exception ignored) {
//        }
//        Map<String, String> params = new HashMap<>(1);
//        params.put("Content-Secret", HashUtil.sha256(HashUtil.sha256(userId) + getUserAvatarFolderUri(userId) + FLAG_SLANTING_ROD + file.getName()));
//        return ussUtil.writeFile(getUserAvatarFolderUri(userId) + FLAG_SLANTING_ROD + file.getName(), file, true, params);
//    }
//
//}
