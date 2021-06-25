package com.koreait.facebook.user;

import com.koreait.facebook.common.EmailService;
import com.koreait.facebook.common.MyFileUtils;
import com.koreait.facebook.common.MySecurityUtils;
import com.koreait.facebook.security.IAuthenticationFacade;
import com.koreait.facebook.user.model.UserEntity;
import com.koreait.facebook.user.model.UserProfileEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private EmailService email;

    @Autowired
    private MySecurityUtils secUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthenticationFacade auth;

    @Autowired
    private MyFileUtils myFileUtils;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserProfileMapper profileMapper;

    public int join(UserEntity param) {
        String authCd = secUtils.getRandomDigit(5);

        //비밀번호 암호화
        String hashedPw = passwordEncoder.encode(param.getPw());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        int result = mapper.join(param);

        if(result == 1) { //메일 쏘기!! (id, authcd값을 메일로 쏜다.)
            String subject = "[얼굴책] 인증메일입니다.";
            String txt = String.format("<a href=\"http://localhost:8090/user/auth?email=%s&authCd=%s\">인증하기</a>"
                    , param.getEmail(), authCd);
            email.sendMimeMessage(param.getEmail(), subject, txt);
        }
        return result;
    }

    //이메일 인증 처리
    public int auth(UserEntity param) {
        return mapper.auth(param);
    }

    public void profileImg(MultipartFile[] imgArr) {
        UserEntity loginUser = auth.getLoginUser();
        int iuser = loginUser.getIuser();
        System.out.println("iuser : " + iuser);
        String target = "profile/" + iuser;

        UserProfileEntity profileParam = new UserProfileEntity();
        profileParam.setIuser(iuser);

        for(MultipartFile img : imgArr) {
            String saveFileNm = myFileUtils.transferTo(img, target);
            //saveFileNm이 null이 아니라면 t_user_profile 테이블에 insert
            if(saveFileNm != null) {
                profileParam.setImg(saveFileNm);

                if(loginUser.getMainProfile() == null && profileMapper.insUserProfile(profileParam) == 1) {
                    // insert는 성공했지만, 메인프사가 없는경우
                    UserEntity param2 = new UserEntity();
                    param2.setIuser(loginUser.getIuser());
                    param2.setMainProfile(saveFileNm);

                    if(mapper.updUser(param2) == 1) {
                        loginUser.setMainProfile(saveFileNm);
                    }
                }
            }
        }
    }
    public List<UserProfileEntity> selUserProfileList(UserEntity param) {
        return profileMapper.selUserProfileList(param);
    }

    // 메인 이미지 변경경
   public int updUserMainProfile(UserProfileEntity param) {
        param.setIuser(auth.getLoginUserPk());
        return mapper.updUserMainProfile(param);
    }
}
