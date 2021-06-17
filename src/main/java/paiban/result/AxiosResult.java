package paiban.result;

import com.fasterxml.jackson.annotation.JsonInclude;




//转json的时候 只转 值不为null的

public class AxiosResult<T> {
    private int status;
    private String message;
    private T result;

    private AxiosResult() {
    }

    /**
     * 成功返回的方法
     *
     * @param <T>
     * @return
     */
    public static <T> AxiosResult<T> success() {
        return setData(AxiosStatus.OK, null);
    }


    /**
     * 成功返回的方法 带数据
     */
    public static <T> AxiosResult<T> success(T data) {
        return setData(AxiosStatus.OK, data);
    }


    /**
     * 成功返回的方法自定义状态吗
     */
    public static <T> AxiosResult<T> success(AxiosStatus axiosStatus) {
        return setData(axiosStatus, null);
    }

    /**
     * 成功返回的方法自定义状态吗
     */
    public static <T> AxiosResult<T> success(AxiosStatus axiosStatus, T data) {
        return setData(axiosStatus, data);
    }


    /**
     * 错误的返回
     */
    public static <T> AxiosResult<T> error() {
        return setData(AxiosStatus.ERROE, null);
    }


    /**
     * 错误的返回自定义状态码
     */
    public static <T> AxiosResult<T> error(AxiosStatus axiosStatus) {
        return setData(axiosStatus, null);
    }



    /**
     * 封装数据公共方法
     *
     * @param axiosStatus
     * @param data
     * @param <T>
     * @return
     */
    private static <T> AxiosResult<T> setData(AxiosStatus axiosStatus, T data) {
        AxiosResult<T> axiosResult = new AxiosResult<>();
        axiosResult.setResult(data);
        axiosResult.setStatus(axiosStatus.getStatus());
        axiosResult.setMessage(axiosStatus.getMessage());
        return axiosResult;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
