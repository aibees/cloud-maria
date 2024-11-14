package com.aibees.service.maria.common.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aibees.service.maria.common.domain.dto.UserDto;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.domain.entity.UserDetail;
import com.aibees.service.maria.common.domain.entity.UserEncryption;
import com.aibees.service.maria.common.domain.entity.UserMaster;
import com.aibees.service.maria.common.domain.repo.UserDetailRepo;
import com.aibees.service.maria.common.domain.repo.UserEncryptionRepo;
import com.aibees.service.maria.common.domain.repo.UserMasterRepo;
import com.aibees.service.maria.common.utils.CryptoUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMasterRepo userMasterRepo;
    private final UserDetailRepo userDetailRepo;
    private final UserEncryptionRepo userEncryptionRepo;

    public ResponseEntity<ResponseData> userLogin(UserDto param) {
        String[] emails = param.getEmail().split("@");
        String passwd = param.getPassword();

        Optional<UserMaster> user = userMasterRepo.findByEmailAndEmailPostfix(emails[0], emails[1]);

        String result = "success";
        String message = "";

        // 사용자 정보 찾음
        if (user.isPresent()) {
            Long uuid = user.get().getUuid();
            UserDetail detail = userDetailRepo.findById(uuid).orElse(null);

            if (Objects.isNull(detail) && StringUtils.equals("Y", user.get().getAdmin())) {
                String newSalt = CryptoUtils.geSalt();
                String encryptedPwd = CryptoUtils.EncryptPasswd(passwd, newSalt);

                UserDetail newDetail = UserDetail.builder()
                        .uuid(uuid)
                        .password(encryptedPwd)
                        .otp(newSalt)
                        .errcnt("0")
                        .build();

                userDetailRepo.save(newDetail);

            } else {
                // 비밀번호 저장되어있음
                String userSalt = detail.getOtp();
                String encrypted = CryptoUtils.EncryptPasswd(passwd, userSalt);

                if(StringUtils.equals(encrypted, detail.getPassword())) {
                    result = "success";
                    message = "로그인 성공";
                } else {
                    result = "failed";
                    message = "비밀번호가 틀립니다.";
                }
            }
        } else {
            result = "failed";
            message = "비밀번호가 틀립니다..";
        }

        return ResponseEntity.ok(
            ResponseData.builder()
                .data(ImmutableMap.of(
                    "result", result,
                    "message", message,
                    "loginTime", LocalDateTime.now()
                ))
                .build()
        );
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
