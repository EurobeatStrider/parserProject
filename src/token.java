class token
{
    String type;
    String id;

    public void set(String type)
    {
        this.type = type;
    }
    public void setID(String type, String id) {
        this.type = type;
        this.id = id;
    }
    public String get()
    {
        return type;
    }
    public String getID()
    {
        return id;
    }

}
