
package com.joey.homenetlocate.ui.interfaces;

/**
 * 文件名称 : IUiController
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : IUiController - UI基础操作接口
 * <p>
 * 创建时间 : 2014-3-25 上午12:33:10
 * <p>
 */
public interface IUiController
{
    /**
     * 初始化控件
     */
    void initView();
    
    /**
     * 初始化数据
     */
    void initData();
    
    /**
     * 设置监听事件
     */
    void setListener();
}
