package paiban.result;



public class MyResultException extends RuntimeException {
    private AxiosResult axiosResult;


    public AxiosResult getAxiosResult() {
        return axiosResult;
    }

    public void setAxiosResult(AxiosResult axiosResult) {
        this.axiosResult = axiosResult;
    }

    public MyResultException(AxiosResult axiosResult) {
        this.axiosResult = axiosResult;
    }
}
