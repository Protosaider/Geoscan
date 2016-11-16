package com.protosaider.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import javax.xml.datatype.Duration;

import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 * Created by Protosaider on 03.11.2016.
 */

public class MarkerAnimation {

	private CallbackAttempt callbackAttempt;

	private static final String TAG = "Logger";

	private final double DURATION = 1500;
	private boolean isAnimate = false;
	private int latLngArrayListCounter;

	public MarkerSetGetLatLng mark;

	public MarkerAnimation(CallbackAttempt callbackAttempt, int latLngArrayListCounter) {
		// Save the event object for later use.
		this.callbackAttempt = callbackAttempt;
		this.latLngArrayListCounter = latLngArrayListCounter;
	}

	class MarkerSetGetLatLng {

		double lat;
		double lng;

		MarkerSetGetLatLng(Marker marker){
			lat = marker.getPosition().latitude;
			lng = marker.getPosition().longitude;
		}

		public void setLat(double latitude){
			lat = latitude;
		}

		public void setLng(double longitude){
			lng = longitude;
		}

		public void setLatLng(double latitude, double longitude){
			lat = latitude;
			lng = longitude;
		}

		public void setLatLng(LatLng latLng){
			lat = latLng.latitude;
			lng = latLng.longitude;
		}

		public double getLat(){
			return lat;
		}

		public double getLng(){
			return lng;
		}
	}

	public void likeAnimate(final Marker animMarker, final LatLng startLatLng, final LatLng endLatLng){

		if (!isAnimate) {

			mark = new MarkerSetGetLatLng(animMarker);

			LatLng check;
			ValueAnimator valueAnimatorLongitude;

			if (signum(startLatLng.longitude) + signum(endLatLng.longitude) == 0)
			{
				Log.d(TAG, "signum 1 = " + signum(startLatLng.longitude) + " signum 2 = " + signum(endLatLng.longitude) + " total = " + (signum(startLatLng.longitude) + signum(endLatLng.longitude) == 0));
				if (startLatLng.longitude == min(startLatLng.longitude, endLatLng.longitude))
				{
					//check = new LatLng(startLatLng.latitude, startLatLng.longitude + 360);
					valueAnimatorLongitude = ValueAnimator.ofFloat((float) startLatLng.longitude + 360, (float) endLatLng.longitude);
					//Log.d(TAG, "check longitude1 = " + check.longitude);
				}
				else
				{
					//check = new LatLng(endLatLng.latitude, endLatLng.longitude + 360);
					valueAnimatorLongitude = ValueAnimator.ofFloat((float) startLatLng.longitude, (float) endLatLng.longitude + 360);
					//Log.d(TAG, "check longitude2 = " + check.longitude);
				}
			}
			else
			{
				valueAnimatorLongitude = ValueAnimator.ofFloat((float) startLatLng.longitude, (float) endLatLng.longitude);
			}

			ValueAnimator valueAnimatorLatitude = ValueAnimator.ofFloat((float) startLatLng.latitude, (float) endLatLng.latitude);
			//ValueAnimator valueAnimatorLongitude = ValueAnimator.ofFloat((float) startLatLng.longitude, (float) endLatLng.longitude);

			valueAnimatorLatitude.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					double lat = (float) animation.getAnimatedValue();
					mark.setLat(lat);
					animMarker.setPosition(new LatLng(lat, mark.getLng()));
					Log.d(TAG, "onAnimationUpdate lat");
				}
			});

			valueAnimatorLongitude.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					double lng = (float) animation.getAnimatedValue();
					mark.setLng(lng);
					animMarker.setPosition(new LatLng(mark.getLat(), lng));
					Log.d(TAG, "onAnimationUpdate lng");
				}
			});

			AnimatorSet set = new AnimatorSet();
			set.playTogether(valueAnimatorLatitude, valueAnimatorLongitude);
			set.setDuration((long)DURATION);
			set.addListener(getLikeListener());
			set.start();
		}
	}

	private AnimatorListenerAdapter getLikeListener() {
		return new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				isAnimate = true;
				Log.d(TAG, "onAnimationStart");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				isAnimate = false;
				Log.d(TAG, "onAnimationEnd");
				// Signal the even by invoking the interface's method.
				callbackAttempt.callback(latLngArrayListCounter + 1);
			}
		};
	}


}
