package com.example.clay.a20181220;

public class Account
{
    private Long id;
    private String datetime;
    private String info;
    private Integer value;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        if(value>=0)
            this.value = value;
    }


    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Account(long id, String datetime, String info, Integer value)
    {
        super();
        this.id = id;
        this.datetime = datetime;
        this.value = value;
        this.info = info;
    }

    public Account(String datetime, String info, Integer value)
    {
        super();
        this.datetime = datetime;
        this.info = info;
        this.value = value;
    }

    public Account()
    {
        super();
    }

    public String toString()
    {
        return "[序號：" + id + ",日期：" + datetime + ",資訊：" + info + ",金額：" + value + "]";
    }
}