package com.aibees.service.maria.multipart.domain.repo;

import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.entity.FileImage;
import com.aibees.service.maria.multipart.domain.entity.QFileImage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileImageRepo extends JpaRepository<FileImage, Long>, FileImageRepoCustom {

}

interface FileImageRepoCustom {
    List<FileImage> selectDisplayImageList(FileImageReq param);
    Long getFileImageMaxId();
}

@AllArgsConstructor
class FileImageRepoCustomImpl implements FileImageRepoCustom {

    private final JPAQueryFactory query;
    private static final QFileImage qFileImage = QFileImage.fileImage;

    @Override
    public List<FileImage> selectDisplayImageList(FileImageReq param) {
        return query.selectFrom(qFileImage)
            .where(qFileImage.displayYn.eq("Y"),
                   qFileImage.shotTime.lt(param.getShotTime()))
            .orderBy(qFileImage.shotTime.desc())
            .limit(param.getSearchSize())
            .fetch();
    }

    @Override
    public Long getFileImageMaxId() {
        return query.select(qFileImage.id.max())
                .from(qFileImage)
                .fetchOne();
    }

//    private BooleanExpression leastImageId(Long imageId) {
//        return imageId == null ? null : qFileImage.id.lt(imageId);
//    }
}