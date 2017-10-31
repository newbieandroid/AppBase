package com.fuyoul.sanwenseller.widgets.pickerview;

import android.content.Context;
import android.content.res.AssetManager;


import com.fuyoul.sanwenseller.bean.pickerview.CityModel;
import com.fuyoul.sanwenseller.bean.pickerview.DistrictModel;
import com.fuyoul.sanwenseller.bean.pickerview.ProvinceBean;
import com.fuyoul.sanwenseller.bean.pickerview.ProvinceModel;
import com.fuyoul.sanwenseller.widgets.pickerview.xmlparser.XmlParserHandler;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 作者;陈松林
 * 时间:2016/6/30 15:32
 * Email:806666189@qq.com
 * 功能描述：解析assets下城市数据
 **/
public class InitCityDataHelper {


    private Context context;

    private InitCityDataHelper() {
    }

    private static InitCityDataHelper instance;

    public static InitCityDataHelper getInstance() {

        if (instance == null) {
            instance = new InitCityDataHelper();
        }
        return instance;
    }


    private ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();//PickerView的省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();//PickerView的城市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();//PickerView的区县


    /**
     * 返回所有城市
     *
     * @return
     */
    public ArrayList<ProvinceBean> getCity() {

        return options1Items;
    }

    /**
     * 返回所有城市
     *
     * @return
     */
    public ArrayList<ArrayList<String>> getProvince() {
        return options2Items;
    }

    /**
     * 返回所有曲线
     *
     * @return
     */
    public ArrayList<ArrayList<ArrayList<String>>> getAddress() {
        return options3Items;
    }


    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */

    public void initDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_new.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];


            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                ProvinceBean province = new ProvinceBean(i, provinceList.get(i).getName(), "", "");
                options1Items.add(province);

                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];


                ArrayList<String> city = new ArrayList<String>();

                ArrayList<ArrayList<String>> district_I = new ArrayList<ArrayList<String>>();


                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    city.add(cityNames[j]);

                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];

                    ArrayList<String> district_J = new ArrayList<String>();


                    for (int k = 0; k < districtList.size(); k++) {


                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                        district_J.add(districtModel.getName());

                    }

                    district_I.add(district_J);

                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }

                options3Items.add(district_I);

                options2Items.add(city);
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }


        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void showAddress(@NotNull Context context, OptionsPickerView.OnOptionsSelectListener listener) {
        OptionsPickerView cityPicker = new OptionsPickerView.Builder(context, listener).build();
        cityPicker.setPicker(options1Items, options2Items, options3Items);
        cityPicker.show();
    }

    public void showTime(Context context, TimePickerView.OnTimeSelectListener listener) {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(1950, 0, 1);
        TimePickerView timePickerView = new TimePickerView.Builder(context, listener)

                .setType(new boolean[]{true, true, true, false, false, false})
                .setRangDate(startCalendar, Calendar.getInstance())
                .build();


        timePickerView.show();

    }
}
