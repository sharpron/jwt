package pub.ron.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 更新不存在的对象将触发该异常
 * @author ron
 * 2019.05.08
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "修改失败，可能对象不存在")
public class NotExistedUpdateException extends RuntimeException {
}
