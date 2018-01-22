package com.example.piotr.pogoda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

public class CityList extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    public void click(View view) {

        switch (view.getId()) {
            case R.id.city0:
                setWidth(getLocationWidth());
                setHeight(getLocationHeight());
                break;
            case R.id.city1:
                setWidth("51.75");
                setHeight("19.466669");
                break;
            case R.id.city2:
                setWidth("52.23547");
                setHeight("21.04191");
                break;
            case R.id.city3:
                setWidth("54.3520517");
                setHeight("18.64637");
                break;
            case R.id.city4:
                setWidth("53.429699");
                setHeight("14.62422");
                break;
            case R.id.city5:
                setWidth("50.75185");
                setHeight("19.26737");
                break;
            case R.id.city6:
                setWidth("52.406921");
                setHeight("16.92993");
                break;
            case R.id.city7:
                setWidth("53.013748");
                setHeight("18.598141");
                break;
            case R.id.city8:
                setWidth("53.123501");
                setHeight("18.00762");
                break;
            case R.id.city9:
                setWidth("50.083328");
                setHeight("19.91667");
                break;
            case R.id.city10:
                setWidth("51.099998");
                setHeight("17.033331");
                break;
            case R.id.city11:
                setWidth("50.258419");
                setHeight("19.02754");
                break;
            case R.id.city12:
                setWidth("54.46405");
                setHeight("17.028721");
                break;
            case R.id.city13:
                setWidth("50.666672");
                setHeight("17.950001");
                break;
            case R.id.city14:
                setWidth("53.133331");
                setHeight("23.15");
                break;
            case R.id.city15:
                setWidth("50.870331");
                setHeight("20.62752");
                break;
        }
        Intent intent = new Intent(CityList.this, City.class);
        startActivity(intent);
    }
}
