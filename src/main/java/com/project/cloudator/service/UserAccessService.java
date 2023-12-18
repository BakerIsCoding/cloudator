package com.project.cloudator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.cloudator.entity.UserAccess;
import com.project.cloudator.repository.UserAccessRepository;

@Service
public class UserAccessService {
    public static final int MAX_FAILED_ATTEMPTS = 3;

    // COMENTADO
    // private UserAccess userAccess;

    // private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24
    // hours

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
        // user.setLockTime(new Date());
        repo.save(userAccess);
    }

    public void unLock(UserAccess userAccess) {
        userAccess.setIsblocked(false);
        repo.save(userAccess);
    }

    public UserAccess findById(Long id) {
        return repo.fetchUserAccess(id);
    }
}
