package com.vivian.baseproject.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.vivian.baseproject.interfaces.KeyboardInvoke;
import com.vivian.baseproject.R;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends FragmentActivity {

    protected List<BaseFragment> subFragments;
    protected BaseFragment mainFragment;
    public static FragmentManager mManager;

    // protected FragmentManagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        subFragments = new ArrayList<BaseFragment>();

        mainFragment = getMainFragment();

        mManager = getSupportFragmentManager();

        if (mainFragment != null) {

            mManager.beginTransaction().add(R.id.fragment_main, mainFragment, "main").commit();
        }
        // mAdapter = new FragmentManagerAdapter(getSupportFragmentManager());

    }

    /**
     * 将当前fragment加入到list中
     *
     * @param fragment
     */
    public void addSubFragment(BaseFragment fragment) {
        boolean flag = false;
        for (int i = 0; i < subFragments.size(); i++) {
            if (subFragments.get(i).getClass().equals(fragment.getClass())) {
                subFragments.add(subFragments.get(i));
                subFragments.remove(i);
                flag = true;
                break;
            }
        }
        if (!flag) {
            subFragments.add(fragment);
        }
    }

    public BaseFragment getMainFragment() {
        return null;
    }

    public List<BaseFragment> getSubFragments() {
        return subFragments;
    }

    public int getNumberofFragments() {
        if (mainFragment != null) {
            return subFragments.size() + 1;
        } else {
            return subFragments.size();
        }
    }

    public void removeSubFragment(BaseFragment fragment) {
        subFragments.remove(fragment);
    }

    public void removeFragment(BaseFragment fragment) {

        if (fragment == null)
            return;

        if (fragment instanceof KeyboardInvoke) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {

                if (this.getCurrentFocus() != null && this.getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                }

            }
        }

        subFragments.remove(fragment);

        if (subFragments.size() > 0) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(fragment.getEnterAnimation(), fragment.getExitAnimation()).show(subFragments.get(subFragments.size() - 1)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(fragment.getEnterAnimation(), fragment.getExitAnimation()).show(mainFragment).commit();
        }
    }

    public void removeAllSubFragment() {

        if (subFragments.size() > 0) {
            int length = subFragments.size();
            if (length > 1) {
                for (int i = length - 2; i >= 0; i--) {
                    BaseFragment fragment = getFragment(i);
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

                    subFragments.remove(i);
                }
            }

            BaseFragment lastFragment = getLastFragment();

            getSupportFragmentManager().beginTransaction().setCustomAnimations(lastFragment.getEnterAnimation(), lastFragment.getExitAnimation()).remove(lastFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mainFragment).commit();
            subFragments.remove(lastFragment);
        }
    }

    public void bringFragmentToFrontOrStart(BaseFragment f, String tag) {
        if (subFragments.size() > 0) {
            BaseFragment selectedFragment = null;
            BaseFragment lastFragment = subFragments.get(subFragments.size() - 1);

            for (BaseFragment fragment : subFragments) {
                if (fragment.getClass().getName().equals(f.getClass().getName())) {
                    selectedFragment = fragment;
                    break;
                }
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(selectedFragment.getEnterAnimation(), selectedFragment.getExitAnimation()).hide(lastFragment).show(selectedFragment)
                        .commit();

                List<BaseFragment> temp = rearrange(subFragments, selectedFragment);
                subFragments.clear();
                subFragments.addAll(temp);
            } else {
                startFragment(f, tag);
            }

        } else {
            startFragment(f, tag);
        }
    }

    public static <T> List<T> rearrange(List<T> items, T input) {
        int index = items.indexOf(input);
        List<T> copy;
        if (index >= 0) {
            copy = new ArrayList<T>(items.size());
            copy.addAll(items.subList(0, index));
            copy.addAll(items.subList(index + 1, items.size()));
            copy.add(input);
        } else {
            copy = new ArrayList<T>(items);
        }
        return copy;
    }

    public BaseFragment getLastFragment() {
        return isMainFragment() ? mainFragment : subFragments.get(subFragments.size() - 1);
    }

    public BaseFragment getFragment(int index) {
        if (index >= subFragments.size())
            return null;

        return isMainFragment() ? null : subFragments.get(index);
    }

    public boolean isMainFragment() {
        return subFragments == null || subFragments.size() == 0;
    }

    /**
     * 只需在xml文件的onclick事件中添加就可以执行返回操作
     *
     * @param v
     */
    public void actionFinish(View v) {
        getLastFragment().dismiss();
    }


    public BaseFragment getFragment(BaseFragment f) {
        if (mainFragment.getClass().equals(f.getClass())) {
            return mainFragment;
        }
        for (int i = 0; i < subFragments.size(); i++) {
            if (subFragments.get(i).getClass().equals(f.getClass())) {
                return subFragments.get(i);
            }
        }
        return null;
    }

    public void replaceMainFragment(BaseFragment f, String tag) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(f.getEnterAnimation(), f.getExitAnimation()).replace(R.id.fragment_main, f).commit();
    }

    @SuppressLint("NewApi")
    public void startFragment(BaseFragment f, String tag) {

        if (subFragments.contains(f)) {
            Class<?> clazz = f.getClass();
            try {

                BaseFragment newF = (BaseFragment) clazz.newInstance();

                if (f.getArguments() != null)
                    newF.setArguments(f.getArguments());
                f = newF;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (f == null)
            return;
//        String user_unique_key = SharedPMananger.getInstance().getUserUniqueKey();
//        if (TextUtils.isEmpty(user_unique_key) && (f instanceof RequireLogin)) {
//            LoginFragment login = new LoginFragment();
//            login.setPendingFragment(f, tag);
//            f = login;
//            tag = "登录";
//        }
        try {
            if (f.getPopEnterAnimation() == -1 || f.getPopExitAnimation() == -1) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(f.getEnterAnimation(), f.getExitAnimation()).hide(getLastFragment()).add(R.id.fragment_main, f, tag).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(f.getEnterAnimation(), f.getExitAnimation(), f.getPopEnterAnimation(), f.getPopExitAnimation())
                        .hide(getLastFragment()).add(R.id.fragment_main, f, tag).commit();
            }

            subFragments.add(f);
            // addSubFragment(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加切换动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
    }

}
