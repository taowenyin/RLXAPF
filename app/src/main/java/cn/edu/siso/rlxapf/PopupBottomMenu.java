package cn.edu.siso.rlxapf;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;


public class PopupBottomMenu extends PopupWindow {

    public static String TITLE_KEY = "TITLE";

    private View rootView = null;

    private ListView popupMenu = null;
    private Button cancelBtn = null;

    private SimpleAdapter adapter = null;

    public PopupBottomMenu(Context context, List<Map<String, String>> data,
                           AdapterView.OnItemClickListener itemClickListener,
                           OnDismissListener dismissListener,
                           View.OnClickListener cancelListener) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.popup_window_layout, null);
        popupMenu = (ListView) rootView.findViewById(R.id.popup_menu_list);
        cancelBtn = (Button) rootView.findViewById(R.id.popup_menu_cancel);
        adapter = new SimpleAdapter(context,
                data,
                R.layout.popup_window_item_layout,
                new String[]{TITLE_KEY},
                new int[]{R.id.popup_item_text});
        popupMenu.setAdapter(adapter);
        popupMenu.setOnItemClickListener(itemClickListener);

        setOutsideTouchable(true); // 设置外部可点击
        setContentView(rootView); // 设置视图
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        setOnDismissListener(dismissListener);
        setAnimationStyle(R.style.popup_menu_anim);
        cancelBtn.setOnClickListener(cancelListener);
    }
}
