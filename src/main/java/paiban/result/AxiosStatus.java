package paiban.result;

/**
 * 开发者：辉哥
 * 特点： 辉哥很帅
 * 开发时间：2021/1/1 12:18
 * 文件说明：
 */

public enum AxiosStatus {

    OK(0,"操作成功"),
    ERROE(1,"操作失败"),
    PERSON_NUM_NOT_ENOUGH(1,"排班人员数量不足"),
    ;

    AxiosStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private  int status;

    private String message;

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
}
