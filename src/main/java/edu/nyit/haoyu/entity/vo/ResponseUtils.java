package edu.nyit.haoyu.entity.vo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/08/6:06
 * @Description:
 */
public class ResponseUtils {

    private static int retCode = 0;

    public ResponseUtils() {
    }

    public static void setRetCode(int systemCode) {
        if (retCode == 0) {
            retCode = systemCode;
        }
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse();
        response.setData(data);
        return response;
    }
}
