import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.indexes.AbstractIndexCreationTask;
import net.ravendb.client.documents.indexes.IndexDefinition;
import net.ravendb.client.documents.operations.counters.CountersDetail;
import net.ravendb.client.documents.operations.counters.GetCountersOperation;
import net.ravendb.client.documents.operations.indexes.GetIndexOperation;
import net.ravendb.client.documents.queries.Query;
import net.ravendb.client.documents.queries.QueryData;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.serverwide.DatabaseRecord;
import net.ravendb.client.serverwide.operations.CreateDatabaseOperation;
import net.ravendb.client.serverwide.operations.DeleteDatabasesOperation;
import net.ravendb.client.serverwide.operations.GetDatabaseNamesOperation;
import org.eclipse.jetty.websocket.api.Session;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Menu {
    public String Id;
    public String Name;
    public List<Course> courseList;

}
class Course {
    public String name;
    public double cost;
    public List<String> allergenics;
}

public class Test {
    private static IndexDefinition index;

    public static void main(String[] args) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{ "http://localhost:8080" },        // URL to the Server,
                // or list of URLs
                // to all Cluster Servers (Nodes)
                "DB-2")                                           // Default database that DocumentStore will interact with
        ) {

            DocumentConventions conventions = store.getConventions();  // DocumentStore customizations

            store.initialize();                                        // Each DocumentStore needs to be initialized before use.
            // This process establishes the connection with the Server
            // and downloads various configurations
            // e.g. cluster topology or client configuration

            IDocumentSession session = store.openSession();
//            Course course = new Course();
//            course.name = "Cereal";
//            course.cost = 1.3;
//            course.allergenics = Arrays.asList("Peanuts");
//
//            Course course2 = new Course();
//            course2.name = "Waffle";
//            course2.cost = 3;
//
//            Menu menu = new Menu();
//            menu.Name = "Breakfast Menu";
//            menu.courseList = new ArrayList<>();
//            menu.courseList.add(course);
//            menu.courseList.add(course2);
//
//            session.store(menu);
//
//            session.saveChanges();


//            Menu menu = session.load(Menu.class, "menus/1-A");
//            System.out.println(menu.Name);
//            //menu.Name = "dinner menu";
//
//            List<Menu> menus = session.query(Menu.class).toList();
//            System.out.println(menus.get(0).Name);
//            System.out.println(menus.get(0).courseList.get(0).name);
//            System.out.println(menus.get(0).courseList.get(1).name);
//
//            QueryData queryData = new QueryData(
//                    new String[] { "Name", "Address.city", "Address.country"},
//                    new String[] { "Name", "City", "Country"});
//
//            List<NameCityAndCountry> results = session
//                    .query(Company.class)
//                    .selectFields(NameCityAndCountry.class, queryData)
//                    .toList();
//
//
//            List<String> courses = session.query(Menu.class)
//                    .selectFields(List.class, "courseList")
//                    .selectFields(String.class, "name")
//                    //.whereGreaterThan("cost", 2)
//
//                    .toList()
//                   ;
//
//            Menu menu2 = new Menu();
//            menu2.Name = "lunch Menu";
//
//           // session.store(menu2, "menus/7-Z");
//           // session.saveChanges();
//            System.out.println(courses.size());
//            System.out.println(courses.get(0));


            List<CompanyDetails> list = session.query(Company.class)
                    .selectFields(CompanyDetails.class, new QueryData(
                            new String[] { "Name", "Address.City", "Address.Country"  },
                            new String[] { "companyName", "city", "country"})
                    ).toList();

            for (CompanyDetails companyDetails : list) {
                System.out.println(companyDetails.companyName);
                System.out.println(companyDetails.city);
                System.out.println(companyDetails.country);
                System.out.println("__________________________");
            }

                        List<String> courses = session.query(Company.class)

                    .selectFields(String.class, "name")
                    //.whereGreaterThan("cost", 2)

                    .toList()
                   ;

            System.out.println(courses);

            IndexDefinition index
                    = store.maintenance()
                    .send(new GetIndexOperation("Orders/Totals"));


            System.out.println(index.getReduce());


//            DatabaseRecord databaseRecord = new DatabaseRecord();
//            databaseRecord.setDatabaseName("MyNewDatabase");
//            store.maintenance().server().send(new CreateDatabaseOperation(databaseRecord));
//
//            store.maintenance().server().send(
//                    new DeleteDatabasesOperation("MyNewDatabase", true, null, Duration.ofSeconds(30)));1

            GetDatabaseNamesOperation operation = new GetDatabaseNamesOperation(0, 1);
            String[] databaseNames = store.maintenance().server().send(operation);
            System.out.println("*****************************");
            for (String s : databaseNames) {
                System.out.println(s);
            }




            //new Employees_ByFullName().execute(store);

//            List<Employee> employees = session
//                    .query(Employee.class)
//                    .whereEquals("FirstName", "Robert")
//                    .andAlso()
//                    .whereEquals("LastName", "King")
//                    .toList();

            List<Employee> employees = session
                    .query(Employee.class, Employees_ByFullName.class)
                    .whereEquals("FullName", "Robert King")
                    .toList();
//            List<Employee> results = session
//                    .query(Employee.class, Employees_ByFirstAndLastName.class)
//                    .whereEquals("FirstName", "Robert")
//                    .toList();
//
//
//            System.out.println("*****************************--");
//            for (Employee s : results) {
//
//                System.out.println(s.FirstName);
//                System.out.println(s.LastName);
//            }

//            List<Employee> employees = session.query(Employee.class, Query.index("Employees/ByFirstName"))
//                    .whereEquals("FirstName", "Robert")
//                    .toList();
            System.out.println(employees.size());
            for (Employee s : employees) {

                System.out.println(s.FirstName);
                System.out.println(s.LastName);
            }

            //new Products_ByCategoryName().execute(store);
            List<Product> results = session
                    .query(Product.class, Products_ByCategoryName.class)
                    .whereEquals("CategoryName", "Beverages")
                    .toList();

            System.out.println(results.size() + "size is");

            for (Product p : results) {
                System.out.println(p.Category);
            }



            CountersDetail operationResult = store.operations()
                    .send(new GetCountersOperation("users/1", "likes"));
        }





    }

    public static class CompanyDetails {
        public String companyName;
        public String city;
        public String country;

    }

    public static class Employees_ImportantDetails extends AbstractIndexCreationTask {

        public Employees_ImportantDetails() {
            map = "docs.Employees.Select(employee => new { " +
                    "    FullName = (employee.FirstName + \" \") + employee.LastName, " +
                    "    Country = employee.Address.Country, " +
                    "    WorkingInCompanySince = employee.HiredAt.Year, " +
                    "    NumberOfTerritories = employee.Territories.Count " +
                    "})";
        }
    }


    public static class Employees_ByFirstAndLastName extends AbstractIndexCreationTask {
        public Employees_ByFirstAndLastName() {
            map = "docs.Employees.Select(employee => new {" +
                    "    FirstName = employee.FirstName," +
                    "    LastName = employee.LastName" +
                    "})";
        }
    }

    public static class Employees_ByFullName extends AbstractIndexCreationTask {
        public Employees_ByFullName() {
            map = "docs.Employees.Select(employee => new { " +
                    "    FullName = (employee.FirstName + \" \") + employee.LastName " +
                    "})";
        }
    }
    public static class Products_ByCategoryName extends AbstractIndexCreationTask {
        public Products_ByCategoryName() {
            map = "docs.Products.Select(product => new { " +
                    "    CategoryName = (this.LoadDocument(product.Category, \"Categories\")).Name " +
                    "})";
        }
    }

}

