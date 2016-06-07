package info.krushik.android.ags.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {
    public static final String TABLE_NAME = "Clients";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_CARD = "idCard";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_EMAIL = "Email";


    public long id;
    public long idCard;
    public String FirstName;
    public String LastName;
    public long Phone;
    public String Email;

    public Client() {
    }

    public Client(long idCard, String firstName, String lastName, long phone) {
        this.idCard = idCard;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
    }

    @Override
    public String toString() {//переопределяем toString для того чтобы не делать CustomAdapters, а вывести в ArrayAdapter
        return String.format("id %s, %s %s %s, %s, %s", id, idCard, FirstName, LastName, Phone, Email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.idCard);
        dest.writeString(this.FirstName);
        dest.writeString(this.LastName);
        dest.writeLong(this.Phone);
        dest.writeString(this.Email);
    }

    protected Client(Parcel in) {
        this.id = in.readLong();
        this.idCard = in.readLong();
        this.FirstName = in.readString();
        this.LastName = in.readString();
        this.Phone = in.readLong();
        this.Email = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };
}
