package cn.edu.siso.rlxapf.bean;

/**
 * Created by taowenyin on 17-7-21.
 */

public class UserBean {

    private String account = null;
    public static String ACCOUNT_KEY = "account";

    private String password = null;
    public static String PASSWORD_KEY = "password";

    private String name = null;
    public static String NAME_KEY = "name";

    private String address = null;
    public static String ADDRESS_KEY = "address";

    private String contact = null;
    public static String CONTACT_KEY = "contact";

    private String phone = null;
    public static String PHONE_KEY = "phone";

    private String mobileId = null;
    public static String MOBILE_ID_KEY = "mobileId";

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }
}
