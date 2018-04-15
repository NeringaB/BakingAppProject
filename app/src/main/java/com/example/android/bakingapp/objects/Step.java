package com.example.android.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This {@link Step} object contains step information:
 * step id, short description, description, video url and thumbnail url.
 */
public class Step implements Parcelable {

    // Step id
    private int stepId;
    // Step short description
    private String stepShortDescription;
    // Step description
    private String stepDescription;
    // Step video URL
    private String stepVideoUrl;
    // Step thumbnail URL
    private String stepThumbnailUrl;

    /**
     * Constructs a new {@link Step} object.
     *
     * @param stepId                    is the id for the step
     * @param stepShortDescription      is the short description of the step
     * @param stepDescription           is the description of the step
     * @param stepVideoUrl              is the video url for the step
     * @param stepThumbnailUrl          is the thumbnail url for the step
     */
    public Step(int stepId, String stepShortDescription, String stepDescription,
                String stepVideoUrl, String stepThumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepDescription = stepDescription;
        this.stepVideoUrl = stepVideoUrl;
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    protected Step(Parcel in) {
        this.stepId = in.readInt();
        this.stepShortDescription = in.readString();
        this.stepDescription = in.readString();
        this.stepVideoUrl = in.readString();
        this.stepThumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.stepId);
        parcel.writeString(this.stepShortDescription);
        parcel.writeString(this.stepDescription);
        parcel.writeString(this.stepVideoUrl);
        parcel.writeString(this.stepThumbnailUrl);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    // Returns the step id.
    public int getStepId() {
        return stepId;
    }

    // Returns the recipe name.
    public String getStepShortDescription() {
        return stepShortDescription;
    }

    // Returns the recipe image.
    public String getStepDescription() {
        return stepDescription;
    }

    // Returns the recipe video url.
    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    // Returns the recipe thumbnail url.
    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }
}
