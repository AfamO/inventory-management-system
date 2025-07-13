package afamo.app.inventory.models;

public class User {
    private String firstName;
    private  String lastName;
    private  int age;
    private  String phone;
    private  String address;

    private User (UserBuilder userBuilder) {
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.age = userBuilder.age;
        this.phone = userBuilder.phone;
        this.address = userBuilder.address;
    }
    public static  class  UserBuilder {
        private final  String firstName;
        private  final  String lastName;
        private int age;
        private String phone;
        private  String address;

        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        public  UserBuilder age(int age) {
            this.age = age;
            return this;
        }
        public  UserBuilder phone (String phone) {
            this.phone = phone;
            return  this;
        }
        public  UserBuilder address (String address) {
            this.address = address;
            return  this;
        }

        public  User build () {
            return  new User(this);
        }
    }
}
