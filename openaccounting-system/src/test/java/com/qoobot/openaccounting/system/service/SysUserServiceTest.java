package com.qoobot.openaccounting.system.service;

import com.qoobot.openaccounting.system.dto.UserCreateRequest;
import com.qoobot.openaccounting.system.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SysUserService单元测试
 *
 * @author openaccounting
 */
@SpringBootTest
@Transactional
class SysUserServiceTest {

    @Autowired
    private SysUserService userService;

    @Test
    void testCreateUser() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        request.setPassword("Test123!");
        request.setEmail("test@example.com");
        request.setMobile("13800138000");
        request.setRealName("测试用户");

        Long userId = userService.createUser(request);

        assertNotNull(userId);
        assertTrue(userId > 0);

        SysUser user = userService.getById(userId);
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("测试用户", user.getRealName());
    }

    @Test
    void testUpdateUser() {
        // 先创建用户
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setUsername("testuser2");
        createRequest.setPassword("Test123!");
        createRequest.setEmail("test2@example.com");
        createRequest.setMobile("13800138001");
        createRequest.setRealName("测试用户2");

        Long userId = userService.createUser(createRequest);

        // 更新用户
        SysUser user = userService.getById(userId);
        user.setRealName("更新后的用户");
        userService.updateById(user);

        // 验证更新
        SysUser updatedUser = userService.getById(userId);
        assertEquals("更新后的用户", updatedUser.getRealName());
    }

    @Test
    void testDeleteUser() {
        // 先创建用户
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser3");
        request.setPassword("Test123!");
        request.setEmail("test3@example.com");
        request.setRealName("测试用户3");

        Long userId = userService.createUser(request);

        // 删除用户
        userService.removeById(userId);

        // 验证删除
        SysUser user = userService.getById(userId);
        assertEquals(1, user.getDeleted());
    }
}
