package com.fuyoul.sanwenseller.bean.pickerview;


import com.fuyoul.sanwenseller.bean.pickerview.IPickerViewData;

public class DistrictModel implements IPickerViewData {
    private String name;
    private String zipcode;

    @Override
    public String getPickerViewText() {
        return name;
    }

    public DistrictModel() {
        super();
    }

    public DistrictModel(String name, String zipcode) {
        super();
        this.name = name;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
    }

}
