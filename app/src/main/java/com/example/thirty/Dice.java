package com.example.thirty;

import android.os.Parcel;
import android.os.Parcelable;

public class Dice implements Parcelable {

    private int number, face, sides;
    private boolean saved, disabled;
    private String ID;

    /**
     * Construct a new die instance
     * @param sides amount of sides of the die
     */
    public Dice(int sides) {
        this.setSides(sides);
        this.roll();
        this.setSaved(false);
        this.setDisabled(false);
    }

    // Overriden function to implement Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    // Overriden function that writes Dice values
    // to parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(face);
        dest.writeInt(sides);
        dest.writeInt(saved ? 1 : 0);
        dest.writeInt(disabled ? 1 : 0);
        dest.writeString(ID);
    }

    // Creator of Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

    // De-parcel Dice object
    public Dice(Parcel in) {
        number = in.readInt();
        face = in.readInt();
        sides = in.readInt();
        saved = in.readInt() != 0;
        ID = in.readString();
        disabled = in.readInt() != 0;
    }


    /**
     * Roll the die and set new random face
     */
    public void roll() {
        int newSide = (int)Math.floor(Math.random() * this.sides + 1);
        this.setNumber(newSide);
        this.setFace(newSide);
    }

    /**
     * Sets the ID of this die
     * @param ID ID to apply to this die
     */
    public void setID(String ID) {

        this.ID = ID;
    }

    /**
     * Gets the ID of this die
     * @return ID of this die
     */
    public String getID() {

        return this.ID;
    }

    /**
     * Set amount of sides on the die
     * @param sides amount of sides wanted on this die
     */
    public void setSides(int sides) {
        this.sides = sides;
    }

    /**
     * Set new face of this die
     * @param number The number to represent as a face
     */
    public void setFace(int number) {

        this.face = number - 1;
    }

    /**
     * Gets the face number of this die
     * @return an integer representing the die face
     */
    public int getFace() {
        return this.face;
    }

    /**
     * Gets the current number showing on the die
     * @return the current number of the die as an integer
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Sets the current number of the die
     * @param number the number to set on the die
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Sets the save state of the die
     * @param saved boolean value to represent the save state
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Sets the disabled state of the die
     * @param saved boolean value to represent the disabled state
     */
    public void setDisabled(boolean disabled) {

        this.disabled = disabled;
    }

    /**
     * Checks if current die is saved
     * @return a boolean value representing the current save state
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Checks if current die is disabled
     * @return a boolean value representing the current disabled state
     */
    public boolean isDisabled() {

        return disabled;
    }

    /**
     * Toggles the save state on the die.
     */
    public void toggleSaved() {
        if(saved){
            this.setSaved(false);
        } else {
            this.setSaved(true);
        }
    }
}