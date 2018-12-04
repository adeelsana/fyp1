package fyp.fyp1;



public class Item {
    String name,quantity,contactno, description;

    @Override
    public String toString() {
        return "Item{" +
                "Name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", Contactno='" + contactno + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String comments) {
        this.description = comments;
    }

    public Item(String name, String quantity, String contactno, String descrip) {

        this.name = name;
        this.quantity = quantity;
        this.contactno = contactno;
        this.description = descrip;

    }

    public Item() {

    }
}
