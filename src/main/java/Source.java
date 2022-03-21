import java.util.List;

class Source {
}
class Company
{
    public String Id ;
    public String ExternalId ;
    public String Name ;
    public Contact Contact ;
    public Address Address ;
    public String Phone ;
    public String Fax ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getExternalId() {
        return ExternalId;
    }

    public void setExternalId(String externalId) {
        ExternalId = externalId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Contact getContact() {
        return Contact;
    }

    public void setContact(Contact contact) {
        Contact = contact;
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }
}

class Address
{
    public String Line1 ;
    public String Line2 ;
    public String City ;
    public String Region ;
    public String PostalCode ;
    public String Country ;
    public Location Location ;
}

class Location
{
    public double Latitude ;
    public double Longitude ;
}

class Contact
{
    public String Name ;
    public String Title ;
}

class Category
{
    public String Id ;
    public String Name ;
    public String Description ;
}

class Order
{
    public String Id ;
    public String Company ;
    public String Employee ;
//    public DateTime OrderedAt ;
//    public DateTime RequireAt ;
//    public DateTime? ShippedAt ;
    public Address ShipTo ;
    public String ShipVia ;
    public double Freight ;
    public List<OrderLine> Lines ;
}

class OrderLine
{
    public String Product ;
    public String ProductName ;
    public double PricePerUnit ;
    public int Quantity ;
    public double Discount ;
}

class Product
{
    public String Id ;
    public String Name ;
    public String Supplier ;
    public String Category ;
    public String QuantityPerUnit ;
    public double PricePerUnit ;
    public int UnitsInStock ;
    public int UnitsOnOrder ;
    public boolean Discontinued ;
    public int ReorderLevel ;
}

class Supplier
{
    public String Id ;
    public Contact Contact ;
    public String Name ;
    public Address Address ;
    public String Phone ;
    public String Fax ;
    public String HomePage ;
}

class Employee
{
    public String Id ;
    public String LastName ;
    public String FirstName ;
    public String Title ;
    public Address Address ;
//    public DateTime HiredAt ;
//    public DateTime Birthday ;
    public String HomePhone ;
    public String Extension ;
    public String ReportsTo ;
    public List<String> Notes ;
    public List<String> Territories ;
}

class Region
{
    public String Id ;
    public String Name ;
    public List<Territory> Territories ;
}

class Territory
{
    public String Code ;
    public String Name ;
    public String Area ;
}

class Shipper
{
    public String Id ;
    public String Name ;
    public String Phone ;
}