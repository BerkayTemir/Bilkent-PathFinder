package service;

public class test {

    public static void main(String[] args) throws Exception {
        AuthService auth = new AuthService();

        System.out.println("Supabase status: " + auth.testConnection());
    }
}
