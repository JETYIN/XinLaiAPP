package com.work.xinlai.work.testhttp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/8.
 */
public class Student implements Parcelable{

    public String title;

    protected Student(Parcel in) {
        title = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }
}
