import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String hashed = encoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashed);
        System.out.println("Matches: " + encoder.matches(password, hashed));
        
        // Проверим пароль из базы данных
        String dbPassword = "$2a$10$LlyoQH62n47Gf94w6DzEXeMGuWqgZgeylWs6gr5IYhhx2BHcxRq7u";
        System.out.println("DB password matches: " + encoder.matches(password, dbPassword));
    }
}









