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

    /**
     * Incrementa el número de intentos fallidos de acceso de un usuario.
     *
     * @param userAccess El acceso del usuario que se quiere modificar.
     */
    public void increaseFailedAttempts(UserAccess userAccess) {
        int newFailAttempts = userAccess.getCounter() + 1;
        repo.updateCounter(userAccess.getId(), newFailAttempts);
    }

    /**
     * Restablece el número de intentos fallidos de acceso de un usuario.
     *
     * @param userAccess El acceso del usuario que se quiere modificar.
     */
    public void resetFailedAttempts(UserAccess userAccess) {
        repo.updateCounter(userAccess.getId(), 0);
    }

    /**
     * Bloquea el acceso de un usuario.
     *
     * @param userAccess El acceso del usuario que se quiere bloquear.
     */
    public void lock(UserAccess userAccess) {
        userAccess.setIsblocked(true);
        // user.setLockTime(new Date());
        repo.save(userAccess);
    }

    /**
     * Desbloquea el acceso de un usuario.
     *
     * @param userAccess El acceso del usuario que se quiere desbloquear.
     */
    public void unLock(UserAccess userAccess) {
        userAccess.setIsblocked(false);
        repo.save(userAccess);
    }

    /**
     * Busca el acceso de un usuario por su ID.
     *
     * @param id El ID del usuario que se quiere buscar.
     * @return El acceso del usuario con el ID proporcionado.
     */
    public UserAccess findById(Long id) {
        return repo.fetchUserAccess(id);
    }

}
