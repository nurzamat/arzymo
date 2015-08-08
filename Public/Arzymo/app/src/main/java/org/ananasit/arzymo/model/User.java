package org.ananasit.arzymo.model;

public class User
{
    private String id = "";
    private String client_key = "";
    private String name = "";
    private String phone = "";
    private String email = "";
    private String avatarUrl = "";

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String _avatarUrl) {
        this.avatarUrl = _avatarUrl;
    }


}
