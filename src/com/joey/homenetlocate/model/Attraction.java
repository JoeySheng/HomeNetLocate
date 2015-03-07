
package com.joey.homenetlocate.model;

/**
 * 文件名称 : Attraction
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : Attraction - 景点位置信息
 * <p>
 * 创建时间 : 2014-3-24 下午10:10:37
 * <p>
 */
public class Attraction
{
    // id
    private int id;
    
    // 名称
    private String name;
    
    // 经度
    private double longitude;
    
    // 纬度
    private double latitude;
    
    // 时间
    private long time;
    
    // 城市
    private String city;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public double getLongitude()
    {
        return longitude;
    }
    
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    public double getLatitude()
    {
        return latitude;
    }
    
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    
    @Override
    public String toString()
    {
        return "id=" + id + ",name=" + name + ",longitude=" + longitude + ",latitude=" + latitude;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Attraction)) { return false; }
        if (((Attraction) o).getName().equals(this.getName())) { return true; }
        return false;
    }
    
    public long getTime()
    {
        return time;
    }
    
    public void setTime(long time)
    {
        this.time = time;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
}
