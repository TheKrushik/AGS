package info.krushik.android.ags.objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Product implements Parcelable {
    public static final String TABLE_NAME = "Products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "ProductName";
    public static final String COLUMN_PRICE = "Price";

    @Element
    public long id;
    @Element
    public String ProductName;
    @Element
    public long Price;

    public Product() {
    }

    public Product(String productName, long price) {
        ProductName = productName;
        Price = price;
    }

    @Override
    public String toString() {//переопределяем toString для того чтобы не делать CustomAdapters, а вывести в ArrayAdapter
        return String.format("id %s, %s, price: %s", id, ProductName, Price);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.ProductName);
        dest.writeLong(this.Price);
    }

    protected Product(Parcel in) {
        this.id = in.readLong();
        this.ProductName = in.readString();
        this.Price = in.readLong();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
