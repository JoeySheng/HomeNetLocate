
package com.joey.homenetlocate.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.joey.homenetlocate.ui.interfaces.IUiController;

/**
 * 文件名称 : BaseActivity
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : BaseActivity - 所有activity基类
 * <p>
 * 创建时间 : 2014-3-25 上午12:35:26
 * <p>
 */
public abstract class BaseActivity extends Activity implements IUiController
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }
    
    /**
     * 初始化
     */
    private void init()
    {
        initView();
        initData();
        setListener();
    };
}
