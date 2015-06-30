package com.vivian.baseproject.base;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.vivian.baseproject.activities.MainActivity;
import com.vivian.baseproject.R;
import com.vivian.baseproject.net.BaseHandle;

import java.lang.reflect.Field;

public abstract class BaseFragment extends Fragment {
    // fragment的标识
    protected int mId;
    protected String mName;
    private LinearLayout fragmentLayout;

    private String tag;

    private static final String PENDING_FRAGMENT_CLASS_FLAG = "pending_fragment_class";
    private static final String PENDING_FRAGMENT_ARGUMENT_FLAG = "pending_fragment_argument";
    private static final String PENDING_FRAGMENT_TAG_FLAG = "pending_fragment_tag";

    public BaseFragment(int id, String Name) {
        super();
    }

    public BaseFragment() {
        super();
    }

    public void requestData(BaseHandle handle) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = new LinearLayout(getActivity());
        View child = onChildCreateView(inflater, container, savedInstanceState);
        child.setClickable(true);
        fragmentLayout.addView(child, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return fragmentLayout;
    }

    public void dismiss() {
        if (getActivity() instanceof BaseActivity) {

            BaseFragment f = getPendingFragment();
            if (f != null) {
                ((BaseActivity) getActivity()).startFragment(f, f.getTag());
            }

            if (((BaseActivity) getActivity()).getSubFragments().size() == 0 || ((BaseActivity) getActivity()).getSubFragments() == null) {
                ((BaseActivity) getActivity()).finish();
            } else {
                ((BaseActivity) getActivity()).removeFragment(this);
            }

            // getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out).remove(this).show(((BaseActivity)
            // getActivity()).getLastFragment()).commit();
        }
    }

    public BaseFragment getPendingFragment() {
        if (this.getArguments() != null && this.getArguments().containsKey(PENDING_FRAGMENT_CLASS_FLAG)) {
            String className = this.getArguments().getString(PENDING_FRAGMENT_CLASS_FLAG);
            try {
                Class<?> clazz = Class.forName(className);
                BaseFragment fragment = (BaseFragment) clazz.newInstance();

                if (this.getArguments().getBundle(PENDING_FRAGMENT_ARGUMENT_FLAG) != null) {
                    fragment.setArguments(this.getArguments().getBundle(PENDING_FRAGMENT_ARGUMENT_FLAG));
                }

                if (this.getArguments().getString(PENDING_FRAGMENT_TAG_FLAG) != null) {
                    fragment.setFragmentTag(this.getArguments().getString(PENDING_FRAGMENT_TAG_FLAG));
                }

                return fragment;

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    public void setPendingFragment(Fragment fragment, String tag) {
        if (this.getArguments() == null) {
            Bundle bundle = new Bundle();

            this.setArguments(bundle);
        }

        this.getArguments().putString(PENDING_FRAGMENT_CLASS_FLAG, fragment.getClass().getName());

        if (fragment.getArguments() != null) {
            this.getArguments().putBundle(PENDING_FRAGMENT_ARGUMENT_FLAG, fragment.getArguments());
        }

        if (tag != null) {
            this.getArguments().putString(PENDING_FRAGMENT_TAG_FLAG, tag);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        try {
            if (enter) {
                Animation enterAnimation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                enterAnimation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        onEnterAnimationStarted(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        onEnterAnimationEnded(animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                return enterAnimation;
            } else {
                Animation exitAnimation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                exitAnimation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        onExitAnimationStarted(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        onExitAnimationEnded(animation);

                        BaseFragment fragment = getPendingFragment();
                        if (fragment != null) {
                            ((BaseActivity) getActivity()).startFragment(fragment, null);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                });

                return exitAnimation;
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected void onEnterAnimationStarted(Animation anim) {
    }

    ;

    protected void onEnterAnimationEnded(Animation anim) {
    }

    ;

    protected void onExitAnimationStarted(Animation anim) {
    }

    ;

    protected void onExitAnimationEnded(Animation anim) {
    }

    ;

    /**
     * 子类实现视图界面
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View onChildCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstance) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
        MainActivity activity = (MainActivity) getActivity();
        // activity.getAdapter().changeName();
    }

    public void setFragmentTag(String tag) {
        this.tag = tag;
    }

    public void updateFragmentView() {
        // 子类实现
    }

    public void requestLayout() {
        if (fragmentLayout != null) {
            fragmentLayout.requestLayout();
        }
    }

    public void copy(BaseFragment baseFragment) {

    }

    protected void titleChange(String name) {
        this.mName = name;
        MainActivity activity = (MainActivity) getActivity();
        // activity.getAdapter().titleChange(this);
    }

    /**
     * @param v
     */
    public void actionFinish(View v) {
        ((BaseActivity) getActivity()).getLastFragment().dismiss();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getEnterAnimation() {
        return R.anim.fragment_fade_in;
    }

    public int getExitAnimation() {
        return R.anim.fragment_fade_out;
    }

    public int getPopEnterAnimation() {
        return -1;
    }

    public int getPopExitAnimation() {
        return -1;
    }


}
