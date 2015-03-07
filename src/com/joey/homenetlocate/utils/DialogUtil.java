
package com.joey.homenetlocate.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.joey.homenetlocate.R;

public class DialogUtil
{
    public static class DialogParams
    {
        /**
         * 左侧按钮文字
         */
        private String leftButtonText;
        
        /**
         * 右侧按钮文字
         */
        private String rightButtonText;
        
        /**
         * 消息
         */
        private String messageText;
        
        /**
         * 确认事件
         */
        private OnClickListener confirmListener;
        
        /**
         * 取消事件
         */
        private OnClickListener cancelListener;
        
        /**
         * 为了使用方便,增加参数对应的对话框
         */
        private Dialog dialog;
        
        public DialogParams()
        {
        }
        
        public DialogParams(String... args)
        {
            try
            {
                this.messageText = args[0];
                this.leftButtonText = args[1];
                this.rightButtonText = args[2];
            }
            catch (Exception exp)
            {
                // 不考虑越界异常，初始化参数
            }
        }
        
        public void setConfirmListener(OnClickListener confirmListener)
        {
            this.confirmListener = confirmListener;
        }
        
        public void setCancelListener(OnClickListener concelListener)
        {
            this.cancelListener = concelListener;
        }
        
        public void dismiss()
        {
            DialogUtil.dismiss(dialog);
        }
    }
    
    private static Dialog initializeDialog(Context context, int layoutId)
    {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(layoutId);
        Window window = dialog.getWindow();
        LayoutParams params1 = window.getAttributes();
        int width = ResourceUtil.getScreenWidth(context);
        float margin = 30 * ResourceUtil.getDesity(context) * 2;
        params1.width = Math.round(width - margin);
        window.setAttributes(params1);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    private static Dialog initializeOneButtonDialog(Context context, DialogParams params)
    {
        Dialog dialog = initializeDialog(context, R.layout.dialog_layout);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(params.messageText);
        Button button = (Button) dialog.findViewById(R.id.oneButton);
        button.setText(params.leftButtonText);
        
        if (params.confirmListener != null)
        {
            button.setOnClickListener(params.confirmListener);
        }
        params.dialog = dialog;
        return dialog;
    }
    
    private static Dialog initializeTwoButtonDialog(Context context, DialogParams params)
    {
        Dialog dialog = initializeDialog(context, R.layout.dialog_layout);
        dialog.findViewById(R.id.one_button_layout).setVisibility(View.GONE);
        dialog.findViewById(R.id.two_button_layout).setVisibility(View.VISIBLE);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirmButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        confirmButton.setText(params.leftButtonText);
        cancelButton.setText(params.rightButtonText);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(params.messageText);
        if (params.confirmListener != null)
        {
            confirmButton.setOnClickListener(params.confirmListener);
        }
        if (params.cancelListener != null)
        {
            cancelButton.setOnClickListener(params.cancelListener);
        }
        params.dialog = dialog;
        return dialog;
    }
    
    public static Dialog showOneButtonDialog(Context context, DialogParams params)
    {
        if (context == null || params == null) { return null; }
        Dialog dialog = initializeOneButtonDialog(context, params);
        try
        {
            // dialog从初始化到show的过程中,如果界面销毁,将会导致异常
            dialog.show();
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
        return dialog;
    }
    
    public static Dialog showTwoButtonDialog(Context context, DialogParams params)
    {
        if (context == null || params == null) { return null; }
        Dialog dialog = initializeTwoButtonDialog(context, params);
        try
        {
            // dialog从初始化到show的过程中,如果界面销毁,将会导致异常
            dialog.show();
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
        return dialog;
    }
    
    public static Dialog showProgessDialog(Context context, int msgId)
    {
        if (context == null) { return null; }
        Dialog dialog = initializeDialog(context, R.layout.progress_dialog);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(msgId);
        try
        {
            // dialog从初始化到show的过程中,如果界面销毁,将会导致异常
            dialog.show();
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
        return dialog;
    }
    
    public static void dismiss(Dialog dialog)
    {
        if (dialog == null) { return; }
        dialog.dismiss();
    }
    
    public static void showNetWorkErrorDialog(final Context context)
    {
        if (context == null) { return; }
        String msg = context.getString(R.string.check_network_please);
        String leftButtonText = context.getString(R.string.set_network);
        String rightButtonText = context.getString(R.string.cancel);
        final DialogParams params = new DialogParams(msg, leftButtonText, rightButtonText);
        params.setConfirmListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                params.dismiss();
            }
        });
        params.setCancelListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                params.dismiss();
            }
        });
        Dialog dialog = initializeTwoButtonDialog(context, params);
        try
        {
            // dialog从初始化到show的过程中,如果界面销毁,将会导致异常
            dialog.show();
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }
    
    public static void showFailureDialog(Context context, DialogParams tempParams, String msg)
    {
        if (context == null) { return; }
        final DialogParams params = new DialogParams(msg, context.getString(R.string.ok));
        params.setConfirmListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                params.dismiss(); // 默认动作是关闭对话框
            }
        });
        if (tempParams != null && tempParams.confirmListener != null)
        {
            params.confirmListener = tempParams.confirmListener;
        }
        if (tempParams != null && tempParams.cancelListener != null)
        {
            params.cancelListener = tempParams.cancelListener;
        }
        DialogUtil.showOneButtonDialog(context, params);
    }
}
