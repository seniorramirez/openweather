package com.example.kevinramirez.openweatcher.Model;

/**
 * Created by william.montiel on 4/12/2017.
 */

public class City {

    protected int idCity;
    protected String nameCity;

    public void City(int idCity,String nameCity){
        setId(idCity);
        setName(nameCity);
    }

    public void setId(int idCity){
        this.idCity = idCity;
    }
    public void setName(String nameCity){
        this.nameCity = nameCity;
    }

    public int getIdCity(){
        return this.idCity;
    }

    public String getNameCity(){
        return this.nameCity;
    }


}
