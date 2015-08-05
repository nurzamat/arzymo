package org.ananasit.arzymo.model;

public class User
{
    private String id, client_key, name, phone, email, avatar, password;
    private boolean isLogedIn = false;
    private boolean isRegistered = false;
    private boolean isActivated = false;

    public User() {
    }

    public String getId()
    {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getClient_key() {
        return client_key;
    }

    public void setClient_key(String _client_key) {
        this.client_key = _client_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name)
    {
        this.name = _name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String _phone) {
        this.phone = _phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String _avatar) {
        this.avatar = _avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _pass)
    {
        this.password = _pass;
    }

    public boolean isLogedIn() {
        return isLogedIn;
    }

    public void setLogedIn(boolean _loged)
    {
        this.isLogedIn = _loged;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean _reged)
    {
        this.isRegistered = _reged;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean _act)
    {
        this.isActivated = _act;
    }

}
