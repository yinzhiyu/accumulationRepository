package com.randy.training.utils;

import android.content.Context;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/911:05
 * desc   :
 */
@DisplayName("我的第一个测试用例")
@ExtendWith(MockitoExtension.class)
class PhotoUtilsTest {
    @Mock
    public String jUnitStr;

    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

    @BeforeEach
    public void tearup() {
        System.out.println("当前测试方法开始");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("当前测试方法结束");
    }

    @DisplayName("我的第一个测试")
    @Test
    public void testFirstTest() {
        System.out.println("我的第一个测试开始测试");
    }

    @DisplayName("我的第二个测试")
    @Test
    public void testSecondTest() {
        MockedStatic<PhotoUtils> photoUtils = Mockito.mockStatic(PhotoUtils.class);
//        photoUtils.verify(PhotoUtils.test(jUnitStr));
        photoUtils.close();
        System.out.println("我的第二个测试开始测试");
    }


}