
package com.joey.homenetlocate.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.joey.homenetlocate.R;

/**
 * 文件名称 : RouteDialog
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : RouteDialog - 路线
 * <p>
 * 创建时间 : 2014-3-25 上午12:32:54
 * <p>
 */
public class RouteDialog extends Dialog
{
    
    private EditText startEdt;
    
    private EditText endEdt;
    
    private Button changeBtn;
    
    private Button book1Btn;
    
    private Button book2Btn;
    
    private Button searchBusBtn;
    
    private Button searchWalkBtn;
    
    private RelativeLayout startLayout;
    
    private RelativeLayout endLayout;
    
    private LinearLayout closeImg;
    
    public RelativeLayout getStartLayout()
    {
        return startLayout;
    }
    
    public void setStartLayout(RelativeLayout startLayout)
    {
        this.startLayout = startLayout;
    }
    
    public RelativeLayout getEndLayout()
    {
        return endLayout;
    }
    
    public void setEndLayout(RelativeLayout endLayout)
    {
        this.endLayout = endLayout;
    }
    
    private ListView startList;
    
    private ListView endList;
    
    public ListView getStartList()
    {
        return startList;
    }
    
    public void setStartList(ListView startList)
    {
        this.startList = startList;
    }
    
    public ListView getEndList()
    {
        return endList;
    }
    
    public void setEndList(ListView endList)
    {
        this.endList = endList;
    }
    
    public EditText getStartEdt()
    {
        return startEdt;
    }
    
    public void setStartEdt(EditText startEdt)
    {
        this.startEdt = startEdt;
    }
    
    public EditText getEndEdt()
    {
        return endEdt;
    }
    
    public void setEndEdt(EditText endEdt)
    {
        this.endEdt = endEdt;
    }
    
    public Button getChangeBtn()
    {
        return changeBtn;
    }
    
    public void setChangeBtn(Button changeBtn)
    {
        this.changeBtn = changeBtn;
    }
    
    public Button getBook1Btn()
    {
        return book1Btn;
    }
    
    public void setBook1Btn(Button book1Btn)
    {
        this.book1Btn = book1Btn;
    }
    
    public Button getBook2Btn()
    {
        return book2Btn;
    }
    
    public void setBook2Btn(Button book2Btn)
    {
        this.book2Btn = book2Btn;
    }
    
    public Button getSearchBusBtn()
    {
        return searchBusBtn;
    }
    
    public void setSearchBusBtn(Button searchBusBtn)
    {
        this.searchBusBtn = searchBusBtn;
    }
    
    public Button getSearchWalkBtn()
    {
        return searchWalkBtn;
    }
    
    public void setSearchWalkBtn(Button searchWalkBtn)
    {
        this.searchWalkBtn = searchWalkBtn;
    }
    
    public LinearLayout getCloseImg()
    {
        return closeImg;
    }
    
    public void setCloseImg(LinearLayout closeImg)
    {
        this.closeImg = closeImg;
    }
    
    public RouteDialog(Context context, int style)
    {
        super(context, style);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        getWindow().setAttributes(windowParams);
        startEdt = (EditText) findViewById(R.id.from_txt);
        endEdt = (EditText) findViewById(R.id.to_txt);
        searchBusBtn = (Button) findViewById(R.id.search_bus_btn);
        searchWalkBtn = (Button) findViewById(R.id.search_walk_btn);
        startLayout = (RelativeLayout) findViewById(R.id.qi_dian_list_layout);
        endLayout = (RelativeLayout) findViewById(R.id.zhong_dian_list_layout);
        startList = (ListView) findViewById(R.id.list_view_01);
        endList = (ListView) findViewById(R.id.list_view_02);
        closeImg = (LinearLayout) findViewById(R.id.close_img);
    }
    
}
