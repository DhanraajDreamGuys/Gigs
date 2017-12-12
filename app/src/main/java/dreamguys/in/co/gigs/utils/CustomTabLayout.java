package dreamguys.in.co.gigs.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import dreamguys.in.co.gigs.R;

public class CustomTabLayout extends TabLayout {
    AttributeSet attrs;

    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
    }

    @Override
    public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {

//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = a.getString(R.styleable.CustomTextView_fontName);
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName + ".ttf");
        this.removeAllTabs();

        ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            Tab tab = this.newTab();
            this.addTab(tab.setText(adapter.getPageTitle(i)));
            AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
            view.setTypeface(myTypeface, Typeface.NORMAL);
        }
    }
}