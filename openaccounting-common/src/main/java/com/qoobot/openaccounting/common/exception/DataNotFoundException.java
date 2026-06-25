package com.qoobot.openaccounting.common.exception;

import com.qoobot.openaccounting.common.constant.ResultCode;
import lombok.Getter;

/**
 * 数据未找到异常
 *
 * @author openaccounting
 */
@Getter
public class DataNotFoundException extends BusinessException {

    public DataNotFoundException(String message) {
        super(ResultCode.DATA_NOT_FOUND.getCode(), message);
    }

    public DataNotFoundException(String resource, Object id) {
        super(ResultCode.DATA_NOT_FOUND.getCode(), resource + "不存在: " + id);
    }
}
