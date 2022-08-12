package com.example.service.Bean;

public class RemoteOperation {
    private long vmc_no;
    private String operation;
    private String cmd;

    public long getVmc_no() {
        return vmc_no;
    }

    public void setVmc_no(long vmc_no) {
        this.vmc_no = vmc_no;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
