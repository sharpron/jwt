package pub.ron.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 新增已经存在的对象将触发该异常
 * @author ron
 * 2019.05.08
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "新增失败，可能对象已经存在")
public class ExistedAddException extends RuntimeException {
}
