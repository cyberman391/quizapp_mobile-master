package com.quizest.quizestapp.AdapterPackage;


/*all used classes are imported here */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;


public class ImageSliderAdapter extends PagerAdapter {

    /*all global field instances and variables */
    private List<Integer> images;
    private LayoutInflater inflater;
    private Context context;


    /**
     * constructor for getting the array list of values and current activity
     * @param images
     * @param context
     */
    public ImageSliderAdapter(List<Integer> images, Context context) {
        this.images = images;
        this.context = context;
    }


    /*return the total size of the list of images for setting the view pager length*/
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     * return is the current view and the passed object is same or not
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    /**
     * set the image view of the by getting the image form the list of images and add the imageview int the viewgroup
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        /*make imageview with current activity*/
        ImageView mImageView = new ImageView(context);
        /*set centrecrop scaling the imageview */
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        /*bind the image to the imageview by taking the image from the list with position*/
         mImageView.setImageResource(images.get(position));

        /*set imageview to the viewgroup*/
        container.addView(mImageView, 0);

        return mImageView;

    }


    /*remove the imageview if it is already there and add the new one */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }


}
