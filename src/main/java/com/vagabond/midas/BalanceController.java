package com.vagabond.midas;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class BalanceController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/balance")
    public Balance getBalance(@RequestParam String userId) {
        Long userIdLong = Long.parseLong(userId);
        UserRecord user = userRepository.findById(userIdLong).orElse(null);
        if (user != null) {
            return new Balance(user.getBalance());
        } else {
            return new Balance(0);
        }
    }
}
