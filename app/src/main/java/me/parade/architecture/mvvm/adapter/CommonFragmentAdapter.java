package me.parade.architecture.mvvm.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by parade岁月 on 2019/4/11 22:07
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {
    private Fragment currentFragment;
    private List<Fragment> mFragments;
    private List<String> titleList;
    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> mFragments) {
        this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mFragments);
    }

    /**
     * 配合tabLayout使用需要设置标题时使用
     * @param titleList 需要设置的标题集合
     */
    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> titleList) {
        this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mFragments);
        this.titleList = titleList;
    }

    private CommonFragmentAdapter(FragmentManager fm, int behavior, List<Fragment> mFragments) {
        super(fm,behavior);
        this.mFragments = mFragments;
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     *记录当前的fragment
     */
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    /**
     * 获取保存的当前的fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
