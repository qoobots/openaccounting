package com.qoobot.openaccounting.common.constant;

/**
 * 响应码枚举
 *
 * @author openaccounting
 */
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请登录"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "请求资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    CONFLICT(409, "资源冲突"),

    // 服务器错误 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // 业务错误 1000-1999
    BUSINESS_ERROR(1000, "业务处理失败"),
    VALIDATION_ERROR(1001, "参数校验失败"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    DATA_DUPLICATE(1003, "数据已存在"),
    OPERATION_NOT_ALLOWED(1004, "不允许的操作"),
    STATUS_ERROR(1005, "状态错误"),

    // 用户认证相关 2000-2099
    LOGIN_FAILED(2000, "登录失败"),
    LOGIN_EXPIRED(2001, "登录已过期"),
    TOKEN_INVALID(2002, "令牌无效"),
    TOKEN_EXPIRED(2003, "令牌已过期"),
    PASSWORD_ERROR(2004, "密码错误"),
    USER_NOT_FOUND(2005, "用户不存在"),
    USER_DISABLED(2006, "用户已被禁用"),
    USER_LOCKED(2007, "用户已被锁定"),

    // 权限相关 2100-2199
    PERMISSION_DENIED(2100, "权限不足"),
    ROLE_NOT_FOUND(2101, "角色不存在"),

    // 科目管理相关 3000-3099
    ACCOUNT_CODE_DUPLICATE(3000, "科目编码已存在"),
    ACCOUNT_NAME_DUPLICATE(3001, "科目名称已存在"),
    ACCOUNT_HAS_CHILDREN(3002, "科目存在子科目，无法删除"),
    ACCOUNT_HAS_BALANCE(3003, "科目存在余额，无法删除"),
    ACCOUNT_LEVEL_LIMIT(3004, "科目层级超过限制"),

    // 凭证管理相关 3100-3199
    VOUCHER_NOT_FOUND(3100, "凭证不存在"),
    VOUCHER_STATUS_ERROR(3101, "凭证状态错误"),
    VOUCHER_NUMBER_DUPLICATE(3102, "凭证号已存在"),
    VOUCHER_NOT_BALANCED(3103, "凭证借贷不平衡"),
    VOUCHER_ENTRY_LIMIT(3104, "凭证分录数量限制"),
    VOUCHER_AUDITED(3105, "凭证已审核，无法修改"),
    VOUCHER_POSTED(3106, "凭证已过账，无法修改"),

    // 会计期间相关 3200-3299
    PERIOD_CLOSED(3200, "会计期间已关闭"),
    PERIOD_NOT_OPEN(3201, "会计期间未开启"),
    PERIOD_EXISTS(3202, "会计期间已存在"),

    // 文件上传相关 4000-4099
    FILE_UPLOAD_FAILED(4000, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(4001, "文件类型不允许"),
    FILE_SIZE_EXCEED(4002, "文件大小超过限制"),
    FILE_NOT_FOUND(4003, "文件不存在"),

    // 系统管理相关 5000-5099
    COMPANY_NOT_FOUND(5000, "公司不存在"),
    DEPARTMENT_NOT_FOUND(5001, "部门不存在"),
    DICT_NOT_FOUND(5002, "数据字典不存在"),

    // 预算管理相关 6000-6099
    BUDGET_EXISTS(6000, "预算已存在"),
    BUDGET_VERSION_EXISTS(6001, "预算版本已存在"),
    BUDGET_EXCEEDED(6002, "预算超支"),

    // 资产管理相关 7000-7099
    ASSET_NOT_FOUND(7000, "资产不存在"),
    ASSET_CODE_DUPLICATE(7001, "资产编码已存在"),
    ASSET_DISPOSED(7002, "资产已处置"),

    // 工作流相关 8000-8099
    WORKFLOW_NOT_FOUND(8000, "工作流不存在"),
    TASK_NOT_FOUND(8001, "任务不存在"),
    TASK_COMPLETED(8002, "任务已完成"),
    PROCESS_COMPLETED(8003, "流程已完成"),
    TASK_NOT_CURRENT_USER(8004, "任务不属于当前用户"),
    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
