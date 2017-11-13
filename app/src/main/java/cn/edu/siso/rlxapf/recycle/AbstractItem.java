package cn.edu.siso.rlxapf.recycle;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public abstract class AbstractItem<VH extends FlexibleViewHolder> extends AbstractFlexibleItem<VH> {

    public enum ITEM_TYPE {CATEGORY, LOCAL, REAL};

    protected String id = StringUtils.EMPTY;
    protected String title = StringUtils.EMPTY;
    protected String statusTitle = StringUtils.EMPTY;
    protected ITEM_TYPE type = ITEM_TYPE.CATEGORY;

    protected Context context = null;

    public AbstractItem(String id, ITEM_TYPE tpye, Context context) {
        this.type = tpye;
        this.id = id;
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractItem) {
            AbstractItem item = (AbstractItem) o;
            return this.id.equals(item.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public ITEM_TYPE getType() {
        return type;
    }

    public void setType(ITEM_TYPE type) {
        this.type = type;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
