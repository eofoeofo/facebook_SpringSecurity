package com.koreait.facebook.security;

import com.koreait.facebook.user.model.UserEntity;

public interface IAuthenticationFacade {
    UserEntity getLoginUser();
    int getLoginUserPk();
}
