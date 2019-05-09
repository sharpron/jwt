package pub.ron.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 资源找不到将触发该异常
 * @author ron
 * 2019.05.08
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "资源找不到")
public class ResourceNotFoundException extends RuntimeException {
}
