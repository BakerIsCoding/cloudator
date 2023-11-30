package com.example.registrationlogindemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
import com.example.registrationlogindemo.repository.UserAccessRepository;
import com.example.registrationlogindemo.repository.UserRepository;

@Service
public class UserAccessService {
    public static final int MAX_FAILED_ATTEMPTS = 3;

    private UserAccess userAccess;

    //private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours
     
    @Autowired
    private UserAccessRepository repo;
     
    public void increaseFailedAttempts(UserAccess userAccess) {
        int newFailAttempts = userAccess.getCounter() + 1;
        repo.updateCounter(userAccess.getId(), newFailAttempts);
    }
     
    public void resetFailedAttempts(UserAccess userAccess) {
        repo.updateCounter(userAccess.getId(), 0);
    }
     
    public void lock(UserAccess userAccess) {
        userAccess.setIsblocked(true);
        //user.setLockTime(new Date());
        repo.save(userAccess);
    }

    public UserAccess findById(Long id) {
        System.out.println("id");
        System.err.println(id);
        return repo.fetchUserAccess(id);
    }

    
     /* 
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
         
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
             
            repo.save(user);
             
            return true;
        }
         
        return false;
    }
    */
}
