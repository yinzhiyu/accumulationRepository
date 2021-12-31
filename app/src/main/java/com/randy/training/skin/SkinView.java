package com.randy.training.skin;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * @author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/12/3010:29
 * desc   :
 */
public class SkinView {
    View view;
    HashMap<String, String> attrsMap = new HashMap<String, String>();

    /**
     * 应用换肤
     */
    public void changeSkin() {
        if (TextUtils.isEmpty(attrsMap.get("background"))) {
            int bgId = Integer.parseInt(attrsMap.get("background").substring(1));
            String attrsType = view.getResources().getResourceTypeName(bgId);
            if (TextUtils.equals(attrsType, "drawable")) {
                view.setBackgroundDrawable(ShinEngine.getInstance().getDrawable(bgId));
            } else if (TextUtils.equals(attrsType, "color")) {
                view.setBackgroundColor(ShinEngine.getInstance().getColor(bgId));
            }
        }
        if (view instanceof TextView) {
            if (!TextUtils.isEmpty(attrsMap.get("textColor"))) {
                int textColorId = Integer.parseInt(attrsMap.get("textColor").substring(1));
                ((TextView) view).setTextColor(ShinEngine.getInstance().getColor(textColorId));
            }
        }
    }
}
