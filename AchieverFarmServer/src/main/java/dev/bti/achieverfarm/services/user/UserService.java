package dev.bti.achieverfarm.services.user;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.logging.auth.Type;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.service.LogService;
import dev.bti.achieverfarm.models.User;
import dev.bti.achieverfarm.models.req.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private LogService logService;

    public <T> T getUser(String id, Class<T> tClass) throws AuthException {
        User user = repository.findById(id).orElseThrow(() -> {
            Common.Log.auth(logService, "", Level.E, id, Type.USNF);
            return Constants.Throws.UserNotFound;
        });

        if (tClass == User.class) {
            return tClass.cast(user);
        } else {
            return tClass.cast(user.map());
        }
    }

    public List<User> getUsers() {
        return repository.findAll();
    }


    public void updateUser(String id, UserReq userReq) throws AuthException {
        User user = repository.findById(id).orElseThrow(() -> {
            Common.Log.auth(logService, "", Level.E, id, Type.USNF);
            return Constants.Throws.UserNotFound;
        });

        user.update(userReq);

        Common.Log.auth(logService, "", Level.I, user.getEmail(), Type.UPDT);
        repository.save(user);
    }
}
