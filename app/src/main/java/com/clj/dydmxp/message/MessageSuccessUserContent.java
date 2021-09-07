package com.clj.dydmxp.message;

public class MessageSuccessUserContent {

    public String getRealNickName() {
        return RealNickName;
    }

    public void setRealNickName(String realNickName) {
        RealNickName = realNickName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "SuccessUserContent{" +
                "RealNickName='" + RealNickName + '\'' +
                ", Content='" + Content + '\'' +
                ", count=" + count +
                '}';
    }

    public String RealNickName;
    public String Content;
    public int count = 0;

}
