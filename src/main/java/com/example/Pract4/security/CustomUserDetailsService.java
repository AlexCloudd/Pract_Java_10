package com.example.Pract4.security;

import com.example.Pract4.entity.User;
import com.example.Pract4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        List<Object[]> results = userRepository.findByUsernameOrEmailWithRoles(usernameOrEmail);
        
        if (results.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден: " + usernameOrEmail);
        }
        
        // Создаем пользователя из первого результата
        Object[] firstRow = results.get(0);
        User user = new User();
        user.setId((Long) firstRow[0]);
        user.setCreatedAt(((java.sql.Timestamp) firstRow[1]).toLocalDateTime());
        user.setEmail((String) firstRow[2]);
        user.setFirstName((String) firstRow[3]);
        user.setIsActive((Boolean) firstRow[4]);
        user.setLastName((String) firstRow[5]);
        user.setPassword((String) firstRow[6]);
        user.setSubscriptionType(User.SubscriptionType.valueOf((String) firstRow[7]));
        user.setUpdatedAt(((java.sql.Timestamp) firstRow[8]).toLocalDateTime());
        user.setUsername((String) firstRow[9]);
        
        // Собираем роли из всех результатов
        Set<User.Role> roles = new HashSet<>();
        for (Object[] row : results) {
            if (row[10] != null) { // role column
                String roleString = (String) row[10];
                try {
                    roles.add(User.Role.valueOf(roleString));
                } catch (IllegalArgumentException e) {
                    // Игнорируем неизвестные роли
                }
            }
        }
        user.setRoles(roles);
        
        return new CustomUserDetails(user);
    }
}
