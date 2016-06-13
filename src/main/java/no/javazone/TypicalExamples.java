package no.javazone;

public class TypicalExamples {

    /**
     * Sort of typical web request, presented in a typical blocking, sequential manner.
     *
     */
    public Object getSomething(Object parameters) {
        validate(parameters);
        String id = String.valueOf(parameters);
        Person person = getPersonFromWebService(id);
        Address adresse = getAddressFromWebService(id);
        postEvent(new PersonEvent(id));
        return translate(person, adresse);
    }


    //TODO: add example for typical batch job

    private PersonInCity translate(Person person, Address adresse) {
        return new PersonInCity(person.name, adresse.city);
    }


    private void postEvent(PersonEvent personEvent) {

    }

    private void validate(Object parameters) {

    }

    public Person getPersonFromWebService(String id) {
        return new Person("Noen", 29);
    }

    public Address getAddressFromWebService(String id) {
        return new Address("Hus 15", "Gate 1", "1337", "Sandvika");
    }

    public static class Person {
        public final String name;
        public final int age;
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static class Address {
        public final String street1;
        public final String street2;
        public final String postcode;
        public final String city;

        public Address(String street1, String street2, String postcode, String city) {
            this.street1 = street1;
            this.street2 = street2;
            this.postcode = postcode;
            this.city = city;
        }
    }

    public static class PersonInCity {
        public final String name;
        public final String city;

        public PersonInCity(String name, String city) {
            this.name = name;
            this.city = city;
        }
    }

    public static class PersonEvent {
        private String id;

        public PersonEvent(String id) {
            this.id = id;
        }
    }

}
