package me.parade.architecture.mvvm.adapter;

import android.content.Context;
import android.widget.ExpandableListView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * 可展开收起的Adapter。
 * 它只是利用了{@link GroupedRecyclerViewAdapter}的
 * 删除一组里的所有子项{@link GroupedRecyclerViewAdapter#notifyChildrenRemoved(int)}} 和
 * 插入一组里的所有子项{@link GroupedRecyclerViewAdapter#notifyChildrenInserted(int)}
 * 两个方法达到列表的展开和收起的效果。
 * 这种列表类似于{@link ExpandableListView}的效果。
 * 这里我把列表的组尾去掉是为了效果上更像ExpandableListView。
 */
public class ExpandableAdapter<T> extends GroupedRecyclerViewAdapter {

    private List<T> mGroups;

    public ExpandableAdapter(Context context){
        super(context);
    }

    public ExpandableAdapter(Context context, List<T> groups) {
        super(context);
        mGroups = groups;
    }

    public void setGroups(List<T> list){
        if (mGroups != null && mGroups.size()!=0){
            mGroups.clear();
        }
        mGroups = list;
        notifyDataChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //如果当前组收起，就直接返回0，否则才返回子项数。这是实现列表展开和收起的关键。
        if (!isExpand(groupPosition)) {
            return 0;
        }
        /*List<WorkFlow> children = mGroups.get(groupPosition).getList();
        return children.size();*/
        return 0;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
//        return R.layout.adapter_expandable_header;
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return 0;
//        return R.layout.adapter_child;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
       /* CategoryWorkFlow entity = mGroups.get(groupPosition);
        holder.setText(R.id.tvGroupName, entity.getName());
        ImageView ivState = holder.findView(R.id.ivExpand);
        if(ivState!=null && entity.isExpand()){
            ivState.setRotation(90);
        } else if (ivState!=null){
            ivState.setRotation(0);
        }*/
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
       /* WorkFlow entity = mGroups.get(groupPosition).getList().get(childPosition);
        holder.setText(R.id.tvFlowName, entity.getName());*/
    }

    @Override
    public int getEmptyLayoutId() {
        return 0;
    }

    /**
     * 判断当前组是否展开
     */
    public boolean isExpand(int groupPosition) {
       /* CategoryWorkFlow entity = mGroups.get(groupPosition);
        return entity.isExpand();*/
       return true;
    }

    /**
     * 展开一个组
     */
    public void expandGroup(int groupPosition) {
        expandGroup(groupPosition, false);
    }

    /**
     * 展开一个组
     */
    public void expandGroup(int groupPosition, boolean animate) {
       /* CategoryWorkFlow entity = mGroups.get(groupPosition);
        entity.setExpand(true);*/
        if (animate) {
            notifyChildrenInserted(groupPosition);
        } else {
            notifyDataChanged();
        }
    }

    /**
     * 收起一个组
     */
    public void collapseGroup(int groupPosition) {
        collapseGroup(groupPosition, false);
    }

    /**
     * 收起一个组
     */
    public void collapseGroup(int groupPosition, boolean animate) {
       /* CategoryWorkFlow entity = mGroups.get(groupPosition);
        entity.setExpand(false);*/
        if (animate) {
            notifyChildrenRemoved(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}
