package com.fuyoul.sanwenseller.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * 创 建 人:  Chen
 * 创建时间:  2017/4/13 11:49
 * 描    述:
 */
public class AddFragmentUtils {


    private FragmentManager manager;
    private List<Fragment> list;
    private int layoutID;

    private Fragment currentFragment;

    public FragmentManager getManager() {

        return manager;
    }

    /**
     * @param context
     */
    public AddFragmentUtils(AppCompatActivity context, int layoutID) {

        this.layoutID = layoutID;
        manager = context.getSupportFragmentManager();
    }

    public AddFragmentUtils(Fragment fragment, int layoutID) {

        this.layoutID = layoutID;
        manager = fragment.getChildFragmentManager();
    }


    /**
     * @param addFragment 被加载的Fragment
     * @param tag         被加载Fragment的标签
     */
    public void showFragment(Fragment addFragment, String tag) {


        FragmentTransaction fragmentT = manager.beginTransaction();
        list = manager.getFragments();

        //如果第一次加载
        if (list == null || list.size() <= 0) {
            fragmentT.add(layoutID, addFragment, tag).commit();
            currentFragment = addFragment;
            return;
        }


        //如果是同一个
        if (currentFragment == addFragment) {
            return;
        }

        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {//如果有数据
            fragmentT.hide(currentFragment).show(fragment).commit();
        } else {//如果没有数据
            fragment = addFragment;
            fragmentT.hide(currentFragment).add(layoutID, fragment, tag).commit();
        }
        currentFragment = fragment;

    }


    public Fragment getCurrentFragment() {

        return currentFragment;
    }


    private FragmentManager childManager;


}
