package edu.nyit.haoyu.entity.vo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/08/6:02
 * @Description:
 */
public class ApiResponse<T> {

    protected T data;

    public ApiResponse() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
