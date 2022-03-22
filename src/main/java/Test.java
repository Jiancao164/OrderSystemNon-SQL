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
import org.checkerframework.checker.units.qual.C;
import org.eclipse.jetty.websocket.api.Session;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Test {
    public static void main(String[] args) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{ "http://localhost:8080" },        // URL to the Server,
                // or list of URLs
                // to all Cluster Servers (Nodes)
                "DB-2")                                           // Default database that DocumentStore will interact with
        ) {
            // Create a new database
//            DatabaseRecord databaseRecord = new DatabaseRecord(); // Create a new database
//            databaseRecord.setDatabaseName("MyNewDatabase");
//            store.maintenance().server().send(new CreateDatabaseOperation(databaseRecord));


            store.initialize();                                        // Each DocumentStore needs to be initialized before use.
            // This process establishes the connection with the Server
            // and downloads various configurations
            // e.g. cluster topology or client configuration

            // open a session of the store
            IDocumentSession session = store.openSession();
            // create a new employee
            Employee employee = new Employee();
            employee.FirstName = "Yuki";
            employee.LastName = "Bob";
            employee.Title = "Manager";

            //insert the new employee to database
            //session.store(employee);
            //session.saveChanges();

            // find the employee whose first name is Yuki using document query
            List<Employee> employees_list = session
                    .advanced()
                    .documentQuery(Employee.class)
                    .whereEquals("FirstName", "Yuki")
                    .toList();

            // find the employee id of the employee
            String employeeId = session.advanced().getDocumentId(employees_list.get(0));
            System.out.println("employee's ID in db is " + employeeId);
            System.out.println( "employees size is " + employees_list.size());

            // print all employees whose first name is Yuki
            for (Employee employee1 : employees_list) {
                System.out.println(employee1);
            }
            Employee employee1 = session.load(Employee.class, employeeId);
            System.out.println(employee1.FirstName);

            // change the title of Yuki to Vice President
            employee1.Title = "Vice President";
            // Updata the title in the database
            //session.saveChanges();


            // check the title again to see if the update is successful
            List<Employee> employees_list2 = session
                    .advanced()
                    .documentQuery(Employee.class)
                    .whereEquals("FirstName", "Yuki")
                    .toList();
            System.out.println("New title is" + employees_list2.get(0).Title);

            // make query using indexing
            List<CompanyDetails> list = session.query(Company.class)
                    .selectFields(CompanyDetails.class, new QueryData(
                            new String[] { "Name", "Address.City", "Address.Country"  },
                            new String[] { "companyName", "city", "country"})
                    ).toList();

            // print all companies name, city, and country
            for (CompanyDetails companyDetails : list) {
                System.out.println(companyDetails.companyName);
                System.out.println(companyDetails.city);
                System.out.println(companyDetails.country);
                System.out.println("__________________________");
            }


            System.out.println("*****************************");
            // print the RQL(RavenDB Query Language) for "Orders/Totals"
            IndexDefinition index
                    = store.maintenance()
                    .send(new GetIndexOperation("Orders/Totals"));
            System.out.println("RQL is " + index.getMaps());
            System.out.println("*****************************");


            GetDatabaseNamesOperation operation = new GetDatabaseNamesOperation(0, 2);
            String[] databaseNames = store.maintenance().server().send(operation);
            System.out.println("*****************************");
            for (String s : databaseNames) {
                System.out.println(s);
            }
            System.out.println("*****************************");




            System.out.println("*****************************");
            List<Employee> employees = session
                    .query(Employee.class)
                    .whereEquals("FirstName", "Robert")
                    .andAlso()
                    .whereEquals("LastName", "King")
                    .toList();

            for (Employee employee02 : employees) {
                System.out.println(employee02.FirstName);
                System.out.println(employee02.LastName);
                System.out.println(employee02.Title);
            }
            System.out.println("*****************************");


            System.out.println("*****************************");
            //new Employees_ByFullName().execute(store);
            employees = session
                    .query(Employee.class, Employees_ByFullName.class)
                    .whereEquals("FullName", "Robert King")
                    .toList();
            for (Employee employee02 : employees) {
                System.out.println(employee02.FirstName);
                System.out.println(employee02.LastName);
                System.out.println(employee02.Title);
            }
            System.out.println("*****************************");

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

            System.out.println("there are " + results.size() + " beverages");




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

