package rmanager.adminapi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.commons.entity.User;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.service.UserService;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class UserRoleAspect {

    private static Logger logger = LogManager.getLogger(UserRoleAspect.class);

    private UserService userService;

    @Autowired
    public UserRoleAspect(UserService userService) {
        this.userService = userService;
    }

    @Pointcut(value = "@annotation(rmanager.adminapi.annotation.AuthRequired)")
    private void callAtMyServiceSecurityAnnotation() { }

    @Around("callAtMyServiceSecurityAnnotation()")
    public Object checkUserRole(ProceedingJoinPoint thisJoinPoint) throws Throwable {

        Method method = ((MethodSignature) thisJoinPoint.getSignature()).getMethod();
        AuthRequired annotation = method.getAnnotation(AuthRequired.class);
        UserRole[] userRolesWithTheRuleForThisMethod = annotation.value();

        String token = thisJoinPoint.getArgs().clone()[0].toString();
        User user = userService.getUserByToken(token);

        if (user == null) {
            logger.info("User unauthorized");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        for (UserRole userRole: userRolesWithTheRuleForThisMethod) {
            if (userRole.equals(user.getUserRole())) {
                return thisJoinPoint.proceed();
            }
        }
        logger.info("Denied access. Method name: " + method.getName() + " / user Login: " + user.getLogin() + " with id: " + user.getUserId() + " / UserRole: " + user.getUserRole().name());
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

}
