package com.aibees.service.maria.common.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.domain.dto.UserReq;
import com.aibees.service.maria.common.domain.dto.UserRes;
import com.aibees.service.maria.common.domain.dto.UserRole;
import com.aibees.service.maria.common.domain.entity.*;
import com.aibees.service.maria.common.domain.repo.*;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.utils.CryptoUtils;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMasterRepo userMasterRepo;
    private final UserDetailRepo userDetailRepo;
    private final UserEncryptionRepo userEncryptionRepo;
    private final UserAuthorityRepo authorityRepo;
    private final UserAuthRepo userAuthRepo;

    /**
     * User 로그인
     * TODO : 시스템 별로 구분하는 로직 필요
     * param : email / passwd / loginkey
     * @param param
     * @return
     */
    @Transactional
    public UserRes userLogin(UserReq param, String systemCd) {
        UserAuthority systemAuth = authorityRepo.findById(systemCd).orElseThrow(() -> new MariaException("잘못된 접근의 로그인입니다."));

        if (!systemAuth.getLoginKey().equals(param.getLoginKey())) {
            throw new MariaException("접속정보가 없는 로그인입니다.");
        }

        // UserRes result
        UserRes response = new UserRes();

        // param parsing
        String[] emails = param.getEmail().split("@");
        String passwd = param.getPassword();

        Optional<UserMaster> user = userMasterRepo.findByEmailAndEmailPostfix(emails[0], emails[1]);

        // 사용자 정보 찾음
        if (user.isPresent()) {
            Long uuid = user.get().getUuid();
            UserDetail detail = userDetailRepo.findById(uuid).orElse(null);

            // Admin 계정은 비밀번호 없어도 들어갈 수 있도록(처음만 가능)
            if (Objects.isNull(detail) && StringUtils.isEquals(AccConstant.YES, user.get().getAdmin())) {
                String newSalt = CryptoUtils.getSalt();
                String encryptedPwd = CryptoUtils.EncryptPasswd(passwd, newSalt);

                UserDetail newDetail = UserDetail.builder()
                        .uuid(uuid)
                        .password(encryptedPwd)
                        .otp(newSalt)
                        .errcnt("0")
                        .build();

                userDetailRepo.save(newDetail);

                // 일반 사용자
            } else {
                if (Objects.isNull(detail)) {
                    throw new MariaException("로그인 정보가 없습니다.");
                }
                // 비밀번호 저장되어있음
                String userSalt = detail.getOtp();
                String encrypted = CryptoUtils.EncryptPasswd(passwd, userSalt);

                if (!StringUtils.isEquals(detail.getPassword(), encrypted)) {
                    // 로그인 실패
                    throw new MariaException("로그인 정보가 없습니다.");
                }
            }
            // 로그인 성공. 후처리 시작

            // user Role 조회 & Set
            List<UserRole> roleList = userAuthRepo.findAllByUuid(uuid)
                .stream()
                .map(data -> UserRole.builder()
                    .authCd(data.getAuthCd()).authNm(data.getAuthNm()).build())
                .collect(Collectors.toList());

            // access Token
            // token 새로 생성 & Set
            String accessToken = CryptoUtils.getSalt();

            // access Time
            Optional<UserEncryption> tokenOpt = userEncryptionRepo.findById(uuid);
            if (tokenOpt.isPresent()) {
                UserEncryption userToken = tokenOpt.get();
                userToken.setAccessToken(accessToken);
                userToken.setAccessExpiredTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusDays(1));
            } else {
                UserEncryption newEnc = UserEncryption.builder()
                    .uuid(uuid)
                    .accessExpiredTime(LocalDateTime.now().plusDays(1))
                    .accessToken(accessToken)
                    .build();
                userEncryptionRepo.save(newEnc);
            }

            response.setName(user.get().getName());
            response.setAccessToken(accessToken);
            response.setAdmin(user.get().getAdmin());
            response.setRoleList(roleList);
            response.setAccessTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        } else {
            throw new MariaException("로그인 정보가 없습니다.");
        }
        return response;
    }

    public ResponseEntity<ResponseData> refreshToken() {
        return null;
    }

    /******************************************************
     ******************* Private Method *******************
     ******************************************************/
    private boolean checkToken() {
        return true;
    }
}
